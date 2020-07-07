package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.blankj.utilcode.util.StringUtils;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.di.component.DaggerForgetPasswordComponent;
import com.irontower.gcbuser.di.module.ForgetPasswordModule;
import com.irontower.gcbuser.mvp.contract.ForgetPasswordContract.View;
import com.irontower.gcbuser.mvp.presenter.ForgetPasswordPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;


public class ForgetPasswordActivity extends BaseActivity<ForgetPasswordPresenter> implements
    View {


  @BindView(R.id.layGetCode)
  RelativeLayout layGetCode;
  @BindView(R.id.tvCode)
  TextView tvCode;
  @BindView(R.id.etPhone)
  AppCompatEditText etPhone;
  @BindView(R.id.etSms)
  AppCompatEditText etSms;
  @BindView(R.id.etPwd)
  AppCompatEditText etPwd;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerForgetPasswordComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .forgetPasswordModule(new ForgetPasswordModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_forget_password; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {

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


  @OnClick({R.id.layGetCode, R.id.layConfirm})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {

      case R.id.layGetCode:
        if (validateGetCode()) {
          mPresenter.getCode(etPhone.getText().toString(), tvCode);
        }

        break;
      case R.id.layConfirm:

        if (validateResetPwd()) {
          mPresenter.resetPwd(etPhone.getText().toString(), etSms.getText().toString(),
              etPwd.getText().toString());
        }
        break;
      default:
    }
  }

  @Override
  public Activity getActivity() {
    return this;
  }

  @Override
  public void disable() {
    layGetCode.setEnabled(false);

    layGetCode.setBackground(ArmsUtils.getDrawablebyResource(getActivity(), R.mipmap.btn_wait));
  }

  @Override
  public void able() {
    layGetCode.setEnabled(true);
    layGetCode.setBackground(ArmsUtils.getDrawablebyResource(getActivity(), R.mipmap.btn_getcode));
    tvCode.setText("获取验证码");

  }

  public boolean validateGetCode() {
    if (StringUtils.isEmpty(etPhone.getText().toString())) {
      ArmsUtils.makeText(getActivity(), "请输入用户名");
      return false;
    }

    return true;
  }

  public boolean validateResetPwd() {
    if (StringUtils.isEmpty(etPhone.getText().toString())) {
      ArmsUtils.makeText(getActivity(), "请输入用户名");
      return false;
    }

    if (etSms.getText().length() != 6) {
      ArmsUtils.makeText(getActivity(), "请输入6位验证码");
      return false;
    }
    if (etPhone.getText().length() == 0) {
      ArmsUtils.makeText(getActivity(), "请输入新密码");
      return false;
    }
    return true;
  }

}
