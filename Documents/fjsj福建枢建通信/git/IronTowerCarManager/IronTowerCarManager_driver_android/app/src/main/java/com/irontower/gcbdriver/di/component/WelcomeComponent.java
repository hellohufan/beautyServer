package com.irontower.gcbdriver.di.component;

import com.irontower.gcbdriver.di.module.WelcomeModule;
import com.irontower.gcbdriver.mvp.ui.activity.WelcomeActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;

/**
 * Created by jianghaifeng on 2017/11/30.
 */
@ActivityScope
@Component(modules = WelcomeModule.class, dependencies = AppComponent.class)
public interface WelcomeComponent {

  void inject(WelcomeActivity activity);
}
