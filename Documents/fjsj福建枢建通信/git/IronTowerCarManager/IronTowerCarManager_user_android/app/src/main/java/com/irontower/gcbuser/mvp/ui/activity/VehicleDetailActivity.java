package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;
import butterknife.BindView;
import com.blankj.utilcode.util.StringUtils;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.di.component.DaggerVehicleDetailComponent;
import com.irontower.gcbuser.di.module.VehicleDetailModule;
import com.irontower.gcbuser.mvp.contract.VehicleDetailContract.View;
import com.irontower.gcbuser.mvp.model.entity.Car;
import com.irontower.gcbuser.mvp.presenter.VehicleDetailPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;


public class VehicleDetailActivity extends BaseActivity<VehicleDetailPresenter> implements
    View {

  @BindView(R.id.tvCarNo)
  TextView tvCarNo;
  @BindView(R.id.tvCarType)
  TextView tvCarType;
  @BindView(R.id.tvCnName)
  TextView tvCnName;
  @BindView(R.id.tvModelName)
  TextView tvModelName;
  @BindView(R.id.tvSeatNum)
  TextView tvSeatNum;
  @BindView(R.id.tvOutputVolume)
  TextView tvOutputVolume;
  @BindView(R.id.tvCarColor)
  TextView tvCarColor;
  @BindView(R.id.tvPurpose)
  TextView tvPurpose;
  @BindView(R.id.tvState)
  TextView tvState;
  @BindView(R.id.tvOrgName)
  TextView tvOrgName;
  @BindView(R.id.tvRegisterDate)
  TextView tvRegisterDate;


  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerVehicleDetailComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .vehicleDetailModule(new VehicleDetailModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_vehicle_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    Car car = getIntent().getParcelableExtra("car");
    tvCarNo.setText(car.getCarNo());
    if (!StringUtils.isEmpty(car.getCarType())) {
      tvCarType.setText(AppUtil.getDictName(car.getCarType(), AppUtil.getCarTypes()));
    }
    tvCnName.setText(car.getCnName());
    tvModelName.setText(car.getModelName());
    if (!StringUtils.isEmpty(car.getSeatNum())) {
      tvSeatNum.setText(car.getSeatNum() + "座");
    }
    if (!StringUtils.isEmpty(car.getOutputVolume())) {
      tvOutputVolume.setText(car.getOutputVolume() + "L");
    }

    tvCarColor.setText(car.getCarColor());
    tvState.setText(car.getCarUseState() == 1 ? "空闲" : "忙碌");
    tvOrgName.setText(car.getOrgName());
    tvRegisterDate.setText(car.getRegisterDate());
    tvPurpose.setText(car.getCarUseDetail());
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


}
