package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.UseCarMoneyModule;

import com.irontower.gcbuser.mvp.ui.fragment.UseCarMoneyFragment;

@ActivityScope
@Component(modules = UseCarMoneyModule.class, dependencies = AppComponent.class)
public interface UseCarMoneyComponent {

  void inject(UseCarMoneyFragment fragment);
}