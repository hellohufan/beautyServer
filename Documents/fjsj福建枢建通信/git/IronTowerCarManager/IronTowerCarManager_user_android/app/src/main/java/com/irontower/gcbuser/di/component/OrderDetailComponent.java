package com.irontower.gcbuser.di.component;

import com.irontower.gcbuser.di.module.ApproveDetailModule;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.mvp.ui.activity.ApproveDetailActivity;

@ActivityScope
@Component(modules = ApproveDetailModule.class, dependencies = AppComponent.class)
public interface OrderDetailComponent {

  void inject(ApproveDetailActivity activity);
}