package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ChoosePlatformModule;

import com.irontower.gcbuser.mvp.ui.activity.ChoosePlatformActivity;

@ActivityScope
@Component(modules = ChoosePlatformModule.class, dependencies = AppComponent.class)
public interface ChoosePlatformComponent {

  void inject(ChoosePlatformActivity activity);
}