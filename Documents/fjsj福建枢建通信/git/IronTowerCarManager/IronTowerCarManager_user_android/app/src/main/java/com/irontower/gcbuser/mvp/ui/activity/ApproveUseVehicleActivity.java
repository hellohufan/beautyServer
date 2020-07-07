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
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.di.component.DaggerApproveUseVehicleComponent;
import com.irontower.gcbuser.di.module.ApproveUseVehicleModule;
import com.irontower.gcbuser.mvp.contract.ApproveUseVehicleContract.View;
import com.irontower.gcbuser.mvp.model.entity.ApproveOrder;
import com.irontower.gcbuser.mvp.presenter.ApproveUseVehiclePresenter;
import com.irontower.gcbuser.mvp.ui.adapter.ApproveUseVehicleAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class ApproveUseVehicleActivity extends BaseActivity<ApproveUseVehiclePresenter> implements
    View, SwipeRefreshLayout.OnRefreshListener {


  @BindView(R.id.editText)
  AppCompatEditText editText;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;
  @Inject
  ApproveUseVehicleAdapter adapter;
  @Inject
  LayoutManager mLayoutManager;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerApproveUseVehicleComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .approveUseVehicleModule(new ApproveUseVehicleModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_approve_use_vehicle; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
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

        mPresenter.getData(editText.getText().toString(), false);


      }
    }, recyclerView);
    adapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, android.view.View view, int position) {
        ApproveOrder approveOrder = (ApproveOrder) adapter.getData().get(position);
        Intent intent = new Intent();
        intent.putExtra("todoId", approveOrder.getTodoId());
        intent.setClass(getActivity(), ApproveDetailActivity.class);
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

        mPresenter.getData(editText.getText().toString(), true);
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


  @OnClick({R.id.layHistory})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {

      case R.id.layHistory:
        Intent intent = new Intent();
        intent.setClass(getActivity(), ApproveHistoryActivity.class);
        startActivity(intent);

        break;
      default:
    }
  }

  @Override
  public void onRefresh() {
    mPresenter.getData(editText.getText().toString(), true);
  }


  @Subscriber(tag = EventBusTags.APPROVE_ORDER, mode = ThreadMode.MAIN)
  public void startCity(String str) {

    mPresenter.getData(editText.getText().toString(), true);
  }

  @OnClick(R.id.ivSearch)
  public void onViewClicked() {

    mPresenter.getData(editText.getText().toString(), true);
  }
}
