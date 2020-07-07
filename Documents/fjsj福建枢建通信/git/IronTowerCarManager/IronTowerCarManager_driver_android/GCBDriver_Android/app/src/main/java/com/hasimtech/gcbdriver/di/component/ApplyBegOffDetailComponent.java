package com.irontower.gcbdriver.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.ApplyBegOffDetailModule;

import com.irontower.gcbdriver.mvp.ui.activity.ApplyBegOffDetailActivity;

@ActivityScope
@Component(modules = ApplyBegOffDetailModule.class, dependencies = AppComponent.class)
public interface ApplyBegOffDetailComponent {

  void inject(ApplyBegOffDetailActivity activity);
}