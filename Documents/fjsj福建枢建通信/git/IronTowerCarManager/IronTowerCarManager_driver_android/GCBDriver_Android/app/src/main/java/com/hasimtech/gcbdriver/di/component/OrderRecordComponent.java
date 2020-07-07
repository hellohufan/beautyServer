package com.irontower.gcbdriver.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.OrderRecordModule;

import com.irontower.gcbdriver.mvp.ui.activity.OrderRecordActivity;

@ActivityScope
@Component(modules = OrderRecordModule.class, dependencies = AppComponent.class)
public interface OrderRecordComponent {

  void inject(OrderRecordActivity activity);
}