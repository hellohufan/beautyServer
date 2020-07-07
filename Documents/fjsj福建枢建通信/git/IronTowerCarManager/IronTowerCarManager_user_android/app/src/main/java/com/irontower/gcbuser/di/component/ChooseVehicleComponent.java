package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ChooseVehicleModule;

import com.irontower.gcbuser.mvp.ui.activity.ChooseVehicleActivity;

@ActivityScope
@Component(modules = ChooseVehicleModule.class, dependencies = AppComponent.class)
public interface ChooseVehicleComponent {

  void inject(ChooseVehicleActivity activity);
}