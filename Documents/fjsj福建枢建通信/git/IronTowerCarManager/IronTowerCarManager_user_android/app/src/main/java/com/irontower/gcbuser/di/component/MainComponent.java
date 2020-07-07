package com.irontower.gcbuser.di.component;

import com.irontower.gcbuser.di.module.MainModule;
import com.irontower.gcbuser.mvp.ui.activity.MainActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;


@ActivityScope
@Component(modules = MainModule.class, dependencies = AppComponent.class)
public interface MainComponent {

  void inject(MainActivity activity);
}