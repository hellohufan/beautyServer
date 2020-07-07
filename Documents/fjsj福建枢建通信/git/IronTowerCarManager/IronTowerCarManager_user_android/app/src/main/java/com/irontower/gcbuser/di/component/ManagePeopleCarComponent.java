package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ManagePeopleCarModule;

import com.irontower.gcbuser.mvp.ui.activity.ManagePeopleCarActivity;

@ActivityScope
@Component(modules = ManagePeopleCarModule.class, dependencies = AppComponent.class)
public interface ManagePeopleCarComponent {

  void inject(ManagePeopleCarActivity activity);
}