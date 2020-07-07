package com.irontower.gcbdriver.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.ApplyGoOutModule;

import com.irontower.gcbdriver.mvp.ui.activity.ApplyGoOutActivity;

@ActivityScope
@Component(modules = ApplyGoOutModule.class, dependencies = AppComponent.class)
public interface ApplyGoOutComponent {

  void inject(ApplyGoOutActivity activity);
}