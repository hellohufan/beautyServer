package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.GoingOrderModule;

import com.irontower.gcbuser.mvp.ui.fragment.GoingOrderFragment;

@ActivityScope
@Component(modules = GoingOrderModule.class, dependencies = AppComponent.class)
public interface GoingOrderComponent {

  void inject(GoingOrderFragment fragment);
}