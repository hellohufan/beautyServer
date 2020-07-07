package com.irontower.gcbuser.di.component;

import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.TimeSelectModule;

import com.jess.arms.di.scope.ActivityScope;
import com.irontower.gcbuser.mvp.ui.activity.TimeSelectActivity;

@ActivityScope
@Component(modules = TimeSelectModule.class, dependencies = AppComponent.class)
public interface TimeSelectComponent {

  void inject(TimeSelectActivity activity);
}