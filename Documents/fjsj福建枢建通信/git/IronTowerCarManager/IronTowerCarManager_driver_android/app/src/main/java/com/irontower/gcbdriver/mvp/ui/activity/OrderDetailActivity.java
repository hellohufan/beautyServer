package com.irontower.gcbdriver.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.blankj.utilcode.util.StringUtils;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.app.utils.AppUtil;
import com.irontower.gcbdriver.app.utils.DateUtil;
import com.irontower.gcbdriver.di.component.DaggerOrderDetailComponent;
import com.irontower.gcbdriver.di.module.OrderDetailModule;
import com.irontower.gcbdriver.mvp.contract.OrderDetailContract.View;
import com.irontower.gcbdriver.mvp.model.entity.Dict;
import com.irontower.gcbdriver.mvp.model.entity.Order;
import com.irontower.gcbdriver.mvp.model.entity.OrderCar;
import com.irontower.gcbdriver.mvp.presenter.OrderDetailPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;


public class OrderDetailActivity extends BaseActivity<OrderDetailPresenter> implements
    View {

  @Inject
  RxPermissions mRxPermissions;
  @Inject
  MaterialDialog materialDialog;
  @BindView(R.id.tvPassenger)
  TextView tvPassenger;

  @BindView(R.id.tvPassengerNum)
  TextView tvPassengerNum;
  @BindView(R.id.tvCarUse)
  TextView tvCarUse;
  @BindView(R.id.tvCarType)
  TextView tvCarType;
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
  @BindView(R.id.layNavigation)
  RelativeLayout layNavigation;
  @BindView(R.id.tvEndTime)
  TextView tvEndTime;
  @BindView(R.id.tvRemark)
  TextView tvRemark;
  @BindView(R.id.tvOrgName)
  TextView tvOrgName;
  @BindView(R.id.layTakeOrder)
  RelativeLayout layTakeOrder;
  @BindView(R.id.layContent)
  LinearLayout layContent;
  private List<Dict> carTypeDicts = new ArrayList<>();


  private String phone;
  private LatLng startLatLng, endLatLng;
  private String startAddr, endAddr;
  private MaterialDialog installDialog, takeOrderDialog;
  private boolean canClick = true;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerOrderDetailComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .orderDetailModule(new OrderDetailModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_order_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {

    carTypeDicts.addAll(AppUtil.getCarTypes());
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

  @Override
  public RxPermissions getRxPermissions() {
    return mRxPermissions;
  }

  @Override
  public Activity getActivity() {
    return this;
  }


  @OnClick({R.id.ivPassengerTel, R.id.layNavigation, R.id.layTakeOrder})
  public void onViewClicked(android.view.View view) {

    switch (view.getId()) {
      case R.id.ivPassengerTel:
        mPresenter.callPhone(phone);
        break;
      case R.id.layNavigation:

        if (!canClick) {
          return;
        }
        canClick = false;
        RouteParaOption para = new RouteParaOption()
            .startName(startAddr).endName(endAddr);
        if (startLatLng != null) {
          para.startPoint(startLatLng);

        }
        if (endLatLng != null) {
          para.endPoint(endLatLng);
        }

        try {
          BaiduMapRoutePlan.openBaiduMapDrivingRoute(para, this);
        } catch (Exception e) {
          e.printStackTrace();
          showDialog();
        }
        BaiduMapRoutePlan.finish(this);
        break;
      case R.id.layTakeOrder:
        takeOrder();
        break;
      default:
        break;
    }
  }

  private void takeOrder() {

    if (takeOrderDialog == null) {
      takeOrderDialog = new MaterialDialog.Builder(getActivity())
          .customView(R.layout.dialog_get_order, false)
          .backgroundColor(AlertDialog.THEME_HOLO_LIGHT)
          .show();
      android.view.View customView = takeOrderDialog.getCustomView();
      RelativeLayout layCancel = customView.findViewById(R.id.layCancel);
      RelativeLayout layConfirm = customView.findViewById(R.id.layConfirm);
      layCancel.setOnClickListener(view1 -> takeOrderDialog.dismiss());
      layConfirm.setOnClickListener(view1 -> {
        takeOrderDialog.dismiss();
        mPresenter.takeOrder(getIntent().getStringExtra("orderCarNo"));
      });

    } else {
      takeOrderDialog.show();
    }
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
  public void init(Order order, List<OrderCar> orderCars) {
    layContent.setVisibility(android.view.View.VISIBLE);
    tvPassenger.setText(order.getPassenger());
    tvPassengerNum.setText(order.getPassengerNum() + "人");
    if (orderCars.size() > 0) {
      OrderCar orderCar = orderCars.get(0);
      tvCarType.setText(AppUtil.getDictName(String.valueOf(orderCar.getCarType()), carTypeDicts));


    }
    tvCarUse.setText(order.getUseName());
    tvCreateTime.setText(order.getCreateTime());
    tvBeginTime.setText(
        DateUtil
            .formatDate(DateUtil.YYYY_MM_DD_HH_MI_SS, order.getBeginTime(), DateUtil.STRMM_DD_HH_MI)
            + "出发");
    tvOrgName.setText("用车单位:" + order.getOrgName());
    tvDistanceType.setText(
        order.getDistanceType() == 2 ? "短途/" + order.getTripType() : "长途/" + order.getTripType());
    tvBeginAddr.setText(order.getBeginAddr());
    tvEndAddr.setText(order.getEndAddr());
    tvEndTime.setText(DateUtil
        .formatDate(DateUtil.YYYY_MM_DD_HH_MI_SS, order.getEndTime(), DateUtil.STRMM_DD_HH_MI));
    phone = order.getPassengerTel();
    if (StringUtils.isEmpty(order.getRemark())) {
      tvRemark.setText("无");
    } else {
      tvRemark.setText(order.getRemark());

    }
    startLatLng = AppUtil.getLatLng(order.getBeginGps());
    endLatLng = AppUtil.getLatLng(order.getEndGps());
    startAddr = order.getBeginAddr();
    endAddr = order.getEndAddr();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (materialDialog != null) {
      materialDialog.dismiss();
      materialDialog = null;
    }
    if (installDialog != null) {
      installDialog.dismiss();
      installDialog = null;
    }

  }

  public void showDialog() {

    if (installDialog != null) {
      installDialog.show();

    } else {
      installDialog = new MaterialDialog.Builder(this)
          .title("提示")
          .positiveColor(ArmsUtils.getColor(getActivity(), R.color.colorPrimary))
          .negativeColor(ArmsUtils.getColor(getActivity(), R.color.empty_color))
          .content("您尚未安装百度地图app或app版本过低，点击确认安装")
          .negativeText(R.string.cancel)
          .positiveText(R.string.confirm)
          .onPositive(new SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog takeOrderDialog,
                @NonNull DialogAction which) {
              OpenClientUtil.getLatestBaiduMapApp(getActivity());


            }
          })
          .show();


    }

  }

  @Override
  protected void onStop() {
    super.onStop();
    canClick = true;
  }

}
