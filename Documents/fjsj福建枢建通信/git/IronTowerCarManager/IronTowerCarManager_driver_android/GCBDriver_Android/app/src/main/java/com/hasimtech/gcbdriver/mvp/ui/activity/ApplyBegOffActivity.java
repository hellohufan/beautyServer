package com.irontower.gcbdriver.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.OnClick;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.app.EventBusTags;
import com.irontower.gcbdriver.di.component.DaggerApplyBegOffComponent;
import com.irontower.gcbdriver.di.module.ApplyBegOffModule;
import com.irontower.gcbdriver.mvp.contract.ApplyBegOffContract;
import com.irontower.gcbdriver.mvp.presenter.ApplyBegOffPresenter;
import com.irontower.gcbdriver.mvp.ui.adapter.ApplyBegOffAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class ApplyBegOffActivity extends BaseActivity<ApplyBegOffPresenter> implements
    ApplyBegOffContract.View, SwipeRefreshLayout.OnRefreshListener {

  @Inject
  ApplyBegOffAdapter adapter;
  @Inject
  RecyclerView.LayoutManager mLayoutManager;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;


  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerApplyBegOffComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .applyBegOffModule(new ApplyBegOffModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_apply_beg_off; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
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

    adapter.setEmptyView(LayoutInflater.from(getActivity())
        .inflate(R.layout.empty_apply_beg_off, (ViewGroup) recyclerView.getParent(), false));

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


  @OnClick(R.id.layBegOff)
  public void onViewClicked() {

    ArmsUtils.startActivity(getActivity(), ApplyBegOffDetailActivity.class);


  }

  @Subscriber(tag = EventBusTags.REFRESHBEGOFF, mode = ThreadMode.MAIN)
  public void changeImage(String refresh) {
    mPresenter.refresh(true);

  }


}
