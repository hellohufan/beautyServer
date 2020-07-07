package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.StringUtils;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.di.component.DaggerOrderCarDetail2Component;
import com.irontower.gcbuser.di.module.OrderCarDetail2Module;
import com.irontower.gcbuser.mvp.contract.OrderCarDetail2Contract.View;
import com.irontower.gcbuser.mvp.model.api.Api;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import com.irontower.gcbuser.mvp.model.entity.Evaluate;
import com.irontower.gcbuser.mvp.model.entity.Order;
import com.irontower.gcbuser.mvp.model.entity.OrderCar;
import com.irontower.gcbuser.mvp.model.entity.OrderTrack;
import com.irontower.gcbuser.mvp.presenter.OrderCarDetail2Presenter;
import com.irontower.gcbuser.mvp.ui.adapter.TimeLineAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;


public class OrderCarDetail2Activity extends BaseActivity<OrderCarDetail2Presenter> implements
    View {

  @BindView(R.id.scrollView)
  NestedScrollView scrollView;
  @BindView(R.id.tvOrderNo)
  TextView tvOrderNo;
  @BindView(R.id.tvT)
  TextView tvT;
  @BindView(R.id.tvNodeName)
  TextView tvNodeName;
  @BindView(R.id.rvTimeLine)
  RecyclerView rvTimeLine;
  @BindView(R.id.layPayBack)
  RelativeLayout layPayBack;
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
  @BindView(R.id.tvComment)
  TextView tvComment;
  @BindView(R.id.layComment)
  LinearLayout layComment;
  @BindView(R.id.tvDriverName)
  TextView tvDriverName;
  @BindView(R.id.ivCall)
  ImageView ivCall;
  @BindView(R.id.layOneDriver)
  LinearLayout layOneDriver;
  @BindView(R.id.tvPassengerNum)
  TextView tvPassengerNum;
  @BindView(R.id.tvCarUse)
  TextView tvCarUse;
  @BindView(R.id.tvCarDetail)
  TextView tvCarDetail;
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
  @BindView(R.id.tvOrgName)
  TextView tvOrgName;
  @BindView(R.id.tvEndTime)
  TextView tvEndTime;
  @BindView(R.id.tvRemark)
  TextView tvRemark;
  @BindView(R.id.layContent)
  LinearLayout layContent;
  @BindView(R.id.tvSpeed)
  TextView tvSpeed;
  @BindView(R.id.tvService)
  TextView tvService;
  @BindView(R.id.tvOrderReason)
  TextView tvOrderReason;
  @BindView(R.id.tvCarNo)
  TextView tvCarNo;
  @BindView(R.id.tvAccompany)
  TextView tvAccompany;
  @BindView(R.id.layDriverPosition)
  RelativeLayout layDriverPosition;
  private Order order;
  private OrderCar orderCar;
  private String phone;
  @Inject
  RxPermissions rxPermissions;
  @Inject
  MaterialDialog materialDialog;

  @Inject
  LayoutManager timeLineLayoutManager;
  @Inject
  TimeLineAdapter timeLineAdapter;
  private List<OrderTrack> orderTrackList = new ArrayList<>();
  private String passengerTel;
  private Attach attach;
  @BindView(R.id.layOneAttachment)
  LinearLayout layOneAttachment;
  @BindView(R.id.layManyAttachment)
  LinearLayout layManyAttachment;
  @BindView(R.id.layNoAttachment)
  LinearLayout layNoAttachment;
  @BindView(R.id.ivOneAttachment)
  ImageView ivOneAttachment;
  @BindView(R.id.tvOneAttachment)
  TextView tvOneAttachment;
  private List<Attach> attaches = new ArrayList<>();
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
    DaggerOrderCarDetail2Component //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .orderCarDetail2Module(new OrderCarDetail2Module(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_order_car_detail2; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    scrollView.setFocusable(true);
    scrollView.setFocusableInTouchMode(true);
    scrollView.requestFocus();
    rvTimeLine.setFocusable(false);
    rvTimeLine.setNestedScrollingEnabled(false);
    order = getIntent().getParcelableExtra("order");
    orderCar = getIntent().getParcelableExtra("orderCar");
    attaches.addAll(getIntent().getParcelableArrayListExtra("attachList"));
    passengerTel = order.getPassengerTel();
    phone = orderCar.getDriverPhone();
    rvTimeLine.setNestedScrollingEnabled(false);
    rvTimeLine.setLayoutManager(timeLineLayoutManager);
    rvTimeLine.setAdapter(timeLineAdapter);

    orderTrackList.addAll(orderCar.getCarTracks());
    timeLineAdapter.setNewData(orderTrackList);

    if (orderCar.getEvaluate() != null) {
      layComment.setVisibility(android.view.View.VISIBLE);
      initEvaluate(orderCar.getEvaluate());
    }

    tvDriverName.setText(orderCar.getDriverName());
    if (orderTrackList.size() >= 1) {
      tvNodeName.setText(AppUtil.getDictName(orderCar.getState() + "", AppUtil.getOrderStates()));
    } else {
      tvNodeName.setText("暂未派车");
    }
    if (orderCar.getDriverId() == null) {
      layOneDriver.setVisibility(android.view.View.GONE);
    } else {
      layOneDriver.setVisibility(android.view.View.VISIBLE);
    }
    tvCarDetail.setText("用车人:" + order.getPassenger());
    if (orderCar.getState() >= 8 && orderCar.getState() < 13) {
      layPayBack.setVisibility(android.view.View.VISIBLE);
    } else {
      layPayBack.setVisibility(android.view.View.GONE);
    }
    if (orderCar.getState() >= 4 && orderCar.getState() < 8) {
      layDriverPosition.setVisibility(android.view.View.VISIBLE);
    } else {
      layDriverPosition.setVisibility(android.view.View.GONE);
    }
    tvOrderNo.setText(order.getOrderNo());
    tvCarNo.setText(orderCar.getCarNo());
    tvPassengerNum.setText(order.getPassengerNum() + "人");
    tvBeginTime.setText(order.getBeginTime());
    tvEndTime.setText(order.getEndTime());
    tvBeginAddr.setText(order.getBeginAddr());
    tvEndAddr.setText(order.getEndAddr());
    tvCreateTime.setText(order.getCreateTime());
    if (StringUtils.isEmpty(order.getDeptName())) {
      tvOrgName.setText("用车单位:" + order.getOrgName());
    } else {
      tvOrgName.setText("用车单位:" + order.getOrgName() + "/" + order.getDeptName());
    }
    tvDistanceType.setText(
        order.getDistanceType() == 2 ? "短途/" + order.getTripType() : "长途/" + order.getTripType());
    tvCarUse.setText(order.getUseName());

    tvRemark.setText(StringUtils.isEmpty(order.getRemark()) ? "无" : order.getRemark());
    tvAccompany.setText(StringUtils.isEmpty(order.getAccompany()) ? "无" : order.getAccompany());
    tvOrderReason.setText(order.getOrderReason());

    if (attaches.size() == 0) {
      layNoAttachment.setVisibility(android.view.View.VISIBLE);
      layOneAttachment.setVisibility(android.view.View.GONE);
      layManyAttachment.setVisibility(android.view.View.GONE);
    } else {
      attach = attaches.get(0);

      if (attaches.size() == 1) {
        layOneAttachment.setVisibility(android.view.View.VISIBLE);
        layNoAttachment.setVisibility(android.view.View.GONE);
        layManyAttachment.setVisibility(android.view.View.GONE);
      } else {
        layManyAttachment.setVisibility(android.view.View.VISIBLE);
        layOneAttachment.setVisibility(android.view.View.VISIBLE);
        layNoAttachment.setVisibility(android.view.View.GONE);
      }

      tvOneAttachment.setText(attach.getAttachName());

      if (attach.getAttachName().endsWith(".docx") || attach.getAttachName().endsWith(".doc")) {
        ivOneAttachment.setImageResource(R.mipmap.word);
      } else if (attach.getAttachName().endsWith(".xls") || attach.getAttachName()
          .endsWith(".xlsx")) {
        ivOneAttachment.setImageResource(R.mipmap.excel);
      } else if (attach.getAttachName().endsWith(".jpg") || attach.getAttachName().endsWith(".png")
          || attach.getAttachName()
          .endsWith(".jpeg")) {
        ivOneAttachment.setImageResource(R.mipmap.picture);
      } else {
        ivOneAttachment.setImageResource(R.mipmap.other_file);
      }

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


  @OnClick({R.id.layPayBack, R.id.ivCall, R.id.ivPassengerCall, R.id.layDriverPosition})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layPayBack:
        break;
      case R.id.ivCall:
        mPresenter.call(phone);

        break;
      case R.id.ivPassengerCall:
        mPresenter.call(passengerTel);

        break;
      case R.id.layDriverPosition:
        driverPosition();
        break;
      default:
    }
  }

  private void driverPosition() {
    Intent intent = new Intent();
    intent.putExtra("carId", orderCar.getCarId());
    intent.putExtra("driverName", orderCar.getDriverName());
    intent.putExtra("driverPhone", phone);
    intent.putExtra("carNo", orderCar.getCarNo());
    intent.setClass(getActivity(), DriverPositionActivity.class);
    startActivity(intent);
  }

  private void payBack() {
    Intent intent = new Intent();
    intent.putExtra("orderCarNo", orderCar.getOrderCarNo());
    intent.setClass(getActivity(), PayBackTrackActivity.class);
    startActivity(intent);
  }

  @Override
  public Activity getActivity() {
    return this;
  }

  @Override
  public RxPermissions getRxpermission() {
    return rxPermissions;
  }

  @OnClick({R.id.layOneAttachment, R.id.layManyAttachment})
  public void onViewClicked3(android.view.View view) {
    switch (view.getId()) {
      case R.id.layOneAttachment:
        mPresenter
            .getFile(Api.APP_DOMAIN.substring(0, Api.APP_DOMAIN.length() - 1) + attach.getPath(),
                attach.getAttachName());
        break;
      case R.id.layManyAttachment:
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("attachList",
            (ArrayList<? extends Parcelable>) attaches);
        intent.setClass(getActivity(), ManyAttachmentActivity.class);
        startActivity(intent);
        break;
      default:
        break;
    }
  }

}
