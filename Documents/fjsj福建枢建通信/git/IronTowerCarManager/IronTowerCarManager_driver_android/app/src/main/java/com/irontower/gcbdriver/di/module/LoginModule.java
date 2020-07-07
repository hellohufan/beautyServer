package com.irontower.gcbdriver.di.module;

import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.LoginContract;
import com.irontower.gcbdriver.mvp.model.LoginModel;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Module;
import dagger.Provides;


@Module
public class LoginModule {

  private LoginContract.View view;

  /**
   * 构建LoginModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public LoginModule(LoginContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  LoginContract.View provideLoginView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  LoginContract.Model provideLoginModel(LoginModel model) {
    return model;
  }


  @ActivityScope
  @Provides
  MaterialDialog provideMaterialDialog() {
    return new MaterialDialog.Builder(view.getActivity())
        .content(R.string.login_wait)
        .progress(true, 0).build()
        ;
  }

  @ActivityScope
  @Provides
  RxPermissions provideRxPermissions() {
    return new RxPermissions(view.getActivity());
  }
}