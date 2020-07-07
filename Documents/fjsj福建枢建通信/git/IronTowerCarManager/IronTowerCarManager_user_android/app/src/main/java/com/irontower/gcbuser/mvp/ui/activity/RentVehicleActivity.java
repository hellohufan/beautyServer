package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.StringUtils;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.di.component.DaggerRentVehicleComponent;
import com.irontower.gcbuser.di.module.RentVehicleModule;
import com.irontower.gcbuser.mvp.contract.RentVehicleContract.View;
import com.irontower.gcbuser.mvp.model.entity.CarCenter;
import com.irontower.gcbuser.mvp.model.entity.Dict;
import com.irontower.gcbuser.mvp.model.entity.FeeType;
import com.irontower.gcbuser.mvp.model.entity.Order;
import com.irontower.gcbuser.mvp.presenter.RentVehiclePresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class RentVehicleActivity extends BaseActivity<RentVehiclePresenter> implements
    View {

  @BindView(R.id.tvAccompany)
  TextView tvAccompany;
  @BindView(R.id.tvPassengerNum)
  TextView tvPassengerNum;
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
  @BindView(R.id.tvCarCenter)
  TextView tvCarCenter;
  @BindView(R.id.ivArrow2)
  ImageView ivArrow2;
  @BindView(R.id.layCarCenter)
  LinearLayout layCarCenter;
  @BindView(R.id.tvOrderReason)
  TextView tvOrderReason;
  @BindView(R.id.tvFeeType)
  TextView tvFeeType;
  @BindView(R.id.tvConfirm)
  TextView tvConfirm;
  @BindView(R.id.tvCarType)
  TextView tvCarType;
  @BindView(R.id.tvTitle)
  TextView tvTitle;
  @BindView(R.id.ivArrow)
  ImageView ivArrow;
  @BindView(R.id.layFeeType)
  LinearLayout layFeeType;
  @BindView(R.id.layChooseCarType)
  LinearLayout layChooseCarType;
  @BindView(R.id.layContent)
  LinearLayout layContent;
  private String orderCarNo;
  private List<String> carCenters = new ArrayList<>();
  private List<String> feeTypes = new ArrayList<>();
  private List<CarCenter> carCenterList = new ArrayList<>();
  private List<FeeType> feeTypeList = new ArrayList<>();
  private List<String> carTypes = new ArrayList<>();
  private List<Dict> carTypeDicts = new ArrayList<>();
  private MaterialDialog carCenDialog, confirmDialog, chooseCarTypeDialog;
  @Inject
  MaterialDialog materialDialog;
  private boolean rent;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerRentVehicleComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .rentVehicleModule(new RentVehicleModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_rent_vehicle; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    rent = getIntent().getBooleanExtra("rent", false);
    if (rent) {
      tvTitle.setText("平台租车");
      tvConfirm.setText("确认租车");
    } else {
      tvTitle.setText("平台派车");
      tvConfirm.setText("确认派车");
    }
    Order order = getIntent().getParcelableExtra("order");
    carTypes.addAll(AppUtil.getCarTypes1());
    carTypeDicts.addAll(AppUtil.getCarTypes());
    Collections.reverse(carTypes);
    Collections.reverse(carTypeDicts);

    tvPassengerNum.setText(order.getPassengerNum() + "人");

    tvBeginTime.setText(order.getBeginTime());
    tvEndTime.setText(order.getEndTime());
    tvBeginAddr.setText(order.getBeginAddr());
    tvEndAddr.setText(order.getEndAddr());
    tvCreateTime.setText(order.getCreateTime());
    tvDistanceType.setText(
        order.getDistanceType() == 2 ? "短途/" + order.getTripType() : "长途/" + order.getTripType());
    tvCarUse.setText(order.getUseName());

    tvRemark.setText(StringUtils.isEmpty(order.getRemark()) ? "无" : order.getRemark());
    tvAccompany.setText(StringUtils.isEmpty(order.getAccompany()) ? "无" : order.getAccompany());
    tvOrderReason.setText(order.getOrderReason());

    mPresenter.getCarCenter(rent);
    orderCarNo = getIntent().getStringExtra("orderCarNo");

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
  public void init(List<CarCenter> rows) {
    layContent.setVisibility(android.view.View.VISIBLE);
    carCenterList.addAll(rows);
    for (CarCenter row : rows) {
      carCenters.add(row.getOrgName());
    }
    if (carCenters.size() > 0) {
      tvCarCenter.setText(carCenters.get(0));
      mPresenter.getFeeTypes(rows.get(0).getOrgId());
    }


  }


  @OnClick({R.id.layCarCenter, R.id.layFeeType, R.id.layConfirm, R.id.layChooseCarType})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layCarCenter:
        if (carCenterList.size() == 0) {
          ArmsUtils.makeText(getActivity(), "无车务中心可供选择");
        } else {
          if (carCenDialog == null) {
            carCenDialog = new MaterialDialog.Builder(this)
                .title("车务中心选择")
                .items(carCenters)
                .itemsCallbackSingleChoice(StringUtils.isEmpty(tvCarCenter.getText()) ? 0
                        : carCenters.indexOf(tvCarCenter.getText().toString()),
                    new MaterialDialog.ListCallbackSingleChoice() {
                      @Override
                      public boolean onSelection(MaterialDialog dialog, android.view.View itemView,
                          int which, CharSequence text) {
                        tvCarCenter.setText(text);
                        tvFeeType.setText(null);
                        mPresenter.getFeeTypes(carCenterList.get(which).getOrgId());
                        return false;
                      }


                    })
                .show();

          } else {
            carCenDialog.setSelectedIndex(StringUtils.isEmpty(tvCarCenter.getText()) ? 0
                : carCenters.indexOf(tvCarCenter.getText().toString()));
            carCenDialog.show();

          }

        }

        break;
      case R.id.layFeeType:
        if (feeTypeList.size() != 0) {
          new MaterialDialog.Builder(this)
              .title("结算费用选择")
              .items(feeTypes)
              .itemsCallbackSingleChoice(StringUtils.isEmpty(tvFeeType.getText()) ? 0
                      : feeTypes.indexOf(tvFeeType.getText().toString()),
                  new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, android.view.View itemView,
                        int which, CharSequence text) {
                      tvFeeType.setText(text);
                      return false;
                    }


                  })

              .show();


        } else {
          ArmsUtils.makeText(getActivity(), "无结算费用可供选择");
        }

        break;
      case R.id.layConfirm:

        if (StringUtils.isEmpty(tvCarCenter.getText().toString())) {
          ArmsUtils.makeText(getActivity(), "请选择车务中心");
          return;
        }

        if (rent && StringUtils.isEmpty(tvFeeType.getText().toString())) {
          ArmsUtils.makeText(getActivity(), "请选择结算费用");
          return;
        }
        if (StringUtils.isEmpty(tvCarType.getText().toString())) {
          ArmsUtils.makeText(getActivity(), "请选择车辆类型");
          return;
        }

        if (confirmDialog != null) {
          confirmDialog.show();
        } else {
          confirmDialog = new MaterialDialog.Builder(this)
              .customView(R.layout.dialog_get_order, false)
              .backgroundColor(AlertDialog.THEME_HOLO_LIGHT)
              .show();
          android.view.View customView = confirmDialog.getCustomView();
          TextView textView = customView.findViewById(R.id.tvContent);
          RelativeLayout layCancel = customView.findViewById(R.id.layCancel);
          RelativeLayout layConfirm = customView.findViewById(R.id.layConfirm);
          textView.setText(rent ? "你确认平台租车吗?" : "你确认平台派车吗?");
          layCancel.setOnClickListener(view1 -> confirmDialog.dismiss());
          layConfirm.setOnClickListener(view1 -> {
            confirmDialog.dismiss();

            mPresenter.submitRentCar(getIntent().getStringExtra("todoId"),
                AppUtil.getDictType(tvCarType.getText().toString(), carTypeDicts),
                orderCarNo,
                carCenterList.get(carCenters.indexOf(tvCarCenter.getText().toString())).getOrgId()
                    .toString(),
                StringUtils.isEmpty(tvFeeType.getText().toString()) || "无"
                    .equals(tvFeeType.getText().toString()) ? ""
                    : feeTypeList.get(feeTypes.indexOf(tvFeeType.getText().toString())).getFeeId()
                        .toString());

          });


        }
        break;
      case R.id.layChooseCarType:
        if (chooseCarTypeDialog == null) {
          chooseCarTypeDialog = new MaterialDialog.Builder(this)
              .title("车辆类型选择")
              .items(carTypes)
              .itemsCallbackSingleChoice(0,
                  new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, android.view.View itemView,
                        int which, CharSequence text) {
                      tvCarType.setText(text);
                      return false;
                    }


                  })
              .show();

        } else {
          chooseCarTypeDialog.setSelectedIndex(StringUtils.isEmpty(tvCarType.getText()) ? 0
              : carTypes.indexOf(tvCarType.getText().toString()));
          chooseCarTypeDialog.show();

        }
        break;

      default:
    }
  }

  @Override
  public void initFeeTypes(List<FeeType> data) {
    feeTypeList.clear();
    feeTypes.clear();
    FeeType feeType = new FeeType("无", null);
    if (!rent) {
      feeTypeList.add(feeType);
    }

    feeTypeList.addAll(data);
    for (FeeType datum : feeTypeList) {
      feeTypes.add(datum.getFeeName());


    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (materialDialog != null) {
      materialDialog.dismiss();
      materialDialog = null;
    }
    if (confirmDialog != null) {
      confirmDialog.dismiss();
      confirmDialog = null;

    }

  }

  @Subscriber(tag = EventBusTags.RENT_CAR_SUCCESS, mode = ThreadMode.MAIN)
  public void rentCarSuccess(String str) {
    finish();

  }
}
