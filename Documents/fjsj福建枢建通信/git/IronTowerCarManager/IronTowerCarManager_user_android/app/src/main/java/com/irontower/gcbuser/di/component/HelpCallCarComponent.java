package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.HelpCallCarModule;

import com.irontower.gcbuser.mvp.ui.activity.HelpCallCarActivity;

@ActivityScope
@Component(modules = HelpCallCarModule.class, dependencies = AppComponent.class)
public interface HelpCallCarComponent {

  void inject(HelpCallCarActivity activity);
}