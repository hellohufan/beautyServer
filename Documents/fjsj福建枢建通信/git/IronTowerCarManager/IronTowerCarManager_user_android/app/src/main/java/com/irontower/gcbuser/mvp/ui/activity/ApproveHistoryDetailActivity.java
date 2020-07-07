package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.di.component.DaggerApproveHistoryDetailComponent;
import com.irontower.gcbuser.di.module.ApproveHistoryDetailModule;
import com.irontower.gcbuser.mvp.contract.ApproveHistoryDetailContract.View;
import com.irontower.gcbuser.mvp.model.api.Api;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import com.irontower.gcbuser.mvp.model.entity.Dict;
import com.irontower.gcbuser.mvp.model.entity.Evaluate;
import com.irontower.gcbuser.mvp.model.entity.Order;
import com.irontower.gcbuser.mvp.model.entity.OrderCar;
import com.irontower.gcbuser.mvp.model.entity.OrderTrack;
import com.irontower.gcbuser.mvp.presenter.ApproveHistoryDetailPresenter;
import com.irontower.gcbuser.mvp.ui.adapter.OrderCarAdapter;
import com.irontower.gcbuser.mvp.ui.adapter.TimeLineAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;


public class ApproveHistoryDetailActivity extends
    BaseActivity<ApproveHistoryDetailPresenter> implements View {

  @BindView(R.id.tvAccompany)
  TextView tvAccompany;

  @BindView(R.id.rvCarDriver)
  RecyclerView rvOrderCar;
  @BindView(R.id.layDispatchCar)
  LinearLayout layDispatchCar;
  @BindView(R.id.tvOrderNo)
  TextView tvOrderNo;
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
  @BindView(R.id.tvEndTime)
  TextView tvEndTime;
  @BindView(R.id.tvRemark)
  TextView tvRemark;
  @BindView(R.id.tvOrgName)
  TextView tvOrgName;
  @BindView(R.id.layContent)
  LinearLayout layContent;
  @BindView(R.id.tvSpeed)
  TextView tvSpeed;
  @BindView(R.id.tvService)
  TextView tvService;
  @BindView(R.id.tvCarNo)
  TextView tvCarNo;

  @Named("TimeLine")
  @Inject
  LayoutManager timeLineLayoutManager;
  @Named("CarDriver")
  @Inject
  LayoutManager orderVehicleLayoutManager;
  @Inject
  TimeLineAdapter timeLineAdapter;
  @Inject
  OrderCarAdapter orderCarAdapter;
  @Inject
  RxPermissions rxPermissions;
  @Inject
  MaterialDialog materialDialog;
  @BindView(R.id.tvOrderReason)
  TextView tvOrderReason;
  @BindView(R.id.layDriverPosition)
  RelativeLayout layDriverPosition;
  private Order postOrder;
  private List<Dict> carTypeDicts = new ArrayList<>();
  private String orderCarNo, phone;
  private String orderNo, carNo, passengerTel, driverName;
  private Integer carId;
  private List<OrderTrack> orderTrackList = new ArrayList<>();
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
  private Attach attach;
  private List<Attach> attachList = new ArrayList<>();
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
    DaggerApproveHistoryDetailComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .approveHistoryDetailModule(new ApproveHistoryDetailModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_approve_history_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    orderNo = getIntent().getStringExtra("orderNo");
    rvOrderCar.setNestedScrollingEnabled(false);
    rvTimeLine.setNestedScrollingEnabled(false);

    rvTimeLine.setLayoutManager(timeLineLayoutManager);
    rvTimeLine.setAdapter(timeLineAdapter);
    rvOrderCar.setLayoutManager(orderVehicleLayoutManager);
    orderCarAdapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, android.view.View view, int position) {
        OrderCar orderCar = orderCarAdapter.getData().get(position);

        if (postOrder.getState() != 21) {
          Intent intent = new Intent();
          intent.putExtra("orderCar", orderCar);
          intent.putExtra("order", postOrder);
          intent.putParcelableArrayListExtra("attachList",
              (ArrayList<? extends Parcelable>) attachList);

          intent.setClass(getActivity(), OrderCarDetail2Activity.class);
          startActivity(intent);
        }

      }
    });

    rvOrderCar.setAdapter(orderCarAdapter);
    mPresenter.getData(orderNo);
    carTypeDicts.addAll(AppUtil.getCarTypes());

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


  @Override
  public Activity getActivity() {
    return this;
  }

  @Override
  public void init(Order order, List<OrderCar> orderCars, String node,
      List<OrderTrack> orderTracks,
      List<Attach> attaches) {
    if (orderCars.size() == 1) {
      OrderCar orderCar = orderCars.get(0);
      orderCarNo = orderCar.getOrderCarNo();
      if (orderCar.getState() == null) {
        tvNodeName.setText(AppUtil.getDictName(order.getState() + "", AppUtil.getOrderStates()));
      } else {
        tvNodeName.setText(AppUtil.getDictName(orderCar.getState() + "", AppUtil.getOrderStates()));
      }

      carNo = orderCar.getCarNo();
      carId = orderCar.getCarId();
      driverName = orderCar.getDriverName();
      phone = orderCar.getDriverPhone();
      tvCarNo.setText(carNo);
      if (orderCar.getDriverId() == null) {
        layOneDriver.setVisibility(android.view.View.GONE);
      } else {
        layOneDriver.setVisibility(android.view.View.VISIBLE);
      }
      tvDriverName.setText(orderCar.getDriverName());
      layComment.setVisibility(android.view.View.GONE);
      if (orderCar.getState() != null) {

        if (orderCar.getEvaluate() != null) {
          layComment.setVisibility(android.view.View.VISIBLE);
          initEvaluate(orderCar.getEvaluate());
        }

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
        orderTrackList.addAll(orderCar.getCarTracks());


      }

    }
    if (orderCars.size() > 1)

    {
      layDispatchCar.setVisibility(android.view.View.VISIBLE);
      tvNodeName.setText(AppUtil.getDictName(order.getState() + "", AppUtil.getOrderStates()));
      orderCarAdapter.setNewData(orderCars);
    }
    if (orderCars.size() == 0) {
      tvNodeName.setText(AppUtil.getDictName(order.getState() + "", AppUtil.getOrderStates()));

    }

    Map<String, Integer> map = new HashMap<>();
    for (
        OrderCar orderCar : orderCars)

    {
      if (orderCar.getCarType() != null) {
        String name = AppUtil.getDictName(orderCar.getCarType().toString(), carTypeDicts);
        if (map.containsKey(name)) {
          map.put(name, map.get(name) + 1);
        } else {
          map.put(name, 1);
        }

      }

    }
    orderTrackList.addAll(orderTracks);
    timeLineAdapter.setNewData(orderTrackList);
    tvCarDetail.setText("用车人:" + order.getPassenger());
    passengerTel = order.getPassengerTel();

    layContent.setVisibility(android.view.View.VISIBLE);

    tvOrderNo.setText(order.getOrderNo());
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

    order.setMap(map);
    postOrder = order;
    if (attaches.size() == 0) {
      layNoAttachment.setVisibility(android.view.View.VISIBLE);
      layOneAttachment.setVisibility(android.view.View.GONE);
      layManyAttachment.setVisibility(android.view.View.GONE);
    } else {
      attach = attaches.get(0);
      attachList.addAll(attaches);
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


  @OnClick(R.id.ivCall)
  public void onViewClicked() {
    mPresenter.call(phone);
  }


  @OnClick(R.id.ivPassengerCall)
  public void onViewClicked2() {
    mPresenter.call(passengerTel);
  }

  @Override
  public RxPermissions getRxpermission() {
    return rxPermissions;
  }

  @OnClick(R.id.layPayBack)
  public void payBack() {
    Intent intent = new Intent();
    intent.putExtra("orderCarNo", orderCarNo);
    intent.setClass(getActivity(), PayBackTrackActivity.class);
    startActivity(intent);
  }

  @OnClick(R.id.layDriverPosition)
  public void onViewClicked3() {
    Intent intent = new Intent();
    intent.putExtra("carId", carId);
    intent.putExtra("driverName", driverName);
    intent.putExtra("driverPhone", phone);
    intent.putExtra("carNo", carNo);
    intent.setClass(getActivity(), DriverPositionActivity.class);
    startActivity(intent);

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
            (ArrayList<? extends Parcelable>) attachList);
        intent.setClass(getActivity(), ManyAttachmentActivity.class);
        startActivity(intent);
        break;
      default:
        break;
    }
  }
}
