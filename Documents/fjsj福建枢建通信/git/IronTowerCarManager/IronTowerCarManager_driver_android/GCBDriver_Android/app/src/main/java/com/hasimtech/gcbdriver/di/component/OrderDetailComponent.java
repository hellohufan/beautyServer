package com.irontower.gcbdriver.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.OrderDetailModule;

import com.irontower.gcbdriver.mvp.ui.activity.OrderDetailActivity;

@ActivityScope
@Component(modules = OrderDetailModule.class, dependencies = AppComponent.class)
public interface OrderDetailComponent {

  void inject(OrderDetailActivity activity);
}