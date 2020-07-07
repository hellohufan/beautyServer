package com.irontower.gcbdriver.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.TaskDetailModule;

import com.irontower.gcbdriver.mvp.ui.activity.TaskDetailActivity;

@ActivityScope
@Component(modules = TaskDetailModule.class, dependencies = AppComponent.class)
public interface TaskDetailComponent {

  void inject(TaskDetailActivity activity);
}