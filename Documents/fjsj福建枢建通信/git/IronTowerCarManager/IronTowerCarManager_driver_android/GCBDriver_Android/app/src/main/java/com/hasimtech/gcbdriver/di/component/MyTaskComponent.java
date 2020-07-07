package com.irontower.gcbdriver.di.component;

import com.irontower.gcbdriver.di.module.MyTaskModule;
import com.irontower.gcbdriver.mvp.ui.fragment.MyTaskFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;

@ActivityScope
@Component(modules = MyTaskModule.class, dependencies = AppComponent.class)
public interface MyTaskComponent {

  void inject(MyTaskFragment fragment);
}