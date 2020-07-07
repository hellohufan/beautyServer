package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ApproveBegOffModule;

import com.irontower.gcbuser.mvp.ui.activity.ApproveBegOffActivity;

@ActivityScope
@Component(modules = ApproveBegOffModule.class, dependencies = AppComponent.class)
public interface ApproveBegOffComponent {

  void inject(ApproveBegOffActivity activity);
}