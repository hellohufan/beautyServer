package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ChooseDriverModule;

import com.irontower.gcbuser.mvp.ui.activity.ChooseDriverActivity;

@ActivityScope
@Component(modules = ChooseDriverModule.class, dependencies = AppComponent.class)
public interface ChooseDriverComponent {

  void inject(ChooseDriverActivity activity);
}