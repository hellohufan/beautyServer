package com.irontower.gcbuser.di.component;

import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ReasonModule;

import com.jess.arms.di.scope.ActivityScope;
import com.irontower.gcbuser.mvp.ui.activity.ReasonActivity;

@ActivityScope
@Component(modules = ReasonModule.class, dependencies = AppComponent.class)
public interface ReasonComponent {

  void inject(ReasonActivity activity);
}