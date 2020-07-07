package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.di.component.DaggerWelcomeComponent;
import com.irontower.gcbuser.di.module.WelcomeModule;
import com.irontower.gcbuser.mvp.contract.WelcomeContract;
import com.irontower.gcbuser.mvp.presenter.WelcomePresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;

/**
 * Created by jianghaifeng on 2017/11/30.
 */

public class WelcomeActivity extends BaseActivity<WelcomePresenter> implements
    WelcomeContract.View {


  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerWelcomeComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .welcomeModule(new WelcomeModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_welcome;
  }


  @Override
  public void initData(Bundle savedInstanceState) {
    mPresenter.jump();


  }


  @Override
  public boolean useFragment() {
    return false;
  }

  @Override
  public void showLoading() {

  }

  @Override
  public void hideLoading() {

  }

  @Override
  public void showMessage(String message) {
    checkNotNull(message);


  }

  @Override
  public void launchActivity(Intent intent) {
    startActivity(intent);
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
  public Resources getResources() {
    Resources res = super.getResources();
    Configuration config = new Configuration();
    config.setToDefaults();
    res.updateConfiguration(config, res.getDisplayMetrics());
    return res;

  }
}
