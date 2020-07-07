package com.irontower.gcbdriver.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import butterknife.BindView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemChildClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.app.EventBusTags;
import com.irontower.gcbdriver.di.component.DaggerOrderRecordComponent;
import com.irontower.gcbdriver.di.module.OrderRecordModule;
import com.irontower.gcbdriver.mvp.contract.OrderRecordContract.View;
import com.irontower.gcbdriver.mvp.model.entity.Order;
import com.irontower.gcbdriver.mvp.presenter.OrderRecordPresenter;
import com.irontower.gcbdriver.mvp.ui.adapter.OrderRecordAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class OrderRecordActivity extends BaseActivity<OrderRecordPresenter> implements
    View, OnRefreshListener {


  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;
  @Inject
  LayoutManager mLayoutManager;
  @Inject
  OrderRecordAdapter adapter;
  @Inject
  RxPermissions rxPermissions;

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
    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    swipeRefreshLayout.setOnRefreshListener(this);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setHasFixedSize(true);
    adapter.setOnLoadMoreListener(new RequestLoadMoreListener() {
      @Override
      public void onLoadMoreRequested() {
        mPresenter.refresh(false);


      }
    }, recyclerView);
    adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
      @Override
      public void onItemChildClick(BaseQuickAdapter adapter, android.view.View view, int position) {
        Order order = (Order) adapter.getData().get(position);
        if (view.getId() == R.id.layCall) {
          mPresenter.call(order.getPassengerTel());
        }
      }
    });

    adapter.setEmptyView(LayoutInflater
        .from(this).inflate(R.layout.empty_no_data, (ViewGroup) recyclerView.getParent(), false));
    adapter.setOnItemClickListener((adapter, view, position) -> {
      Order order = (Order) adapter.getData().get(position);
      Intent intent = new Intent();
      intent.setClass(getActivity(), OrderRecordDetailActivity.class);
      intent.putExtra("orderCarNo", order.getOrderCarNo());
      startActivity(intent);

    });
    recyclerView.setAdapter(adapter);
    onRefresh();

  }


  @Override
  public void showLoading() {
    swipeRefreshLayout.setRefreshing(true);


  }

  @Override
  public void hideLoading() {
    swipeRefreshLayout.setRefreshing(false);

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
  public void onRefresh() {
    mPresenter.refresh(true);
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
  public RxPermissions getRxpermissions() {
    return rxPermissions;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    rxPermissions = null;
  }

  @Subscriber(tag = EventBusTags.SUBMITREPORT, mode = ThreadMode.MAIN)
  public void submitReport(String str) {
    mPresenter.refresh(true);
  }

}
