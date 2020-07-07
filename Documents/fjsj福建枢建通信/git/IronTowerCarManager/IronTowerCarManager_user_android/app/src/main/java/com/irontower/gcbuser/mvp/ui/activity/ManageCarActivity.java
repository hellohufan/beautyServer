package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import butterknife.BindView;
import butterknife.OnClick;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.di.component.DaggerManageCarComponent;
import com.irontower.gcbuser.di.module.ManageCarModule;
import com.irontower.gcbuser.mvp.contract.ManageCarContract;
import com.irontower.gcbuser.mvp.model.entity.Car;
import com.irontower.gcbuser.mvp.presenter.ManageCarPresenter;
import com.irontower.gcbuser.mvp.ui.adapter.ManageCarAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import javax.inject.Inject;


public class ManageCarActivity extends BaseActivity<ManageCarPresenter> implements
    ManageCarContract.View, SwipeRefreshLayout.OnRefreshListener {

  @BindView(R.id.editText)
  AppCompatEditText editText;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;
  @Inject
  ManageCarAdapter adapter;
  @Inject
  LayoutManager mLayoutManager;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerManageCarComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .manageCarModule(new ManageCarModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_manage_car; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
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

        mPresenter.refresh(false, editText.getText().toString());


      }
    }, recyclerView);
    adapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, android.view.View view, int position) {
        Car car = (Car) adapter.getData().get(position);
        Intent intent = new Intent();
        intent.putExtra("car", car);
        intent.setClass(getActivity(), VehicleDetailActivity.class);
        startActivity(intent);
      }
    });

    adapter.setEmptyView(LayoutInflater.from(getActivity())
        .inflate(R.layout.empty_my_task, (ViewGroup) recyclerView.getParent(), false));
    recyclerView.setAdapter(adapter);
    onRefresh();
    editText.setOnEditorActionListener(new OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        mPresenter.refresh(true, editText.getText().toString());
        return false;
      }
    });


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
    mPresenter.refresh(true, editText.getText().toString());
  }


  @OnClick(R.id.ivSearch)
  public void onViewClicked() {

    mPresenter.refresh(true, editText.getText().toString());
  }

}
