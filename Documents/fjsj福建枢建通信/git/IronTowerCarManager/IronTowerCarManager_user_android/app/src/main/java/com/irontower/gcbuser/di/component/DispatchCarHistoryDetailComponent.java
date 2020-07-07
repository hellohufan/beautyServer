package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.DispatchCarHistoryDetailModule;

import com.irontower.gcbuser.mvp.ui.activity.DispatchCarHistoryDetailActivity;

@ActivityScope
@Component(modules = DispatchCarHistoryDetailModule.class, dependencies = AppComponent.class)
public interface DispatchCarHistoryDetailComponent {

  void inject(DispatchCarHistoryDetailActivity activity);
}