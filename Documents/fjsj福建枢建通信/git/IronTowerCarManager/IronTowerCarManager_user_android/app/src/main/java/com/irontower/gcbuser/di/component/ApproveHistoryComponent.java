package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ApproveHistoryModule;

import com.irontower.gcbuser.mvp.ui.activity.ApproveHistoryActivity;

@ActivityScope
@Component(modules = ApproveHistoryModule.class, dependencies = AppComponent.class)
public interface ApproveHistoryComponent {

  void inject(ApproveHistoryActivity activity);
}