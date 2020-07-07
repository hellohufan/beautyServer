package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import butterknife.BindView;
import butterknife.OnClick;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.di.component.DaggerRemarkComponent;
import com.irontower.gcbuser.di.module.RemarkModule;
import com.irontower.gcbuser.mvp.contract.RemarkContract.View;
import com.irontower.gcbuser.mvp.presenter.RemarkPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import org.simple.eventbus.EventBus;


public class RemarkActivity extends BaseActivity<RemarkPresenter> implements View {


  @BindView(R.id.etRemark)
  AppCompatEditText etRemark;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerRemarkComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .remarkModule(new RemarkModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_remark; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    etRemark.setText(getIntent().getStringExtra("remark"));
    etRemark.setSelection(getIntent().getStringExtra("remark").length());
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


  @OnClick({R.id.laySubmit})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {

      case R.id.laySubmit:

        EventBus.getDefault().post(etRemark.getText().toString(), EventBusTags.REMARK);
        finish();
        break;
      default:
    }
  }
}
