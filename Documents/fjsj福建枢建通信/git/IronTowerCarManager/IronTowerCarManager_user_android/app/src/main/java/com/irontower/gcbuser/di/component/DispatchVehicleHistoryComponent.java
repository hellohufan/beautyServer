package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.DispatchVehicleHistoryModule;

import com.irontower.gcbuser.mvp.ui.activity.DispatchVehicleHistoryActivity;

@ActivityScope
@Component(modules = DispatchVehicleHistoryModule.class, dependencies = AppComponent.class)
public interface DispatchVehicleHistoryComponent {

  void inject(DispatchVehicleHistoryActivity activity);
}