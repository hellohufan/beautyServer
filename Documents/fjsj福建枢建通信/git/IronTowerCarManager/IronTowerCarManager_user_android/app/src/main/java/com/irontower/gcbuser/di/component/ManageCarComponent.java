package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ManageCarModule;

import com.irontower.gcbuser.mvp.ui.activity.ManageCarActivity;

@ActivityScope
@Component(modules = ManageCarModule.class, dependencies = AppComponent.class)
public interface ManageCarComponent {

  void inject(ManageCarActivity activity);
}