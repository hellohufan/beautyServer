package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ChooseStartPositionModule;

import com.irontower.gcbuser.mvp.ui.activity.ChooseStartPositionActivity;

@ActivityScope
@Component(modules = ChooseStartPositionModule.class, dependencies = AppComponent.class)
public interface ChooseStartPositionComponent {

  void inject(ChooseStartPositionActivity activity);
}