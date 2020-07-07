package com.irontower.gcbdriver.di.component;

import com.irontower.gcbdriver.di.module.LoginModule;
import com.irontower.gcbdriver.mvp.ui.activity.LoginActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;

@ActivityScope
@Component(modules = LoginModule.class, dependencies = AppComponent.class)
public interface LoginComponent {

  void inject(LoginActivity activity);
}