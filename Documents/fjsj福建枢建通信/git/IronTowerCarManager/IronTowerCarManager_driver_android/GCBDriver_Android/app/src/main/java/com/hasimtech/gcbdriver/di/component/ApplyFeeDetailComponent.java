package com.irontower.gcbdriver.di.component;

import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.ApplyFeeDetailModule;

import com.jess.arms.di.scope.ActivityScope;
import com.irontower.gcbdriver.mvp.ui.activity.ApplyFeeDetailActivity;

@ActivityScope
@Component(modules = ApplyFeeDetailModule.class, dependencies = AppComponent.class)
public interface ApplyFeeDetailComponent {

  void inject(ApplyFeeDetailActivity activity);
}