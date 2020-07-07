package com.irontower.gcbuser.di.component;

import com.irontower.gcbuser.di.module.ApproveUseVehicleModule;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.mvp.ui.activity.ApproveUseVehicleActivity;

@ActivityScope
@Component(modules = ApproveUseVehicleModule.class, dependencies = AppComponent.class)
public interface ApproveUseVehicleComponent {

  void inject(ApproveUseVehicleActivity activity);
}