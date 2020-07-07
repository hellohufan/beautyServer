package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import butterknife.BindView;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.di.component.DaggerGoOutRecordComponent;
import com.irontower.gcbuser.di.module.GoOutRecordModule;
import com.irontower.gcbuser.mvp.contract.GoOutRecordContract;
import com.irontower.gcbuser.mvp.presenter.GoOutRecordPresenter;
import com.irontower.gcbuser.mvp.ui.adapter.GoOutRecordAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import javax.inject.Inject;


public class GoOutRecordActivity extends BaseActivity<GoOutRecordPresenter> implements
    GoOutRecordContract.View, SwipeRefreshLayout.OnRefreshListener {

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;
  @Inject
  GoOutRecordAdapter adapter;
  @Inject
  LayoutManager mLayoutManager;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerGoOutRecordComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .goOutRecordModule(new GoOutRecordModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_go_out_record; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
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
        mPresenter.getData(false);


      }
    }, recyclerView);

    adapter.setEmptyView(LayoutInflater.from(getActivity())
        .inflate(R.layout.empty_my_task, (ViewGroup) recyclerView.getParent(), false));
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
    mPresenter.getData(true);
  }

}
