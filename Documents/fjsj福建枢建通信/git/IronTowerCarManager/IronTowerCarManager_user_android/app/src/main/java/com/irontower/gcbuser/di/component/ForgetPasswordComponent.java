package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ForgetPasswordModule;

import com.irontower.gcbuser.mvp.ui.activity.ForgetPasswordActivity;

@ActivityScope
@Component(modules = ForgetPasswordModule.class, dependencies = AppComponent.class)
public interface ForgetPasswordComponent {

  void inject(ForgetPasswordActivity activity);
}