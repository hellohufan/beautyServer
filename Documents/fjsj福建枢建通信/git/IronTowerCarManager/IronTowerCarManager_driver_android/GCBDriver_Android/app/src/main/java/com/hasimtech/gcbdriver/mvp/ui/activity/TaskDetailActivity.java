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
import com.irontower.gcbdriver.app.EventBusTags;
import com.irontower.gcbdriver.app.utils.AppUtil;
import com.irontower.gcbdriver.app.utils.DateUtil;
import com.irontower.gcbdriver.di.component.DaggerTaskDetailComponent;
import com.irontower.gcbdriver.di.module.TaskDetailModule;
import com.irontower.gcbdriver.mvp.contract.TaskDetailContract.View;
import com.irontower.gcbdriver.mvp.model.entity.Dict;
import com.irontower.gcbdriver.mvp.model.entity.Order;
import com.irontower.gcbdriver.mvp.model.entity.OrderCar;
import com.irontower.gcbdriver.mvp.presenter.TaskDetailPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class TaskDetailActivity extends BaseActivity<TaskDetailPresenter> implements
    View {

  @Inject
  MaterialDialog materialDialog;
  @Inject
  RxPermissions mRxPermissions;
  @BindView(R.id.ivUserImage)
  CircleImageView ivUserImage;
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
  @BindView(R.id.tvOrgName)
  TextView tvOrgName;
  @BindView(R.id.tvEndAddr)
  TextView tvEndAddr;
  @BindView(R.id.layNavigation)
  RelativeLayout layNavigation;
  @BindView(R.id.tvEndTime)
  TextView tvEndTime;
  @BindView(R.id.tvRemark)
  TextView tvRemark;
  @BindView(R.id.tvDetail)
  TextView tvDetail;
  @BindView(R.id.layEndOrder)
  RelativeLayout layEndOrder;
  @BindView(R.id.layContent)
  LinearLayout layContent;
  private String phone;
  private LatLng startLatLng, endLatLng;
  private String startAddr, endAddr;
  private MaterialDialog installDialog;

  private List<Dict> carTypeDicts = new ArrayList<>();
  private int state;
  private String orderCarNo;
  private MaterialDialog dialog;
  private String feeType;
  private boolean canClick = true;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerTaskDetailComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .taskDetailModule(new TaskDetailModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_task_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {

    carTypeDicts.addAll(AppUtil.getCarTypes());

    mPresenter.getData(getIntent().getStringExtra("orderCarNo"));
    startLatLng = getIntent().getParcelableExtra("beginGps");
    endLatLng = getIntent().getParcelableExtra("endGps");
    startAddr = getIntent().getStringExtra("beginAddr");
    endAddr = getIntent().getStringExtra("endAddr");


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


  @OnClick({R.id.ivPassengerTel, R.id.layNavigation, R.id.layEndOrder})
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
      case R.id.layEndOrder:
        if (state >= 5 && state < 8) {
          if (dialog == null) {
            dialog = new MaterialDialog.Builder(getActivity())
                .customView(R.layout.dialog_get_order, false)
                .backgroundColor(AlertDialog.THEME_HOLO_LIGHT)
                .show();
            android.view.View customView = dialog.getCustomView();
            if (customView != null) {
              RelativeLayout layCancel = customView.findViewById(R.id.layCancel);
              TextView textView = customView.findViewById(R.id.tvContent);
              textView.setText("确定" + tvDetail.getText().toString() + "吗？");
              RelativeLayout layConfirm = customView.findViewById(R.id.layConfirm);
              layCancel.setOnClickListener(view1 -> dialog.dismiss());
              layConfirm.setOnClickListener(view1 -> {
                dialog.dismiss();
                mPresenter.changeState(orderCarNo, state, feeType);
              });
            }


          } else {
            android.view.View customView = dialog.getCustomView();

            if (customView != null) {
              TextView textView = customView.findViewById(R.id.tvContent);
              textView.setText("确定" + tvDetail.getText().toString() + "吗？");

            }

            dialog.show();
          }
        }
        break;
      default:
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
      if (orderCar.getCarType() != null) {
        tvCarType.setText(AppUtil.getDictName(String.valueOf(orderCar.getCarType()), carTypeDicts));
        tvCarType.setVisibility(android.view.View.VISIBLE);
      } else {
        tvCarType.setVisibility(android.view.View.GONE);
      }

      feeType = orderCar.getFeeType();
      orderCarNo = orderCar.getOrderCarNo();
      state = orderCar.getState();
    }

    tvCarUse.setText(order.getUseName());

    tvCreateTime.setText(order.getCreateTime());
    tvBeginTime.setText(DateUtil
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
    if (state == 5) {
      tvDetail.setText("开始行程");
    } else if (state == 6) {
      if (StringUtils.isEmpty(feeType)) {
        tvDetail.setText("结束行程");
      } else {
        tvDetail.setText("确认乘客已上车");

      }
    } else if (state == 7) {
      tvDetail.setText("结束行程");
    } else if (state == 8) {
      tvDetail.setText("等待用户确认");
    } else {
      tvDetail.setVisibility(android.view.View.GONE);
    }


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
  public void initState(int finalNewState) {
    state = finalNewState;
    if (state == 5) {
      tvDetail.setText("开始行程");
    } else if (state == 6) {
      if (StringUtils.isEmpty(feeType)) {
        tvDetail.setText("结束行程");
      } else {
        tvDetail.setText("确认乘客已上车");

      }
    } else if (state == 7) {
      tvDetail.setText("结束行程");
    } else if (state == 8) {
      tvDetail.setText("等待用户确认");
    } else {
      tvDetail.setVisibility(android.view.View.GONE);
    }

  }

  @Subscriber(tag = EventBusTags.END_ORDER, mode = ThreadMode.MAIN)
  public void endOrder(String userName) {
    finish();
  }

  @Override
  protected void onStop() {
    super.onStop();
    canClick = true;
  }
}
