package com.irontower.gcbdriver.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.ChooseCarModule;

import com.irontower.gcbdriver.mvp.ui.activity.ChooseCarActivity;

@ActivityScope
@Component(modules = ChooseCarModule.class, dependencies = AppComponent.class)
public interface ChooseCarComponent {

  void inject(ChooseCarActivity activity);
}