package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import butterknife.BindView;
import butterknife.OnClick;
import com.blankj.utilcode.util.StringUtils;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.di.component.DaggerModifyPasswordComponent;
import com.irontower.gcbuser.di.module.ModifyPasswordModule;
import com.irontower.gcbuser.mvp.contract.ModifyPasswordContract;
import com.irontower.gcbuser.mvp.presenter.ModifyPasswordPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;


public class ModifyPasswordActivity extends BaseActivity<ModifyPasswordPresenter> implements
    ModifyPasswordContract.View {


  @BindView(R.id.originalPWD)
  AppCompatEditText originalPWD;
  @BindView(R.id.newPWD)
  AppCompatEditText newPWD;
  @BindView(R.id.newPWDAgain)
  AppCompatEditText newPWDAgain;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerModifyPasswordComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .modifyPasswordModule(new ModifyPasswordModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_modify_password; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
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


  @OnClick(R.id.laySubmit)
  public void onViewClicked() {
    if (StringUtils.isEmpty(originalPWD.getText().toString())) {
      ArmsUtils.makeText(getActivity(), "原始密码不能为空");
      return;
    }
    if (StringUtils.isEmpty(newPWD.getText().toString())) {
      ArmsUtils.makeText(getActivity(), "新密码不能为空");
      return;
    }
    if (StringUtils.isEmpty(newPWDAgain.getText().toString())) {
      ArmsUtils.makeText(getActivity(), "新密码不能为空");
      return;
    }
    if (!StringUtils.equals(newPWD.getText().toString(), newPWDAgain.getText().toString())) {
      ArmsUtils.makeText(getActivity(), "新密码不一致");
      return;
    }
    mPresenter.modifyPwd(originalPWD.getText().toString(), newPWDAgain.getText().toString());
  }

  @Override
  public Activity getActivity() {
    return this;
  }


}
