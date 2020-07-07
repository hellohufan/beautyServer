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
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.di.component.DaggerOrderRecordComponent;
import com.irontower.gcbuser.di.module.OrderRecordModule;
import com.irontower.gcbuser.mvp.contract.OrderRecordContract.View;
import com.irontower.gcbuser.mvp.presenter.OrderRecordPresenter;
import com.irontower.gcbuser.mvp.ui.fragment.FinishOrderFragment;
import com.irontower.gcbuser.mvp.ui.fragment.GoingOrderFragment;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;


public class OrderRecordActivity extends BaseActivity<OrderRecordPresenter> implements
    View {


  @BindView(R.id.tvGoingOrder)
  TextView tvGoingOrder;
  @BindView(R.id.layGoingOrder)
  RelativeLayout layGoingOrder;
  @BindView(R.id.tvFinishOrder)
  TextView tvFinishOrder;
  @BindView(R.id.layFinishOrder)
  RelativeLayout layFinishOrder;

  private FragmentManager fragmentManager;
  private int index = 1;


  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerOrderRecordComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .orderRecordModule(new OrderRecordModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_order_record; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    fragmentManager = getSupportFragmentManager();
    setTabSelection(1);

  }

  private void reset(FragmentTransaction transaction) {
    Fragment goingOrderFragment = getSupportFragmentManager()
        .findFragmentByTag("GoingOrderFragment");
    Fragment finishOrderFragment = getSupportFragmentManager()
        .findFragmentByTag("FinishOrderFragment");
    if (goingOrderFragment != null) {
      transaction.hide(goingOrderFragment);
    }
    if (finishOrderFragment != null) {
      transaction.hide(finishOrderFragment);
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
  public Resources getResources() {
    Resources res = super.getResources();
    Configuration config = new Configuration();
    config.setToDefaults();
    res.updateConfiguration(config, res.getDisplayMetrics());
    return res;

  }

  @Override
  public void showLoading() {

  }

  @Override
  public void hideLoading() {

  }

  private void setTabSelection(int i) {
    FragmentTransaction transaction = fragmentManager.beginTransaction();

    // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
    reset(transaction);
    switch (i) {
      case 1:
        Fragment goingOrderFragment = getSupportFragmentManager()
            .findFragmentByTag("GoingOrderFragment");
        if (goingOrderFragment == null) {
          goingOrderFragment = GoingOrderFragment.newInstance();
          transaction.add(R.id.framelayout, goingOrderFragment, "GoingOrderFragment");
        }
        tvGoingOrder.setTextColor(ArmsUtils.getColor(this, R.color.mainColor));
        tvFinishOrder.setTextColor(ArmsUtils.getColor(this, R.color.white));
        layGoingOrder.setBackgroundResource(R.mipmap.tab_left_focus);
        layFinishOrder.setBackgroundResource(R.mipmap.tab_right_unfocus);
        transaction.show(goingOrderFragment);

        break;
      case 2:
        Fragment finishOrderFragment = getSupportFragmentManager()
            .findFragmentByTag("FinishOrderFragment");
        if (finishOrderFragment == null) {
          finishOrderFragment = FinishOrderFragment.newInstance();
          transaction.add(R.id.framelayout, finishOrderFragment, "FinishOrderFragment");
        }
        tvGoingOrder.setTextColor(ArmsUtils.getColor(this, R.color.white));
        tvFinishOrder.setTextColor(ArmsUtils.getColor(this, R.color.mainColor));
        layGoingOrder.setBackgroundResource(R.mipmap.tab_left_unfocus);
        layFinishOrder.setBackgroundResource(R.mipmap.tab_right_focus);
        transaction.show(finishOrderFragment);
        break;
      default:

        break;
    }
    transaction.commit();
  }


  @OnClick({R.id.layGoingOrder, R.id.layFinishOrder})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layGoingOrder:
        if (index == 1) {
          return;
        }
        setTabSelection(1);
        index = 1;
        break;
      case R.id.layFinishOrder:
        if (index == 2) {
          return;
        }
        setTabSelection(2);
        index = 2;
        break;
      default:
    }
  }
}
