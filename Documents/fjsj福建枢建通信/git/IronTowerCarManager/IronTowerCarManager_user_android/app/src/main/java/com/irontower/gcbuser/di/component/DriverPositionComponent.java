package com.irontower.gcbuser.di.component;

import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.DriverPositionModule;

import com.jess.arms.di.scope.ActivityScope;
import com.irontower.gcbuser.mvp.ui.activity.DriverPositionActivity;

@ActivityScope
@Component(modules = DriverPositionModule.class, dependencies = AppComponent.class)
public interface DriverPositionComponent {

  void inject(DriverPositionActivity activity);
}