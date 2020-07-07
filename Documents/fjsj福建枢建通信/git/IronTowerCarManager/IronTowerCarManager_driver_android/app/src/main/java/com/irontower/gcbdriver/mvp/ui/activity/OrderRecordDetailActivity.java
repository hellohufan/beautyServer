package com.irontower.gcbdriver.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.StringUtils;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.app.EventBusTags;
import com.irontower.gcbdriver.app.utils.AppUtil;
import com.irontower.gcbdriver.di.component.DaggerOrderRecordDetailComponent;
import com.irontower.gcbdriver.di.module.OrderRecordDetailModule;
import com.irontower.gcbdriver.mvp.contract.OrderRecordDetailContract.View;
import com.irontower.gcbdriver.mvp.model.entity.Dict;
import com.irontower.gcbdriver.mvp.model.entity.Evaluate;
import com.irontower.gcbdriver.mvp.model.entity.Order;
import com.irontower.gcbdriver.mvp.model.entity.OrderCar;
import com.irontower.gcbdriver.mvp.model.entity.OrderTrack;
import com.irontower.gcbdriver.mvp.model.entity.Report;
import com.irontower.gcbdriver.mvp.presenter.OrderRecordDetailPresenter;
import com.irontower.gcbdriver.mvp.ui.adapter.TimeLineAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class OrderRecordDetailActivity extends BaseActivity<OrderRecordDetailPresenter> implements
    View {

  @BindView(R.id.scrollView)
  NestedScrollView nestedScrollView;

  @Inject
  MaterialDialog materialDialog;

  @BindView(R.id.layExpand)
  RelativeLayout layExpand;

  @BindView(R.id.layApplyFee)
  RelativeLayout layApplyFee;

  @BindView(R.id.layShrink)
  LinearLayout layShrink;
  @BindView(R.id.layDetail)
  LinearLayout layDetail;
  @BindView(R.id.layComment)
  LinearLayout layComment;
  @BindView(R.id.layOrderTracks)
  LinearLayout layOrderTracks;

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @Inject
  RxPermissions rxPermissions;
  @Inject
  LayoutManager mLayoutManager;
  @Inject
  TimeLineAdapter mAdapter;
  @BindView(R.id.tvPassenger)
  TextView tvPassenger;
  @BindView(R.id.tvPassengerNum)
  TextView tvPassengerNum;
  @BindView(R.id.tvCarType)
  TextView tvCarType;
  @BindView(R.id.tvCarUse)
  TextView tvCarUse;
  @BindView(R.id.tvCreateTime)
  TextView tvCreateTime;
  @BindView(R.id.tvBeginTime)
  TextView tvBeginTime;
  @BindView(R.id.tvDistanceType)
  TextView tvDistanceType;
  @BindView(R.id.tvBeginAddr)
  TextView tvBeginAddr;
  @BindView(R.id.tvEndAddr)
  TextView tvEndAddr;
  @BindView(R.id.tvEndTime)
  TextView tvEndTime;
  @BindView(R.id.tvRemark)
  TextView tvRemark;
  @BindView(R.id.tvOrderNo)
  TextView tvOrderNo;
  @BindView(R.id.tvProcess)
  TextView tvProcess;
  @BindView(R.id.tvComment)
  TextView tvComment;
  @BindView(R.id.ivSpeed1)
  ImageView ivSpeed1;
  @BindView(R.id.ivSpeed2)
  ImageView ivSpeed2;
  @BindView(R.id.ivSpeed3)
  ImageView ivSpeed3;
  @BindView(R.id.ivSpeed4)
  ImageView ivSpeed4;
  @BindView(R.id.ivSpeed5)
  ImageView ivSpeed5;
  @BindView(R.id.ivService1)
  ImageView ivService1;
  @BindView(R.id.ivService2)
  ImageView ivService2;
  @BindView(R.id.ivService3)
  ImageView ivService3;
  @BindView(R.id.ivService4)
  ImageView ivService4;
  @BindView(R.id.ivService5)
  ImageView ivService5;
  @BindView(R.id.tvSpeed)
  TextView tvSpeed;
  @BindView(R.id.tvService)
  TextView tvService;
  @BindView(R.id.toolbar_back)
  RelativeLayout toolbarBack;
  @BindView(R.id.toolbar_title)
  TextView toolbarTitle;
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.ivCall)
  ImageView ivCall;
  @BindView(R.id.layPayBack)
  LinearLayout layPayBack;
  @BindView(R.id.tvClear)
  TextView tvClear;
  @BindView(R.id.layNoClear)
  RelativeLayout layNoClear;
  @BindView(R.id.tvToll)
  TextView tvToll;
  @BindView(R.id.tvPark)
  TextView tvPark;
  @BindView(R.id.tvRest)
  TextView tvRest;
  @BindView(R.id.tvMile)
  TextView tvMile;
  @BindView(R.id.tvMinutes)
  TextView tvMinutes;
  @BindView(R.id.tvGasCost)
  TextView tvGasCost;
  @BindView(R.id.tvOtherCost)
  TextView tvOtherCost;
  @BindView(R.id.layReport)
  LinearLayout layReport;
  @BindView(R.id.layClear)
  LinearLayout layClear;
  @BindView(R.id.tvTollUnit)
  TextView tvTollUnit;
  @BindView(R.id.tvParkUnit)
  TextView tvParkUnit;
  @BindView(R.id.tvRestUnit)
  TextView tvRestUnit;
  @BindView(R.id.tvMileUnit)
  TextView tvMileUnit;
  @BindView(R.id.tvMinutesUnit)
  TextView tvMinutesUnit;
  @BindView(R.id.tvGasCostUnit)
  TextView tvGasCostUnit;
  @BindView(R.id.tvOtherCostUnit)
  TextView tvOtherCostUnit;
  private String phone;
  private List<Dict> carTypeDicts = new ArrayList<>();

  private List<Dict> carStateDicts = new ArrayList<>();
  private OrderCar orderCar;
  private Report report;
  @BindView(R.id.tvOrgName)
  TextView tvOrgName;

  @BindView(R.id.tvAttitude)
  TextView tvAttitude;
  @BindView(R.id.ivAttitude1)
  ImageView ivAttitude1;
  @BindView(R.id.ivAttitude2)
  ImageView ivAttitude2;
  @BindView(R.id.ivAttitude3)
  ImageView ivAttitude3;
  @BindView(R.id.ivAttitude4)
  ImageView ivAttitude4;
  @BindView(R.id.ivAttitude5)
  ImageView ivAttitude5;


  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerOrderRecordDetailComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .orderRecordDetailModule(new OrderRecordDetailModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_order_record_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    carTypeDicts.addAll(AppUtil.getCarTypes());
    carStateDicts.addAll(AppUtil.getCarState());

    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(mAdapter);

    mPresenter.getData(getIntent().getStringExtra("orderCarNo"));

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


  @OnClick({R.id.layExpand, R.id.ivCall, R.id.layShrink, R.id.layPayBack, R.id.layApplyFee})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layExpand:
        layExpand.setVisibility(android.view.View.GONE);
        layDetail.setVisibility(android.view.View.VISIBLE);
        nestedScrollView.post(() -> nestedScrollView.fullScroll(ScrollView.FOCUS_DOWN));
        break;
      case R.id.ivCall:
        mPresenter.callPhone(phone);
        break;
      case R.id.layShrink:
        layExpand.setVisibility(android.view.View.VISIBLE);
        layDetail.setVisibility(android.view.View.GONE);
        break;

      case R.id.layPayBack:

        payBack();
        break;

      case R.id.layApplyFee:

        Intent intent = new Intent();
        intent.putExtra("orderCarNo", orderCar.getOrderCarNo());
        intent.putExtra("report", report);
        intent.setClass(getActivity(), ApplyFeeDetailActivity.class);
        startActivity(intent);
        break;
      default:
        break;

    }
  }

  private void payBack() {
    Intent intent = new Intent();
    intent.putExtra("orderCarNo", getIntent().getStringExtra("orderCarNo"));
    intent.setClass(getActivity(), PayBackTrackActivity.class);
    startActivity(intent);
  }


  @Override
  public Activity getActivity() {
    return this;
  }

  @Override
  public RxPermissions getRxPermissions() {
    return rxPermissions;
  }

  @Override
  public Resources getResources() {
    Resources res = super.getResources();
    Configuration config = new Configuration();
    config.setToDefaults();
    res.updateConfiguration(config, res.getDisplayMetrics());
    return res;

  }

  @Override
  public void init(Order order, List<OrderCar> orderCars, String node,
      Evaluate evaluate,
      List<OrderTrack> orderTracks, Report report) {
    if (evaluate == null) {
      layComment.setVisibility(android.view.View.GONE);
    } else {
      layComment.setVisibility(android.view.View.VISIBLE);
      initEvaluate(evaluate);
    }
    this.report = report;

    if (orderCars.size() > 0) {
      orderCar = orderCars.get(0);

      if (orderCar.getState() == 8 || orderCar.getState() == 9) {
        layReport.setVisibility(android.view.View.VISIBLE);
        tvClear.setVisibility(android.view.View.GONE);
        layApplyFee.setVisibility(android.view.View.VISIBLE);
        if (report == null) {
          layClear.setVisibility(android.view.View.GONE);
          layNoClear.setVisibility(android.view.View.VISIBLE);
        } else {
          layClear.setVisibility(android.view.View.VISIBLE);
          layNoClear.setVisibility(android.view.View.GONE);

          initReport(report);
        }

      } else {
        layApplyFee.setVisibility(android.view.View.GONE);
        layNoClear.setVisibility(android.view.View.GONE);
        if (report == null) {
          layReport.setVisibility(android.view.View.GONE);
          layClear.setVisibility(android.view.View.GONE);
          tvClear.setVisibility(android.view.View.GONE);
        } else {
          layReport.setVisibility(android.view.View.VISIBLE);
          tvClear.setVisibility(android.view.View.VISIBLE);
          layClear.setVisibility(android.view.View.VISIBLE);

          initReport(report);
        }

      }

    }

    if (orderCars.size() > 0) {
      orderCar = orderCars.get(0);
      tvCarType.setText(AppUtil.getDictName(String.valueOf(orderCar.getCarType()), carTypeDicts));
      tvProcess.setText(AppUtil.getDictName(orderCars.get(0).getState() + "", carStateDicts));


    }
    if (orderTracks.size() == 0) {
      layOrderTracks.setVisibility(android.view.View.GONE);
    }

    nestedScrollView.setVisibility(android.view.View.VISIBLE);
    tvOrderNo.setText(order.getOrderNo());

    tvPassenger.setText(order.getPassenger());
    tvPassengerNum.setText(order.getPassengerNum() + "人");
    tvBeginTime.setText(order.getBeginTime());
    tvEndTime.setText("预计" + order.getEndTime());
    tvBeginAddr.setText(order.getBeginAddr());
    tvEndAddr.setText(order.getEndAddr());
    tvCreateTime.setText(order.getCreateTime());
    tvOrgName.setText("用车单位:" + order.getOrgName());
    tvDistanceType.setText(
        order.getDistanceType() == 2 ? "短途/" + order.getTripType() : "长途/" + order.getTripType());
    tvCarUse.setText(order.getUseName());
    phone = order.getPassengerTel();

    tvRemark.setText(StringUtils.isEmpty(order.getRemark()) ? "无" : order.getRemark());

  }

  private void initReport(Report report) {
    tvToll.setText(report.getToll());
    tvPark.setText(report.getPark());
    tvRest.setText(report.getRest());
    tvMile.setText(report.getMile());
    tvMinutes.setText(report.getMinutes());
    tvGasCost.setText(report.getGasCost());
    tvOtherCost.setText(report.getOtherCost());
    if (!StringUtils.isEmpty(report.getToll())) {
      tvTollUnit.setVisibility(android.view.View.VISIBLE);
    } else {
      tvTollUnit.setVisibility(android.view.View.GONE);
    }
    if (!StringUtils.isEmpty(report.getPark())) {

      tvParkUnit.setVisibility(android.view.View.VISIBLE);
    } else {
      tvParkUnit.setVisibility(android.view.View.GONE);
    }
    if (!StringUtils.isEmpty(report.getRest())) {

      tvRestUnit.setVisibility(android.view.View.VISIBLE);
    } else {
      tvRestUnit.setVisibility(android.view.View.GONE);
    }
    if (!StringUtils.isEmpty(report.getMile())) {

      tvMileUnit.setVisibility(android.view.View.VISIBLE);
    } else {
      tvMileUnit.setVisibility(android.view.View.GONE);

    }
    if (!StringUtils.isEmpty(report.getMinutes())) {

      tvMinutesUnit.setVisibility(android.view.View.VISIBLE);
    } else {
      tvMinutesUnit.setVisibility(android.view.View.GONE);

    }
    if (!StringUtils.isEmpty(report.getGasCost())) {

      tvGasCostUnit.setVisibility(android.view.View.VISIBLE);
    } else {
      tvGasCostUnit.setVisibility(android.view.View.GONE);

    }
    if (!StringUtils.isEmpty(report.getOtherCost())) {

      tvOtherCostUnit.setVisibility(android.view.View.VISIBLE);
    } else {
      tvOtherCostUnit.setVisibility(android.view.View.GONE);

    }


  }


  @OnClick(R.id.ivCall)
  public void onViewClicked() {
    mPresenter.call(phone);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    rxPermissions = null;
    if (materialDialog != null) {
      materialDialog.dismiss();
      materialDialog = null;
    }


  }

  private void initEvaluate(Evaluate evaluate) {
    if (evaluate.getGrade1() == 1) {
      ivSpeed1.setImageResource(R.mipmap.star_yellow);
      ivSpeed2.setImageResource(R.mipmap.star_gray);
      ivSpeed3.setImageResource(R.mipmap.star_gray);
      ivSpeed4.setImageResource(R.mipmap.star_gray);
      ivSpeed5.setImageResource(R.mipmap.star_gray);
      tvSpeed.setText("很差");
    }
    if (evaluate.getGrade1() == 2) {
      ivSpeed1.setImageResource(R.mipmap.star_yellow);
      ivSpeed2.setImageResource(R.mipmap.star_yellow);
      ivSpeed3.setImageResource(R.mipmap.star_gray);
      ivSpeed4.setImageResource(R.mipmap.star_gray);
      ivSpeed5.setImageResource(R.mipmap.star_gray);
      tvSpeed.setText("一般");

    }
    if (evaluate.getGrade1() == 3) {
      ivSpeed1.setImageResource(R.mipmap.star_yellow);
      ivSpeed2.setImageResource(R.mipmap.star_yellow);
      ivSpeed3.setImageResource(R.mipmap.star_yellow);
      ivSpeed4.setImageResource(R.mipmap.star_gray);
      ivSpeed5.setImageResource(R.mipmap.star_gray);
      tvSpeed.setText("满意");


    }
    if (evaluate.getGrade1() == 4) {
      ivSpeed1.setImageResource(R.mipmap.star_yellow);
      ivSpeed2.setImageResource(R.mipmap.star_yellow);
      ivSpeed3.setImageResource(R.mipmap.star_yellow);
      ivSpeed4.setImageResource(R.mipmap.star_yellow);
      ivSpeed5.setImageResource(R.mipmap.star_gray);
      tvSpeed.setText("非常满意");


    }
    if (evaluate.getGrade1() == 5) {
      ivSpeed1.setImageResource(R.mipmap.star_yellow);
      ivSpeed2.setImageResource(R.mipmap.star_yellow);
      ivSpeed3.setImageResource(R.mipmap.star_yellow);
      ivSpeed4.setImageResource(R.mipmap.star_yellow);
      ivSpeed5.setImageResource(R.mipmap.star_yellow);
      tvSpeed.setText("无可挑剔");

    }
    if (evaluate.getGrade2() == 1) {
      ivService1.setImageResource(R.mipmap.star_yellow);
      ivService2.setImageResource(R.mipmap.star_gray);
      ivService3.setImageResource(R.mipmap.star_gray);
      ivService4.setImageResource(R.mipmap.star_gray);
      ivService5.setImageResource(R.mipmap.star_gray);
      tvService.setText("很差");

    }
    if (evaluate.getGrade2() == 2) {
      ivService1.setImageResource(R.mipmap.star_yellow);
      ivService2.setImageResource(R.mipmap.star_yellow);
      ivService3.setImageResource(R.mipmap.star_gray);
      ivService4.setImageResource(R.mipmap.star_gray);
      ivService5.setImageResource(R.mipmap.star_gray);
      tvService.setText("一般");

    }
    if (evaluate.getGrade2() == 3) {
      ivService1.setImageResource(R.mipmap.star_yellow);
      ivService2.setImageResource(R.mipmap.star_yellow);
      ivService3.setImageResource(R.mipmap.star_yellow);
      ivService4.setImageResource(R.mipmap.star_gray);
      ivService5.setImageResource(R.mipmap.star_gray);
      tvService.setText("满意");
    }
    if (evaluate.getGrade2() == 4) {
      ivService1.setImageResource(R.mipmap.star_yellow);
      ivService2.setImageResource(R.mipmap.star_yellow);
      ivService3.setImageResource(R.mipmap.star_yellow);
      ivService4.setImageResource(R.mipmap.star_yellow);
      ivService5.setImageResource(R.mipmap.star_gray);
      tvService.setText("非常满意");

    }
    if (evaluate.getGrade2() == 5) {
      ivService1.setImageResource(R.mipmap.star_yellow);
      ivService2.setImageResource(R.mipmap.star_yellow);
      ivService3.setImageResource(R.mipmap.star_yellow);
      ivService4.setImageResource(R.mipmap.star_yellow);
      ivService5.setImageResource(R.mipmap.star_yellow);
      tvService.setText("无可挑剔");

    }
    if (evaluate.getGrade3() != null) {
      if (evaluate.getGrade3() == 1) {
        ivAttitude1.setImageResource(R.mipmap.star_yellow);
        ivAttitude2.setImageResource(R.mipmap.star_gray);
        ivAttitude3.setImageResource(R.mipmap.star_gray);
        ivAttitude4.setImageResource(R.mipmap.star_gray);
        ivAttitude5.setImageResource(R.mipmap.star_gray);
        tvAttitude.setText("很差");

      }
      if (evaluate.getGrade3() == 2) {
        ivAttitude1.setImageResource(R.mipmap.star_yellow);
        ivAttitude2.setImageResource(R.mipmap.star_yellow);
        ivAttitude3.setImageResource(R.mipmap.star_gray);
        ivAttitude4.setImageResource(R.mipmap.star_gray);
        ivAttitude5.setImageResource(R.mipmap.star_gray);
        tvAttitude.setText("一般");

      }
      if (evaluate.getGrade3() == 3) {
        ivAttitude1.setImageResource(R.mipmap.star_yellow);
        ivAttitude2.setImageResource(R.mipmap.star_yellow);
        ivAttitude3.setImageResource(R.mipmap.star_yellow);
        ivAttitude4.setImageResource(R.mipmap.star_gray);
        ivAttitude5.setImageResource(R.mipmap.star_gray);
        tvAttitude.setText("满意");
      }
      if (evaluate.getGrade3() == 4) {
        ivAttitude1.setImageResource(R.mipmap.star_yellow);
        ivAttitude2.setImageResource(R.mipmap.star_yellow);
        ivAttitude3.setImageResource(R.mipmap.star_yellow);
        ivAttitude4.setImageResource(R.mipmap.star_yellow);
        ivAttitude5.setImageResource(R.mipmap.star_gray);
        tvAttitude.setText("非常满意");

      }
      if (evaluate.getGrade3() == 5) {
        ivAttitude1.setImageResource(R.mipmap.star_yellow);
        ivAttitude2.setImageResource(R.mipmap.star_yellow);
        ivAttitude3.setImageResource(R.mipmap.star_yellow);
        ivAttitude4.setImageResource(R.mipmap.star_yellow);
        ivAttitude5.setImageResource(R.mipmap.star_yellow);
        tvAttitude.setText("无可挑剔");

      }
    }

    tvComment.setText(StringUtils.isEmpty(evaluate.getIdea()) ? "无评价" : evaluate.getIdea());

  }

  @Subscriber(tag = EventBusTags.SUBMITREPORT, mode = ThreadMode.MAIN)
  public void submitReport(String str) {
    mPresenter.getData(getIntent().getStringExtra("orderCarNo"));
  }


}
