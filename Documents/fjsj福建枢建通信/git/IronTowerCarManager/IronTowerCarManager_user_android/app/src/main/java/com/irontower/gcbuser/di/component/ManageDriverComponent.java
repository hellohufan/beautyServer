package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ManageDriverModule;

import com.irontower.gcbuser.mvp.ui.activity.ManageDriverActivity;

@ActivityScope
@Component(modules = ManageDriverModule.class, dependencies = AppComponent.class)
public interface ManageDriverComponent {

  void inject(ManageDriverActivity activity);
}