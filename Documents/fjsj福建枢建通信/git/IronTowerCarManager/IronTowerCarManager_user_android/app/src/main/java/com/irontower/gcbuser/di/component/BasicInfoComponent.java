package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.BasicInfoModule;

import com.irontower.gcbuser.mvp.ui.activity.BasicInfoActivity;

@ActivityScope
@Component(modules = BasicInfoModule.class, dependencies = AppComponent.class)
public interface BasicInfoComponent {

  void inject(BasicInfoActivity activity);
}