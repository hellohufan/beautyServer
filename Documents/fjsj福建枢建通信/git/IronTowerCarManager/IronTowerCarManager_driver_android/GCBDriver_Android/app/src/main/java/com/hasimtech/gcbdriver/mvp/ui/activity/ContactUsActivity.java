package com.irontower.gcbdriver.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.di.component.DaggerContactUsComponent;
import com.irontower.gcbdriver.di.module.ContactUsModule;
import com.irontower.gcbdriver.mvp.contract.ContactUsContract;
import com.irontower.gcbdriver.mvp.presenter.ContactUsPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;


public class ContactUsActivity extends BaseActivity<ContactUsPresenter> implements
    ContactUsContract.View {


  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerContactUsComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .contactUsModule(new ContactUsModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_contact_us; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {

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
  public Resources getResources() {
    Resources res = super.getResources();
    Configuration config = new Configuration();
    config.setToDefaults();
    res.updateConfiguration(config, res.getDisplayMetrics());
    return res;

  }
}
