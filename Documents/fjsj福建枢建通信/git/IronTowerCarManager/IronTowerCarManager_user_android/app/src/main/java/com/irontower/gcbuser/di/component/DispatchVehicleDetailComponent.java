package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.DispatchVehicleDetailModule;

import com.irontower.gcbuser.mvp.ui.activity.DispatchVehicleDetailActivity;

@ActivityScope
@Component(modules = DispatchVehicleDetailModule.class, dependencies = AppComponent.class)
public interface DispatchVehicleDetailComponent {

  void inject(DispatchVehicleDetailActivity activity);
}