package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.GoOutRecordModule;

import com.irontower.gcbuser.mvp.ui.activity.GoOutRecordActivity;

@ActivityScope
@Component(modules = GoOutRecordModule.class, dependencies = AppComponent.class)
public interface GoOutRecordComponent {

  void inject(GoOutRecordActivity activity);
}