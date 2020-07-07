package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.VehicleDetailModule;

import com.irontower.gcbuser.mvp.ui.activity.VehicleDetailActivity;

@ActivityScope
@Component(modules = VehicleDetailModule.class, dependencies = AppComponent.class)
public interface VehicleDetailComponent {

  void inject(VehicleDetailActivity activity);
}