package com.irontower.gcbdriver.di.component;

import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.OrderFeeListModule;

import com.jess.arms.di.scope.ActivityScope;
import com.irontower.gcbdriver.mvp.ui.activity.OrderFeeListActivity;

@ActivityScope
@Component(modules = OrderFeeListModule.class, dependencies = AppComponent.class)
public interface OrderFeeListComponent {

  void inject(OrderFeeListActivity activity);
}