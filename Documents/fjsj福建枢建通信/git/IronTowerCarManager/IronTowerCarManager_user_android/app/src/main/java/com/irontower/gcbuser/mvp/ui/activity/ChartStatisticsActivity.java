package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.di.component.DaggerChartStatisticsComponent;
import com.irontower.gcbuser.di.module.ChartStatisticsModule;
import com.irontower.gcbuser.mvp.contract.ChartStatisticsContract;
import com.irontower.gcbuser.mvp.presenter.ChartStatisticsPresenter;
import com.irontower.gcbuser.mvp.ui.fragment.UseCarMoneyFragment;
import com.irontower.gcbuser.mvp.ui.fragment.UserCarTimeFragment;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;


public class ChartStatisticsActivity extends BaseActivity<ChartStatisticsPresenter> implements
    ChartStatisticsContract.View, SwipeRefreshLayout.OnRefreshListener {

  @BindView(R.id.toolbar_back)
  RelativeLayout toolbarBack;
  private FragmentManager fragmentManager;
  private int index = 1;
  Fragment useCarTimeFragment, useCarMoneyFragment;
  @BindView(R.id.layUserCarTime)
  RelativeLayout layUserCarTime;
  @BindView(R.id.layUseCarMoney)
  RelativeLayout layUseCarMoney;
  @BindView(R.id.tvUserCarTime)
  TextView tvUserCarTime;
  @BindView(R.id.tvUseCarMoney)
  TextView tvUseCarMoney;

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
        if (useCarTimeFragment == null) {
          useCarTimeFragment = UserCarTimeFragment.newInstance();
          transaction.add(R.id.framelayout, useCarTimeFragment);
        }
        tvUserCarTime.setTextColor(ArmsUtils.getColor(this, R.color.mainColor));
        tvUseCarMoney.setTextColor(ArmsUtils.getColor(this, R.color.white));
        layUserCarTime.setBackgroundResource(R.mipmap.tab_left_focus);
        layUseCarMoney.setBackgroundResource(R.mipmap.tab_right_unfocus);
        transaction.show(useCarTimeFragment);

        break;
      case 2:
        if (useCarMoneyFragment == null) {
          useCarMoneyFragment = UseCarMoneyFragment.newInstance();
          transaction.add(R.id.framelayout, useCarMoneyFragment);
        }
        tvUserCarTime.setTextColor(ArmsUtils.getColor(this, R.color.white));
        tvUseCarMoney.setTextColor(ArmsUtils.getColor(this, R.color.mainColor));
        layUserCarTime.setBackgroundResource(R.mipmap.tab_left_unfocus);
        layUseCarMoney.setBackgroundResource(R.mipmap.tab_right_focus);
        transaction.show(useCarMoneyFragment);
        break;
      default:

        break;
    }
    transaction.commit();
  }

  private void reset(FragmentTransaction transaction) {
    if (useCarMoneyFragment != null) {
      transaction.hide(useCarMoneyFragment);
    }
    if (useCarTimeFragment != null) {
      transaction.hide(useCarTimeFragment);
    }


  }


  @OnClick({R.id.layUserCarTime, R.id.layUseCarMoney})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layUserCarTime:
        if (index == 1) {
          return;
        }
        setTabSelection(1);
        index = 1;
        break;
      case R.id.layUseCarMoney:
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

  @Override
  public void onRefresh() {

  }

  @Override
  public Activity getActivity() {
    return null;
  }
}
