package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.DriverDetailModule;

import com.irontower.gcbuser.mvp.ui.activity.DriverDetailActivity;

@ActivityScope
@Component(modules = DriverDetailModule.class, dependencies = AppComponent.class)
public interface DriverDetailComponent {

  void inject(DriverDetailActivity activity);
}