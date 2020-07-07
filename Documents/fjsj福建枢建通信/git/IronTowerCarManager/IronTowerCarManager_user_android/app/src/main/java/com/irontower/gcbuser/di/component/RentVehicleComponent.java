package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.RentVehicleModule;

import com.irontower.gcbuser.mvp.ui.activity.RentVehicleActivity;

@ActivityScope
@Component(modules = RentVehicleModule.class, dependencies = AppComponent.class)
public interface RentVehicleComponent {

  void inject(RentVehicleActivity activity);
}