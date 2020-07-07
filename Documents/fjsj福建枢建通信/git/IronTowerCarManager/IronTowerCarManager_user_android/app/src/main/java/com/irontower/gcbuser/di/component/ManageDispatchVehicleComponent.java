package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ManageDispatchVehicleModule;

import com.irontower.gcbuser.mvp.ui.activity.ManageDispatchVehicleActivity;

@ActivityScope
@Component(modules = ManageDispatchVehicleModule.class, dependencies = AppComponent.class)
public interface ManageDispatchVehicleComponent {

  void inject(ManageDispatchVehicleActivity activity);
}