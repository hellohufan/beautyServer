package com.irontower.gcbdriver.mvp.ui.fragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import butterknife.BindView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.app.EventBusTags;
import com.irontower.gcbdriver.di.component.DaggerWaitingOrderComponent;
import com.irontower.gcbdriver.di.module.WaitingOrderModule;
import com.irontower.gcbdriver.mvp.contract.WaitingOrderContract;
import com.irontower.gcbdriver.mvp.model.entity.Order;
import com.irontower.gcbdriver.mvp.presenter.WaitingOrderPresenter;
import com.irontower.gcbdriver.mvp.ui.activity.OrderDetailActivity;
import com.irontower.gcbdriver.mvp.ui.adapter.WaitingOrderAdapter;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class WaitingOrderFragment extends BaseFragment<WaitingOrderPresenter> implements
    WaitingOrderContract.View, OnRefreshListener {

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;
  @Inject
  LayoutManager mLayoutManager;
  @Inject
  WaitingOrderAdapter adapter;
  @Inject
  RxPermissions rxPermissions;
  private Context context;


  public static WaitingOrderFragment newInstance() {
    WaitingOrderFragment fragment = new WaitingOrderFragment();
    return fragment;
  }

  @Override
  public void setupFragmentComponent(AppComponent appComponent) {
    DaggerWaitingOrderComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .waitingOrderModule(new WaitingOrderModule(this))
        .build()
        .inject(this);
  }

  @Override
  public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_waiting_order, container, false);
    context = view.getContext();
    return view;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

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
    adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
      @Override
      public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Order order = (Order) adapter.getData().get(position);
        switch (view.getId()) {
          case R.id.layTakeOrder:
            MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_get_order, false)
                .backgroundColor(AlertDialog.THEME_HOLO_LIGHT)
                .show();
            View customView = dialog.getCustomView();
            RelativeLayout layCancel = customView.findViewById(R.id.layCancel);
            RelativeLayout layConfirm = customView.findViewById(R.id.layConfirm);
            layCancel.setOnClickListener(view1 -> dialog.dismiss());
            layConfirm.setOnClickListener(view1 -> {
              dialog.dismiss();
              mPresenter.takeOrder(order.getOrderCarNo());

            });

            break;
          case R.id.layCall:
            mPresenter.call(order.getPassengerTel());

          default:
            break;
        }
      }
    });
    adapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Order order = (Order) adapter.getData().get(position);
        Intent intent = new Intent();
        intent.putExtra("orderCarNo", order.getOrderCarNo());

        intent.setClass(view.getContext(), OrderDetailActivity.class);
        startActivity(intent);
      }
    });

    adapter.setEmptyView(LayoutInflater.from(context)
        .inflate(R.layout.empty_waiting_order, (ViewGroup) recyclerView.getParent(), false));
    recyclerView.setAdapter(adapter);
    onRefresh();


  }

  /**
   * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法, 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
   * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
   *
   * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onCreate还没执行 setData里却调用了presenter的方法时,是会报空的,因为dagger注入是在onCreated方法中执行的,然后才创建的presenter
   * 如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
   */

  @Override
  public void setData(Object data) {

  }


  @Override
  public void showLoading() {
    if (swipeRefreshLayout != null) {
      swipeRefreshLayout.setRefreshing(true);
    }


  }

  @Override
  public void hideLoading() {
    if (swipeRefreshLayout != null) {
      swipeRefreshLayout.setRefreshing(false);
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

  }

  @Override
  public void onRefresh() {
    mPresenter.refresh(true);
  }


  @Override
  public RxPermissions getRxpermissons() {
    return rxPermissions;
  }

  @Subscriber(tag = EventBusTags.TAKE_ORDER_SUCCESS, mode = ThreadMode.MAIN)
  public void takeOrderSuccess(String userName) {
    mPresenter.refresh(true);

  }

  @Override
  public Fragment getFragment() {
    return this;
  }

  @Override
  public void onResume() {
    super.onResume();
    MobclickAgent.onPageStart(
        "WaitingOrderFragment"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
  }

  @Override

  public void onPause() {
    super.onPause();
    MobclickAgent.onPageEnd(
        "WaitingOrderFragment"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
  }


}
