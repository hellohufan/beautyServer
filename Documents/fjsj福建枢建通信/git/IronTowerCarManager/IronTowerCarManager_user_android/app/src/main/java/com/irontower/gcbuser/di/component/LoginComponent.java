package com.irontower.gcbuser.di.component;

import com.irontower.gcbuser.di.module.LoginModule;
import com.irontower.gcbuser.mvp.ui.activity.LoginActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;

@ActivityScope
@Component(modules = LoginModule.class, dependencies = AppComponent.class)
public interface LoginComponent {

  void inject(LoginActivity activity);
}