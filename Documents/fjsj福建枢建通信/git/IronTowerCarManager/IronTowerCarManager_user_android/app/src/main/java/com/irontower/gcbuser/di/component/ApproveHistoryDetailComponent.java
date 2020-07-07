package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ApproveHistoryDetailModule;

import com.irontower.gcbuser.mvp.ui.activity.ApproveHistoryDetailActivity;

@ActivityScope
@Component(modules = ApproveHistoryDetailModule.class, dependencies = AppComponent.class)
public interface ApproveHistoryDetailComponent {

  void inject(ApproveHistoryDetailActivity activity);
}