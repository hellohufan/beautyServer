package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import butterknife.BindView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.di.component.DaggerMessageDetailComponent;
import com.irontower.gcbuser.di.module.MessageDetailModule;
import com.irontower.gcbuser.mvp.contract.MessageDetailContract.View;
import com.irontower.gcbuser.mvp.model.entity.Message;
import com.irontower.gcbuser.mvp.presenter.MessageDetailPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;


public class MessageDetailActivity extends BaseActivity<MessageDetailPresenter> implements
    View {


  @BindView(R.id.tvTitle)
  TextView tvTitle;

  @BindView(R.id.tvTime)
  TextView tvTime;
  @BindView(R.id.tvContent)
  TextView tvContent;
  @BindView(R.id.toolBar)
  Toolbar toolBar;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerMessageDetailComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .messageDetailModule(new MessageDetailModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_message_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    tvContent.setMovementMethod(new ScrollingMovementMethod());
    mPresenter.getDetail(getIntent().getStringExtra("eid"));
  }


  @Override
  public void showLoading() {

  }

  @Override
  public void hideLoading() {

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
  public void init(Message message) {
    tvTitle.setText(message.getTitle());
    tvContent.setText(Html.fromHtml(message.getContent()));
    tvTime.setText(message.getPublishTime());
  }

}
