package com.irontower.gcbdriver.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.di.component.DaggerLoginComponent;
import com.irontower.gcbdriver.di.module.LoginModule;
import com.irontower.gcbdriver.mvp.contract.LoginContract.View;
import com.irontower.gcbdriver.mvp.presenter.LoginPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import javax.inject.Inject;


public class LoginActivity extends BaseActivity<LoginPresenter> implements View {

  @Inject
  MaterialDialog materialDialog;
  @BindView(R.id.etUserNo)
  AppCompatEditText etUserNo;
  @BindView(R.id.etPwd)
  AppCompatEditText etPwd;
  @BindView(R.id.ivEye)
  ImageView ivEye;
  @BindView(R.id.ivClear)
  ImageView ivClear;
  @BindView(R.id.layLogin)
  RelativeLayout layLogin;
  @Inject
  RxPermissions rxPermissions;
  private boolean etUserNoHasFocus = true;
  private boolean isHidden = true;
  private boolean etPwdHasFocus = false;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerLoginComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .loginModule(new LoginModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_login; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    mPresenter.init();

    etUserNo.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (etUserNoHasFocus) {
          if (charSequence.length() > 0) {
            ivClear.setVisibility(android.view.View.VISIBLE);
          } else {
            ivClear.setVisibility(android.view.View.GONE);

          }
        } else {
          ivClear.setVisibility(android.view.View.GONE);

        }
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });
    etUserNo.setOnFocusChangeListener(new OnFocusChangeListener() {
      @Override
      public void onFocusChange(android.view.View view, boolean b) {
        etUserNoHasFocus = b;
        if (b) {
          if (etUserNo.getText().length() > 0) {
            ivClear.setVisibility(android.view.View.VISIBLE);
          } else {
            ivClear.setVisibility(android.view.View.GONE);

          }
        } else {
          ivClear.setVisibility(android.view.View.GONE);

        }
      }
    });
    etPwd.setOnFocusChangeListener(new OnFocusChangeListener() {
      @Override
      public void onFocusChange(android.view.View view, boolean b) {
        etPwdHasFocus = b;
      }
    });


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
  public boolean useFragment() {
    return false;
  }


  @Override
  public Activity getActivity() {
    return this;
  }

  @Override
  public Resources getResources() {
    Resources res = super.getResources();
    Configuration config = new Configuration();
    config.setToDefaults();
    res.updateConfiguration(config, res.getDisplayMetrics());
    return res;

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (materialDialog != null) {
      materialDialog.dismiss();
      materialDialog = null;
    }

  }


  @OnClick({R.id.layLogin, R.id.ivEye, R.id.ivClear, R.id.tvForgetPWD})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {

      case R.id.layLogin:
        if (etUserNo.getText().length() == 0) {
          ArmsUtils.makeText(this, ArmsUtils.getString(this, R.string.user_no_empty));
          return;
        }
        if (etPwd.getText().length() == 0) {
          ArmsUtils.makeText(this, ArmsUtils.getString(this, R.string.pwd_empty));
          return;
        }
        mPresenter.login(etUserNo.getText().toString(), etPwd.getText().toString().trim());
        break;
      case R.id.ivEye:
        if (isHidden) {
          ivEye.setImageResource(R.mipmap.eye_open);
          HideReturnsTransformationMethod method1 = HideReturnsTransformationMethod.getInstance();
          etPwd.setTransformationMethod(method1);
          isHidden = false;
          if (etPwdHasFocus) {
            etPwd.setSelection(etPwd.getText().length());
          }
        } else {
          ivEye.setImageResource(R.mipmap.eye_close);
          TransformationMethod method = PasswordTransformationMethod.getInstance();
          etPwd.setTransformationMethod(method);
          isHidden = true;
          if (etPwdHasFocus) {
            etPwd.setSelection(etPwd.getText().length());
          }
        }

        break;
      case R.id.ivClear:
        etUserNo.setText("");
        break;
      case R.id.tvForgetPWD:
        Intent intent = new Intent();
        intent.setClass(getActivity(), ForgetPasswordActivity.class);
        startActivity(intent);
        break;
      default:
    }
  }

  @Override
  public RxPermissions getRxPermissions() {
    return rxPermissions;
  }
}
