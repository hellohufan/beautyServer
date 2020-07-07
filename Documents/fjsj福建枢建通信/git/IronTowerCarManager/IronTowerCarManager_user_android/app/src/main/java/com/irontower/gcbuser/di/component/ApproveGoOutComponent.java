package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ApproveGoOutModule;

import com.irontower.gcbuser.mvp.ui.activity.ApproveGoOutActivity;

@ActivityScope
@Component(modules = ApproveGoOutModule.class, dependencies = AppComponent.class)
public interface ApproveGoOutComponent {

  void inject(ApproveGoOutActivity activity);
}