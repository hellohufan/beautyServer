package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.blankj.utilcode.util.StringUtils;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.di.component.DaggerDriverDetailComponent;
import com.irontower.gcbuser.di.module.DriverDetailModule;
import com.irontower.gcbuser.mvp.contract.DriverDetailContract.View;
import com.irontower.gcbuser.mvp.model.entity.Driver;
import com.irontower.gcbuser.mvp.presenter.DriverDetailPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import javax.inject.Inject;


public class DriverDetailActivity extends BaseActivity<DriverDetailPresenter> implements
    View {

  @Inject
  RxPermissions permissions;
  @BindView(R.id.tvUserName)
  TextView tvUserName;
  @BindView(R.id.tvSex)
  TextView tvSex;
  @BindView(R.id.tvNativePlace)
  TextView tvNativePlace;
  @BindView(R.id.tvPhoneNo)
  TextView tvPhoneNo;
  @BindView(R.id.tvLicenseType)
  TextView tvLicenseType;
  @BindView(R.id.tvDeptName)
  TextView tvDeptName;
  @BindView(R.id.tvLaborRelation)
  TextView tvLaborRelation;
  @BindView(R.id.tvRemark)
  TextView tvRemark;
  private Driver driver;


  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerDriverDetailComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .driverDetailModule(new DriverDetailModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_driver_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0

  }

  @Override
  public void initData(Bundle savedInstanceState) {
    driver = getIntent().getParcelableExtra("driver");
    tvUserName.setText(driver.getUserName());
    tvSex.setText(driver.getSex());
    tvPhoneNo.setText(driver.getPhoneNo());
    tvLicenseType.setText(driver.getLicenseType());
    tvDeptName.setText(driver.getDeptName());
    tvLaborRelation.setText(driver.getLaborRelation());
    tvRemark.setText(StringUtils.isEmpty(driver.getRemark()) ? "无" : driver.getRemark());
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

  @Override
  public Activity getActivity() {
    return this;
  }

  @Override
  public RxPermissions getRxPermissions() {
    return permissions;
  }


  @OnClick(R.id.layCall)
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layCall:
        mPresenter.call(driver.getPhoneNo());
        break;
      default:

    }
  }


}
