package com.irontower.gcbdriver.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.ApplyBegOffModule;

import com.irontower.gcbdriver.mvp.ui.activity.ApplyBegOffActivity;

@ActivityScope
@Component(modules = ApplyBegOffModule.class, dependencies = AppComponent.class)
public interface ApplyBegOffComponent {

  void inject(ApplyBegOffActivity activity);
}