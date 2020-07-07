package com.irontower.gcbdriver.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.KilometerNumberModule;

import com.irontower.gcbdriver.mvp.ui.fragment.KilometerNumberFragment;

@ActivityScope
@Component(modules = KilometerNumberModule.class, dependencies = AppComponent.class)
public interface KilometerNumberComponent {

  void inject(KilometerNumberFragment fragment);
}