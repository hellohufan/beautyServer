package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ApproveBegOffHistoryModule;

import com.irontower.gcbuser.mvp.ui.activity.ApproveBegOffHistoryActivity;

@ActivityScope
@Component(modules = ApproveBegOffHistoryModule.class, dependencies = AppComponent.class)
public interface ApproveBegOffHistoryComponent {

  void inject(ApproveBegOffHistoryActivity activity);
}