package com.irontower.gcbdriver.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.BegGoOutDetailModule;

import com.irontower.gcbdriver.mvp.ui.activity.BegGoOutDetailActivity;

@ActivityScope
@Component(modules = BegGoOutDetailModule.class, dependencies = AppComponent.class)
public interface BegGoOutDetailComponent {

  void inject(BegGoOutDetailActivity activity);
}