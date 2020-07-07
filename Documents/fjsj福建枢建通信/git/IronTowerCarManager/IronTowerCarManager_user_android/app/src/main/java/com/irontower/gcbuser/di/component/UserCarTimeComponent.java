package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.UserCarTimeModule;

import com.irontower.gcbuser.mvp.ui.fragment.UserCarTimeFragment;

@ActivityScope
@Component(modules = UserCarTimeModule.class, dependencies = AppComponent.class)
public interface UserCarTimeComponent {

  void inject(UserCarTimeFragment fragment);
}