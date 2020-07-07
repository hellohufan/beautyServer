package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.OrderCarModule;

import com.irontower.gcbuser.mvp.ui.activity.OrderCarActivity;

@ActivityScope
@Component(modules = OrderCarModule.class, dependencies = AppComponent.class)
public interface OrderCarComponent {

  void inject(OrderCarActivity activity);
}