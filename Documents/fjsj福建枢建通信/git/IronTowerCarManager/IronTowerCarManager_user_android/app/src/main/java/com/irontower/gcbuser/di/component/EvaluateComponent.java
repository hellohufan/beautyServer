package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.EvaluateModule;

import com.irontower.gcbuser.mvp.ui.activity.EvaluateActivity;

@ActivityScope
@Component(modules = EvaluateModule.class, dependencies = AppComponent.class)
public interface EvaluateComponent {

  void inject(EvaluateActivity activity);
}