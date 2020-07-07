package com.irontower.gcbdriver.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.StringUtils;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.app.utils.DateUtil;
import com.irontower.gcbdriver.di.component.DaggerApplyBegOffDetailComponent;
import com.irontower.gcbdriver.di.module.ApplyBegOffDetailModule;
import com.irontower.gcbdriver.mvp.contract.ApplyBegOffDetailContract.View;
import com.irontower.gcbdriver.mvp.presenter.ApplyBegOffDetailPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;


public class ApplyBegOffDetailActivity extends BaseActivity<ApplyBegOffDetailPresenter> implements
    View {

  @BindView(R.id.tvBeginTime)
  TextView tvBeginTime;
  @BindView(R.id.tvEndTime)
  TextView tvEndTime;
  @BindView(R.id.tvType)
  TextView tvType;
  @BindView(R.id.etReason)
  AppCompatEditText etReason;
  private TimePickerView beginTimePV, endTimePV, typePV;
  @Inject
  MaterialDialog materialDialog;

  private List<String> begOffs = new ArrayList<>();

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerApplyBegOffDetailComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .applyBegOffDetailModule(new ApplyBegOffDetailModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_apply_beg_off_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    begOffs.addAll(Arrays.asList(ArmsUtils.getStringArray(getActivity(), R.array.begOffItems)));
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


  @OnClick({R.id.layBeginTime, R.id.layEndTime, R.id.laySubmit, R.id.layType})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layBeginTime:
        chooseBeginTime();
        break;
      case R.id.layEndTime:
        chooseEndTime();
        break;
      case R.id.laySubmit:
        if (StringUtils.isEmpty(tvBeginTime.getText())) {
          ArmsUtils.makeText(getActivity(), "请假开始时间不能为空");
          return;

        }
        if (StringUtils.isEmpty(tvEndTime.getText())) {
          ArmsUtils.makeText(getActivity(), "请假结束时间不能为空");
          return;


        }
        if (DateUtil
            .diffDate(DateUtil.getDate(DateUtil.YYYY_MM_DD_HH_MI, tvBeginTime.getText().toString()),
                DateUtil.getDate(DateUtil.YYYY_MM_DD_HH_MI, tvEndTime.getText().toString())) > 0) {
          ArmsUtils.makeText(getActivity(), "请假开始时间不能晚于请假结束时间");
          return;


        }
        if (StringUtils.isEmpty(tvType.getText())) {
          ArmsUtils.makeText(getActivity(), "请假类型不能为空");
          return;
        }
        if (StringUtils.isEmpty(etReason.getText().toString())) {
          ArmsUtils.makeText(getActivity(), "请假事由不能为空");
          return;

        }
        mPresenter.submit(tvBeginTime.getText().toString(), tvEndTime.getText().toString(),
            tvType.getText().toString(), etReason.getText().toString());

        break;
      case R.id.layType:
        new MaterialDialog.Builder(this)
            .title(R.string.title)
            .items(R.array.begOffItems)
            .itemsCallbackSingleChoice(StringUtils.isEmpty(tvType.getText()) ? 0
                    : begOffs.indexOf(tvType.getText().toString()),
                new MaterialDialog.ListCallbackSingleChoice() {
                  @Override
                  public boolean onSelection(MaterialDialog dialog, android.view.View itemView,
                      int which, CharSequence text) {
                    tvType.setText(text);
                    return false;
                  }


                })

            .show();
        break;
      default:

    }
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
          .setTitleText("请假开始时间")

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
          .setTitleText("请假结束时间")

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

  @Override
  public Activity getActivity() {
    return this;
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
