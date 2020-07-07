package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ApproveGoOutHistoryModule;

import com.irontower.gcbuser.mvp.ui.activity.ApproveGoOutHistoryActivity;

@ActivityScope
@Component(modules = ApproveGoOutHistoryModule.class, dependencies = AppComponent.class)
public interface ApproveGoOutHistoryComponent {

  void inject(ApproveGoOutHistoryActivity activity);
}