package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ModifyPasswordModule;

import com.irontower.gcbuser.mvp.ui.activity.ModifyPasswordActivity;

@ActivityScope
@Component(modules = ModifyPasswordModule.class, dependencies = AppComponent.class)
public interface ModifyPasswordComponent {

  void inject(ModifyPasswordActivity activity);
}