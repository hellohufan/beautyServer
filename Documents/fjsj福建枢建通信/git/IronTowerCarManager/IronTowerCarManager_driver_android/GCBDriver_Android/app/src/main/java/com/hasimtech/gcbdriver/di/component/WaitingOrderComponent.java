package com.irontower.gcbdriver.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.WaitingOrderModule;

import com.irontower.gcbdriver.mvp.ui.fragment.WaitingOrderFragment;

@ActivityScope
@Component(modules = WaitingOrderModule.class, dependencies = AppComponent.class)
public interface WaitingOrderComponent {

  void inject(WaitingOrderFragment fragment);
}