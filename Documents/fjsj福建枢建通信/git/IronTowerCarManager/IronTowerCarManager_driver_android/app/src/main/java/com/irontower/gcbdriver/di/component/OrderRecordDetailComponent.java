package com.irontower.gcbdriver.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.OrderRecordDetailModule;

import com.irontower.gcbdriver.mvp.ui.activity.OrderRecordDetailActivity;

@ActivityScope
@Component(modules = OrderRecordDetailModule.class, dependencies = AppComponent.class)
public interface OrderRecordDetailComponent {

  void inject(OrderRecordDetailActivity activity);
}