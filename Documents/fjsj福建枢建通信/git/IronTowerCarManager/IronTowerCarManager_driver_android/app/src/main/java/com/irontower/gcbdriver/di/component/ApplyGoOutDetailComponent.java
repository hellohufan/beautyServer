package com.irontower.gcbdriver.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.ApplyGoOutDetailModule;

import com.irontower.gcbdriver.mvp.ui.activity.ApplyGoOutDetailActivity;

@ActivityScope
@Component(modules = ApplyGoOutDetailModule.class, dependencies = AppComponent.class)
public interface ApplyGoOutDetailComponent {

  void inject(ApplyGoOutDetailActivity activity);
}