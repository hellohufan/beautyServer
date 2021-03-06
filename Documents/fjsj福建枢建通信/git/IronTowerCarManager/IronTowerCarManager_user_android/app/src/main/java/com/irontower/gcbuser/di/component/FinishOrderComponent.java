package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.FinishOrderModule;

import com.irontower.gcbuser.mvp.ui.fragment.FinishOrderFragment;

@ActivityScope
@Component(modules = FinishOrderModule.class, dependencies = AppComponent.class)
public interface FinishOrderComponent {

  void inject(FinishOrderFragment fragment);
}