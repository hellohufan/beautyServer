package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.OrderRecordDetailModule;

import com.irontower.gcbuser.mvp.ui.activity.OrderRecordDetailActivity;

@ActivityScope
@Component(modules = OrderRecordDetailModule.class, dependencies = AppComponent.class)
public interface OrderRecordDetailComponent {

  void inject(OrderRecordDetailActivity activity);
}