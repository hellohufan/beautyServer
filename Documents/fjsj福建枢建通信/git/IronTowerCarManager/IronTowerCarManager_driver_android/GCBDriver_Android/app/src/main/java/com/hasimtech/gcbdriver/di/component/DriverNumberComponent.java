package com.irontower.gcbdriver.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.DriverNumberModule;

import com.irontower.gcbdriver.mvp.ui.fragment.DriverNumberFragment;

@ActivityScope
@Component(modules = DriverNumberModule.class, dependencies = AppComponent.class)
public interface DriverNumberComponent {

  void inject(DriverNumberFragment fragment);
}