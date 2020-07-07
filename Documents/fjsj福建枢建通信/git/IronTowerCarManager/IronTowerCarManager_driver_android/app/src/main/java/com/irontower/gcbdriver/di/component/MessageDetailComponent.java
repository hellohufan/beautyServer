package com.irontower.gcbdriver.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.MessageDetailModule;

import com.irontower.gcbdriver.mvp.ui.activity.MessageDetailActivity;

@ActivityScope
@Component(modules = MessageDetailModule.class, dependencies = AppComponent.class)
public interface MessageDetailComponent {

  void inject(MessageDetailActivity activity);
}