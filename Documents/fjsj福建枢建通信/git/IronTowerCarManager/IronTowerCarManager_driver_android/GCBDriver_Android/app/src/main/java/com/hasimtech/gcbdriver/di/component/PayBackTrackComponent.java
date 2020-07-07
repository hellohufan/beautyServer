package com.irontower.gcbdriver.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.PayBackTrackModule;

import com.irontower.gcbdriver.mvp.ui.activity.PayBackTrackActivity;

@ActivityScope
@Component(modules = PayBackTrackModule.class, dependencies = AppComponent.class)
public interface PayBackTrackComponent {

  void inject(PayBackTrackActivity activity);
}