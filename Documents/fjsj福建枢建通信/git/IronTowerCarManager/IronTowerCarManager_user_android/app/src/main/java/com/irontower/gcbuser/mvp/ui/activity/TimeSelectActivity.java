package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.TimePickerView.Builder;
import com.bigkoo.pickerview.TimePickerView.OnTimeSelectListener;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.app.utils.DateUtil;
import com.irontower.gcbuser.di.component.DaggerTimeSelectComponent;
import com.irontower.gcbuser.di.module.TimeSelectModule;
import com.irontower.gcbuser.mvp.contract.TimeSelectContract.View;
import com.irontower.gcbuser.mvp.presenter.TimeSelectPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.simple.eventbus.EventBus;


public class TimeSelectActivity extends BaseActivity<TimeSelectPresenter> implements
    View {

  @BindView(R.id.tvBeginCreateTime)
  TextView tvBeginCreateTime;
  @BindView(R.id.tvEndCreateTime)
  TextView tvEndCreateTime;
  @BindView(R.id.tvBeginTime1)
  TextView tvBeginTime1;
  @BindView(R.id.tvEndTime1)
  TextView tvEndTime1;
  private TimePickerView beginCreateTimePV, endCreateTImePv, beginPv, endPv;
  private String beginCreateTime, endCreateTime, beginTime, endTime;
  private Calendar calendar = Calendar.getInstance();


  @Override
  public void setupActivityComponent(@NonNull AppComponent appComponent) {
    DaggerTimeSelectComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .timeSelectModule(new TimeSelectModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(@Nullable Bundle savedInstanceState) {
    return R.layout.activity_time_select; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(@Nullable Bundle savedInstanceState) {
    beginCreateTime = getIntent().getStringExtra("beginCreateTime");
    endCreateTime = getIntent().getStringExtra("endCreateTime");
    tvBeginCreateTime.setText(beginCreateTime);
    tvEndCreateTime.setText(endCreateTime);
  }

  @Override
  public void showLoading() {

  }

  @Override
  public void hideLoading() {

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


  @OnClick({R.id.layBeginCreateTime, R.id.layEndCreateTime, R.id.laySearch})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layBeginCreateTime:
        if (beginCreateTimePV == null) {
          Calendar startDate = Calendar.getInstance();
          startDate.set(2017, 0, 1);
          Calendar endDate = Calendar.getInstance();
          endDate.set(2030, 11, 31);
          beginCreateTimePV = new Builder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, android.view.View v) {//选中事件回调
              beginCreateTime = DateUtil.format(DateUtil.YYYY_MM_DD, date);
              tvBeginCreateTime.setText(beginCreateTime);

            }
          }).setType(new boolean[]{true, true, true, false, false, false})

              .setTitleText("创建开始时间")
              .setTitleSize(17)
              .setCancelColor(ArmsUtils.getColor(this, R.color.text_hint))
              .setSubmitColor(ArmsUtils.getColor(this, R.color.colorPrimary))
              .setRangDate(startDate, endDate)//起始终止年月日设定
              .setCancelText("取消")//取消按钮文字
              .setSubmitText("确定")//确认按钮文字
              .setLabel("年", "月", "日", null, null, null)
              .build();

          calendar
              .setTime(
                  DateUtil.getDate(DateUtil.YYYY_MM_DD, beginCreateTime));
          beginCreateTimePV.setDate(calendar);

          beginCreateTimePV.show();
        } else {

          beginCreateTimePV.show();
        }
        break;
      case R.id.layEndCreateTime:
        if (endCreateTImePv == null) {
          Calendar startDate = Calendar.getInstance();
          startDate.set(2017, 0, 1);
          Calendar endDate = Calendar.getInstance();
          endDate.set(2030, 11, 31);
          endCreateTImePv = new Builder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, android.view.View v) {//选中事件回调
              endCreateTime = DateUtil.format(DateUtil.YYYY_MM_DD, date);
              tvEndCreateTime.setText(endCreateTime);
            }
          }).setType(new boolean[]{true, true, true, false, false, false})

              .setTitleText("创建结束时间")
              .setTitleSize(17)
              .setCancelColor(ArmsUtils.getColor(this, R.color.text_hint))
              .setSubmitColor(ArmsUtils.getColor(this, R.color.colorPrimary))
              .setRangDate(startDate, endDate)//起始终止年月日设定
              .setCancelText("取消")//取消按钮文字
              .setSubmitText("确定")//确认按钮文字
              .setLabel("年", "月", "日", null, null, null)
              .build();

          calendar
              .setTime(
                  DateUtil.getDate(DateUtil.YYYY_MM_DD, endCreateTime));
          endCreateTImePv.setDate(calendar);
          endCreateTImePv.show();

        } else {

          endCreateTImePv.show();
        }
        break;
      case R.id.laySearch:
        Map<String, Object> map = new HashMap<>();
        map.put("beginCreateTime", tvBeginCreateTime.getText().toString().trim());
        map.put("endCreateTime", tvEndCreateTime.getText().toString().trim());
        map.put("beginTime", tvBeginTime1.getText().toString().trim());
        map.put("endTime", tvEndTime1.getText().toString().trim());
        EventBus.getDefault().post(map, EventBusTags.TIMESELECT);
        finish();

        break;
      default:
        break;
    }
  }


  @OnClick({R.id.layBeginTime1, R.id.layEndTime1})
  public void onViewClicked1(android.view.View view) {
    switch (view.getId()) {
      case R.id.layBeginTime1:
        if (beginPv == null) {
          Calendar startDate = Calendar.getInstance();
          startDate.set(2017, 0, 1);
          Calendar endDate = Calendar.getInstance();
          endDate.set(2030, 11, 31);
          beginPv = new Builder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, android.view.View v) {//选中事件回调
              beginTime = DateUtil.format(DateUtil.YYYY_MM_DD, date);
              tvBeginTime1.setText(beginTime);

            }
          }).setType(new boolean[]{true, true, true, false, false, false})

              .setTitleText("出发开始时间")
              .setTitleSize(17)
              .setCancelColor(ArmsUtils.getColor(this, R.color.text_hint))
              .setSubmitColor(ArmsUtils.getColor(this, R.color.colorPrimary))
              .setRangDate(startDate, endDate)//起始终止年月日设定
              .setCancelText("取消")//取消按钮文字
              .setSubmitText("确定")//确认按钮文字
              .setLabel("年", "月", "日", null, null, null)
              .build();

          calendar
              .setTime(
                  new Date());
          beginPv.setDate(calendar);

          beginPv.show();
        } else {

          beginPv.show();
        }
        break;
      case R.id.layEndTime1:
        if (endPv == null) {
          Calendar startDate = Calendar.getInstance();
          startDate.set(2017, 0, 1);
          Calendar endDate = Calendar.getInstance();
          endDate.set(2030, 11, 31);
          endPv = new Builder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, android.view.View v) {//选中事件回调
              endTime = DateUtil.format(DateUtil.YYYY_MM_DD, date);
              tvEndTime1.setText(endTime);

            }
          }).setType(new boolean[]{true, true, true, false, false, false})

              .setTitleText("出发结束时间")
              .setTitleSize(17)
              .setCancelColor(ArmsUtils.getColor(this, R.color.text_hint))
              .setSubmitColor(ArmsUtils.getColor(this, R.color.colorPrimary))
              .setRangDate(startDate, endDate)//起始终止年月日设定
              .setCancelText("取消")//取消按钮文字
              .setSubmitText("确定")//确认按钮文字
              .setLabel("年", "月", "日", null, null, null)
              .build();

          calendar
              .setTime(
                  new Date());
          endPv.setDate(calendar);

          endPv.show();
        } else {

          endPv.show();
        }
        break;
      default:
        break;
    }
  }
}
