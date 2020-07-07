package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ChoosePositionModule;

import com.irontower.gcbuser.mvp.ui.activity.ChoosePositionActivity;

@ActivityScope
@Component(modules = ChoosePositionModule.class, dependencies = AppComponent.class)
public interface ChoosePositionComponent {

  void inject(ChoosePositionActivity activity);
}