package com.irontower.gcbdriver.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.OnClick;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.app.EventBusTags;
import com.irontower.gcbdriver.di.component.DaggerApplyGoOutComponent;
import com.irontower.gcbdriver.di.module.ApplyGoOutModule;
import com.irontower.gcbdriver.mvp.contract.ApplyGoOutContract;
import com.irontower.gcbdriver.mvp.model.entity.BegGoOut;
import com.irontower.gcbdriver.mvp.presenter.ApplyGoOutPresenter;
import com.irontower.gcbdriver.mvp.ui.adapter.ApplyGoOutAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class ApplyGoOutActivity extends BaseActivity<ApplyGoOutPresenter> implements
    ApplyGoOutContract.View, OnRefreshListener {

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;
  @Inject
  ApplyGoOutAdapter adapter;
  @Inject
  LayoutManager mLayoutManager;


  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerApplyGoOutComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .applyGoOutModule(new ApplyGoOutModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_apply_go_out; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    swipeRefreshLayout.setOnRefreshListener(this);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setHasFixedSize(true);
    adapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BegGoOut begGoOut = (BegGoOut) adapter.getData().get(position);
        Intent intent = new Intent();
        intent.putExtra("begGoOut", begGoOut);
        intent.setClass(getActivity(), BegGoOutDetailActivity.class);
        startActivity(intent);
      }
    });
    adapter.setOnLoadMoreListener(new RequestLoadMoreListener() {
      @Override
      public void onLoadMoreRequested() {
        mPresenter.refresh(false);


      }
    }, recyclerView);

    adapter.setEmptyView(LayoutInflater.from(getActivity())
        .inflate(R.layout.empty_no_data, (ViewGroup) recyclerView.getParent(), false));
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


  @OnClick(R.id.layApply)
  public void onViewClicked() {
    ArmsUtils.startActivity(getActivity(), ApplyGoOutDetailActivity.class);
  }

  @Subscriber(tag = EventBusTags.APPLY_GO_OUT_SUCCESS, mode = ThreadMode.MAIN)
  public void applyGoOutSuccess(String str) {
    mPresenter.refresh(true);

  }

  @Subscriber(tag = EventBusTags.CHANGE_GO_OUT_SUCCESS, mode = ThreadMode.MAIN)
  public void changeUserName(String str) {
    mPresenter.refresh(true);
  }
}
