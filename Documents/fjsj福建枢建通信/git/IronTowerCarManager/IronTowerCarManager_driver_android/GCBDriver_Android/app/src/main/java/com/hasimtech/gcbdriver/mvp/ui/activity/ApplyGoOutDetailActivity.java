package com.irontower.gcbdriver.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.StringUtils;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.app.EventBusTags;
import com.irontower.gcbdriver.app.utils.AppUtil;
import com.irontower.gcbdriver.app.utils.DateUtil;
import com.irontower.gcbdriver.di.component.DaggerApplyGoOutDetailComponent;
import com.irontower.gcbdriver.di.module.ApplyGoOutDetailModule;
import com.irontower.gcbdriver.mvp.contract.ApplyGoOutDetailContract.View;
import com.irontower.gcbdriver.mvp.model.entity.BegGoOut;
import com.irontower.gcbdriver.mvp.model.entity.Dict;
import com.irontower.gcbdriver.mvp.presenter.ApplyGoOutDetailPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class ApplyGoOutDetailActivity extends BaseActivity<ApplyGoOutDetailPresenter> implements
    View {


  @BindView(R.id.tvCar)
  TextView tvCar;
  @BindView(R.id.layCar)
  LinearLayout layCar;
  @BindView(R.id.tvBeginTime)
  TextView tvBeginTime;
  @BindView(R.id.layBeginTime)
  LinearLayout layBeginTime;
  @BindView(R.id.tvEndTime)
  TextView tvEndTime;
  @BindView(R.id.layEndTime)
  LinearLayout layEndTime;
  @BindView(R.id.tvType)
  TextView tvType;
  @BindView(R.id.layType)
  LinearLayout layType;
  @BindView(R.id.etReason)
  AppCompatEditText etReason;
  @BindView(R.id.laySubmit)
  RelativeLayout laySubmit;
  private TimePickerView beginTimePV, endTimePV;
  private OptionsPickerView outTypePV;
  private MaterialDialog submitDialog;
  private List<String> outTypes = new ArrayList<>();
  private List<Dict> outTypeDicts = new ArrayList<>();
  private Integer carId;

  private BegGoOut begGoOut;
  @Inject
  MaterialDialog materialDialog;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerApplyGoOutDetailComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .applyGoOutDetailModule(new ApplyGoOutDetailModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_apply_go_out_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    outTypes.addAll(AppUtil.getCarOutType1());
    outTypeDicts.addAll(AppUtil.getCarOutType());
    tvBeginTime.setText(DateUtil.format(DateUtil.YYYY_MM_DD_HH_MI, DateUtil.getNow()));


  }


  @Override
  public void showLoading() {
    if (materialDialog != null) {
      materialDialog.show();
    }

  }

  @Override
  public void hideLoading() {
    if (materialDialog != null) {
      materialDialog.dismiss();
    }
  }

  @Override
  public void showMessage(@NonNull String message) {
    checkNotNull(message);
    ArmsUtils.snackbarText(message);
  }

  @Override
  public void launchActivity(@NonNull Intent intent) {
    checkNotNull(intent);
    ArmsUtils.startActivity(intent);
  }

  @Override
  public void killMyself() {
    finish();
  }


  @OnClick({R.id.layCar, R.id.layBeginTime, R.id.layEndTime, R.id.layType, R.id.etReason,
      R.id.laySubmit})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layCar:
        if (StringUtils.isEmpty(tvBeginTime.getText())) {
          ArmsUtils.makeText(getActivity(), "出场时间不能为空");
          return;

        }
//        if(DateUtil.diffDate(DateUtil.getDate(DateUtil.YYYY_MM_DD_HH_MI,tvBeginTime.getText().toString()),DateUtil.getNow())<0){
//          ArmsUtils.makeText(getActivity(),"开始时间不能是过去时间");
//          return;
//
//
//        }
        if (StringUtils.isEmpty(tvEndTime.getText())) {
          ArmsUtils.makeText(getActivity(), "返场时间不能为空");
          return;


        }

        if (DateUtil
            .diffDate(DateUtil.getDate(DateUtil.YYYY_MM_DD_HH_MI, tvBeginTime.getText().toString()),
                DateUtil.getDate(DateUtil.YYYY_MM_DD_HH_MI, tvEndTime.getText().toString())) > 0) {
          ArmsUtils.makeText(getActivity(), "开始时间不能晚于结束时间");
          return;


        }
        Intent intent = new Intent();
        intent.putExtra("beginTime", tvBeginTime.getText().toString());
        intent.putExtra("endTime", tvEndTime.getText().toString());
        intent.setClass(getActivity(), ChooseCarActivity.class);
        startActivity(intent);
        break;
      case R.id.layBeginTime:
        chooseBeginTime();
        break;
      case R.id.layEndTime:
        chooseEndTime();
        break;
      case R.id.layType:
        chooseType();
        break;
      case R.id.etReason:
        break;
      case R.id.laySubmit:
        if (StringUtils.isEmpty(tvBeginTime.getText())) {
          ArmsUtils.makeText(getActivity(), "出场时间不能为空");
          return;

        }
//        if(DateUtil.diffDate(DateUtil.getDate(DateUtil.YYYY_MM_DD_HH_MI,tvBeginTime.getText().toString()),DateUtil.getNow())<0){
//          ArmsUtils.makeText(getActivity(),"开始时间不能是过去时间");
//          return;
//
//
//        }
        if (StringUtils.isEmpty(tvEndTime.getText())) {
          ArmsUtils.makeText(getActivity(), "返场时间不能为空");
          return;


        }

        if (DateUtil
            .diffDate(DateUtil.getDate(DateUtil.YYYY_MM_DD_HH_MI, tvBeginTime.getText().toString()),
                DateUtil.getDate(DateUtil.YYYY_MM_DD_HH_MI, tvEndTime.getText().toString())) > 0) {
          ArmsUtils.makeText(getActivity(), "开始时间不能晚于结束时间");
          return;


        }
        if (StringUtils.isEmpty(tvCar.getText())) {
          ArmsUtils.makeText(getActivity(), "车辆不能为空");
          return;
        }
        if (StringUtils.isEmpty(tvType.getText())) {
          ArmsUtils.makeText(getActivity(), "出场类型不能为空");
          return;
        }
        if (StringUtils.isEmpty(etReason.getText().toString())) {
          ArmsUtils.makeText(getActivity(), "出场事由不能为空");
          return;

        }
        if (submitDialog != null) {
          submitDialog.show();
        } else {
          submitDialog = new MaterialDialog.Builder(this)
              .title("提示")
              .content("确认出场申请")
              .positiveText(R.string.confirm)
              .negativeText(R.string.cancel)
              .negativeColor(ArmsUtils.getColor(getActivity(), R.color.empty_color))
              .onPositive(new SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                  mPresenter
                      .submit(tvBeginTime.getText().toString(), tvEndTime.getText().toString(),
                          carId, AppUtil.getDictType(tvType.getText().toString(), outTypeDicts),
                          etReason.getText().toString().trim());

                }
              })
              .show();
        }

        break;
      default:
    }
  }

  @Override
  public Activity getActivity() {
    return this;
  }

  private void chooseBeginTime() {
    if (beginTimePV == null) {
      Calendar startDate = Calendar.getInstance();
      Calendar endDate = Calendar.getInstance();
      endDate.set(2020, 11, 31);
      beginTimePV = new TimePickerBuilder(this, new OnTimeSelectListener() {
        @Override
        public void onTimeSelect(Date date, android.view.View v) {//选中事件回调
          tvBeginTime.setText(DateUtil.format(DateUtil.YYYY_MM_DD_HH_MI, date));
        }
      }).setType(new boolean[]{true, true, true, true, true, false})
          .setTitleText("出场时间")

          .setTitleSize(17)
          .setCancelColor(ArmsUtils.getColor(getActivity(), R.color.text_hint))
          .setSubmitColor(ArmsUtils.getColor(getActivity(), R.color.colorPrimary))
          .setRangDate(startDate, endDate)//起始终止年月日设定
          .setCancelText("取消")//取消按钮文字
          .setSubmitText("确认")//确认按钮文字
          .setLabel("年", "月", "日", "时", "分", null)
          .build();
      beginTimePV.show();

    } else {
      Calendar calendar = Calendar.getInstance();
      if (StringUtils.isEmpty(tvBeginTime.getText().toString())) {
        beginTimePV.setDate(calendar);

      } else {
        calendar
            .setTime(DateUtil.getDate(DateUtil.YYYY_MM_DD_HH_MI, tvBeginTime.getText().toString()));
        beginTimePV.setDate(calendar);

      }
      beginTimePV.show();
    }

  }

  private void chooseEndTime() {
    if (endTimePV == null) {
      Calendar startDate = Calendar.getInstance();
      Calendar endDate = Calendar.getInstance();
      endDate.set(2020, 11, 31);
      endTimePV = new TimePickerBuilder(this, new OnTimeSelectListener() {
        @Override
        public void onTimeSelect(Date date, android.view.View v) {//选中事件回调
          tvEndTime.setText(DateUtil.format(DateUtil.YYYY_MM_DD_HH_MI, date));
        }
      }).setType(new boolean[]{true, true, true, true, true, false})
          .setTitleText("返场时间")

          .setTitleSize(17)
          .setCancelColor(ArmsUtils.getColor(getActivity(), R.color.text_hint))
          .setSubmitColor(ArmsUtils.getColor(getActivity(), R.color.colorPrimary))
          .setRangDate(startDate, endDate)//起始终止年月日设定
          .setCancelText("取消")//取消按钮文字
          .setSubmitText("确认")//确认按钮文字
          .setLabel("年", "月", "日", "时", "分", null)
          .build();
      endTimePV.show();

    } else {
      Calendar calendar = Calendar.getInstance();
      if (StringUtils.isEmpty(tvEndTime.getText().toString())) {
        endTimePV.setDate(calendar);

      } else {
        calendar
            .setTime(DateUtil.getDate(DateUtil.YYYY_MM_DD_HH_MI, tvEndTime.getText().toString()));
        endTimePV.setDate(calendar);

      }
      endTimePV.show();
    }
  }

  private void chooseType() {
    if (outTypePV != null) {

      if (!StringUtils.isEmpty(tvType.getText().toString())) {
        outTypePV.setSelectOptions(
            outTypes.indexOf(tvType.getText().toString()));
      }
      outTypePV.show();
    } else {
      outTypePV = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {


        @Override
        public void onOptionsSelect(int options1, int options2, int options3,
            android.view.View v) {

          tvType.setText(outTypes.get(options1));
        }
      }).setCancelColor(ArmsUtils.getColor(getActivity(), R.color.text_hint))
          .setSubmitColor(ArmsUtils.getColor(getActivity(), R.color.colorPrimary))
          .setCancelText("取消")//取消按钮文字
          .setSubmitText("确认").build();
      outTypePV.setPicker(outTypes);
      if (!StringUtils.isEmpty(tvType.getText().toString())) {
        outTypePV.setSelectOptions(
            outTypes.indexOf(tvType.getText().toString()));
      }
      outTypePV.show();
    }

  }

  @Subscriber(tag = EventBusTags.CHOOSE_CAR_SUCCESS, mode = ThreadMode.MAIN)
  public void chooesCarSuccess(Map<String, Object> map) {
    carId = (Integer) map.get("carId");
    tvCar.setText((CharSequence) map.get("carNo"));
  }

  @Subscriber(tag = EventBusTags.APPLY_GO_OUT_SUCCESS, mode = ThreadMode.MAIN)
  public void applyGoOutSuccess(String str) {
    finish();
  }


  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (materialDialog != null) {
      materialDialog.dismiss();
      materialDialog = null;
    }

  }
}
