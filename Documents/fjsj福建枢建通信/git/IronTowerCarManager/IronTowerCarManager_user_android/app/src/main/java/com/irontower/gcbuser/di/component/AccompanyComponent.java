package com.irontower.gcbuser.di.component;

import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.AccompanyModule;

import com.jess.arms.di.scope.ActivityScope;
import com.irontower.gcbuser.mvp.ui.activity.AccompanyActivity;

@ActivityScope
@Component(modules = AccompanyModule.class, dependencies = AppComponent.class)
public interface AccompanyComponent {

  void inject(AccompanyActivity activity);
}