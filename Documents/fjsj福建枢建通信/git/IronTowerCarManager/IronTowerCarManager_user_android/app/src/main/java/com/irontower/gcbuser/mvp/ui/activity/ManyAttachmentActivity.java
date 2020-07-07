package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.View;
import butterknife.BindView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.di.component.DaggerManyAttachmentComponent;
import com.irontower.gcbuser.di.module.ManyAttachmentModule;
import com.irontower.gcbuser.mvp.contract.ManyAttachmentContract;
import com.irontower.gcbuser.mvp.model.api.Api;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import com.irontower.gcbuser.mvp.presenter.ManyAttachmentPresenter;
import com.irontower.gcbuser.mvp.ui.adapter.ManyAttachAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.util.List;
import javax.inject.Inject;


public class ManyAttachmentActivity extends BaseActivity<ManyAttachmentPresenter> implements
    ManyAttachmentContract.View {

  @Inject
  LayoutManager mLayoutManager;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @Inject
  List<Attach> attachList;
  @Inject
  ManyAttachAdapter adapter;
  @Inject
  MaterialDialog materialDialog;

  @Override
  public void setupActivityComponent(@NonNull AppComponent appComponent) {
    DaggerManyAttachmentComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .manyAttachmentModule(new ManyAttachmentModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(@Nullable Bundle savedInstanceState) {
    return R.layout.activity_many_attachment; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(@Nullable Bundle savedInstanceState) {
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setHasFixedSize(true);
    adapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Attach attach = (Attach) adapter.getData().get(position);
        mPresenter
            .getFile(Api.APP_DOMAIN.substring(0, Api.APP_DOMAIN.length() - 1) + attach.getPath(),
                attach.getAttachName());
      }
    });
    recyclerView.setAdapter(adapter);
    attachList.addAll(getIntent().getParcelableArrayListExtra("attachList"));
    adapter.notifyDataSetChanged();

  }

  @Override
  public void showLoading() {
    if (materialDialog != null) {
      materialDialog.show();
    }

  }

  @Override
  public void hideLoading() {
    if (materialDialog != null) {
      materialDialog.dismiss();
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
}
