package com.irontower.gcbdriver.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.di.component.DaggerChartStatisticsComponent;
import com.irontower.gcbdriver.di.module.ChartStatisticsModule;
import com.irontower.gcbdriver.mvp.contract.ChartStatisticsContract.View;
import com.irontower.gcbdriver.mvp.presenter.ChartStatisticsPresenter;
import com.irontower.gcbdriver.mvp.ui.fragment.DriverNumberFragment;
import com.irontower.gcbdriver.mvp.ui.fragment.KilometerNumberFragment;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;


public class ChartStatisticsActivity extends BaseActivity<ChartStatisticsPresenter> implements
    View {

  @BindView(R.id.toolbar_back)
  RelativeLayout toolbarBack;
  private FragmentManager fragmentManager;
  private int index = 1;
  Fragment driveFragment, kilometerFragment;
  @BindView(R.id.layDrive)
  RelativeLayout layDrive;
  @BindView(R.id.layKilometer)
  RelativeLayout layKilometer;
  @BindView(R.id.tvDrive)
  TextView tvDrive;
  @BindView(R.id.tvKilometer)
  TextView tvKilometer;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerChartStatisticsComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .chartStatisticsModule(new ChartStatisticsModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_chart_statistics; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    fragmentManager = getSupportFragmentManager();
    setTabSelection(1);
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

  private void setTabSelection(int i) {
    FragmentTransaction transaction = fragmentManager.beginTransaction();

    // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
    reset(transaction);
    switch (i) {
      case 1:
        if (driveFragment == null) {
          driveFragment = DriverNumberFragment.newInstance();
          transaction.add(R.id.framelayout, driveFragment);
        }
        tvDrive.setTextColor(ArmsUtils.getColor(this, R.color.mainColor));
        tvKilometer.setTextColor(ArmsUtils.getColor(this, R.color.white));
        layDrive.setBackgroundResource(R.mipmap.tab_left_focus);
        layKilometer.setBackgroundResource(R.mipmap.tab_right_unfocus);
        transaction.show(driveFragment);

        break;
      case 2:
        if (kilometerFragment == null) {
          kilometerFragment = KilometerNumberFragment.newInstance();
          transaction.add(R.id.framelayout, kilometerFragment);
        }
        tvDrive.setTextColor(ArmsUtils.getColor(this, R.color.white));
        tvKilometer.setTextColor(ArmsUtils.getColor(this, R.color.mainColor));
        layDrive.setBackgroundResource(R.mipmap.tab_left_unfocus);
        layKilometer.setBackgroundResource(R.mipmap.tab_right_focus);
        transaction.show(kilometerFragment);
        break;
      default:

        break;
    }
    transaction.commit();
  }

  private void reset(FragmentTransaction transaction) {
    if (kilometerFragment != null) {
      transaction.hide(kilometerFragment);
    }
    if (driveFragment != null) {
      transaction.hide(driveFragment);
    }


  }


  @OnClick({R.id.layDrive, R.id.layKilometer})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layDrive:
        if (index == 1) {
          return;
        }
        setTabSelection(1);
        index = 1;
        break;
      case R.id.layKilometer:
        if (index == 2) {
          return;
        }
        setTabSelection(2);
        index = 2;
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
}
