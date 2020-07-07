package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemChildClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.di.component.DaggerApproveGoOutComponent;
import com.irontower.gcbuser.di.module.ApproveGoOutModule;
import com.irontower.gcbuser.mvp.contract.ApproveGoOutContract;
import com.irontower.gcbuser.mvp.model.entity.BegGoOut;
import com.irontower.gcbuser.mvp.presenter.ApproveGoOutPresenter;
import com.irontower.gcbuser.mvp.ui.adapter.ApproveGoOutAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import javax.inject.Inject;


public class ApproveGoOutActivity extends BaseActivity<ApproveGoOutPresenter> implements
    ApproveGoOutContract.View, SwipeRefreshLayout.OnRefreshListener {

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;
  @Inject
  ApproveGoOutAdapter adapter;
  @Inject
  LayoutManager mLayoutManager;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerApproveGoOutComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .approveGoOutModule(new ApproveGoOutModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_approve_go_out; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    swipeRefreshLayout.setOnRefreshListener(this);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setHasFixedSize(true);
    adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
      @Override
      public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        BegGoOut begGoOut = (BegGoOut) adapter.getData().get(position);
        switch (view.getId()) {

          case R.id.tvAgree:

            MaterialDialog agreeDialog = new Builder(getActivity())
                .customView(R.layout.dialog_approve_choose, false)
                .backgroundColor(AlertDialog.THEME_HOLO_LIGHT)
                .show();
            View customView = agreeDialog.getCustomView();
            TextView textView = customView.findViewById(R.id.tvTitle);
            AppCompatEditText appCompatEditText = customView.findViewById(R.id.editText);
            appCompatEditText.setHint("请输入确定理由");
            appCompatEditText.setText("");
            textView.setText("审批同意");
            RelativeLayout layCancel = customView.findViewById(R.id.layCancel);
            RelativeLayout layConfirm = customView.findViewById(R.id.layConfirm);
            layCancel.setOnClickListener(view1 -> agreeDialog.dismiss());
            layConfirm.setOnClickListener(view1 -> {

              mPresenter
                  .approvalBegGoOut(begGoOut.getHid(), appCompatEditText.getText().toString(), "1");
              agreeDialog.dismiss();
            });

            break;
          case R.id.tvReject:
            MaterialDialog rejectDialog = new Builder(getActivity())
                .customView(R.layout.dialog_approve_choose, false)
                .backgroundColor(AlertDialog.THEME_HOLO_LIGHT)
                .show();
            View customView1 = rejectDialog.getCustomView();
            TextView textView1 = customView1.findViewById(R.id.tvTitle);
            textView1.setText("审批驳回");
            AppCompatEditText appCompatEditText1 = customView1.findViewById(R.id.editText);
            appCompatEditText1.setText("");

            RelativeLayout layCancel1 = customView1.findViewById(R.id.layCancel);
            RelativeLayout layConfirm1 = customView1.findViewById(R.id.layConfirm);
            layCancel1.setOnClickListener(view1 -> rejectDialog.dismiss());
            layConfirm1.setOnClickListener(view1 -> {
              if (StringUtils.isEmpty(appCompatEditText1.getText().toString())) {
                ArmsUtils.makeText(getActivity(), "请输入驳回理由");
                return;
              }
              mPresenter
                  .approvalBegGoOut(begGoOut.getHid(), appCompatEditText1.getText().toString(),
                      "2");
              rejectDialog.dismiss();
            });

            break;
          default:
        }

      }
    });

    adapter.setOnLoadMoreListener(new RequestLoadMoreListener() {
      @Override
      public void onLoadMoreRequested() {
        mPresenter.refresh(false);


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
    mPresenter.refresh(true);
  }


  @OnClick(R.id.layHistory)
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.layHistory:
        ArmsUtils.startActivity(getActivity(), ApproveGoOutHistoryActivity.class);
        break;
      default:
    }
  }
}
