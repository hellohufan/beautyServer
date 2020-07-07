package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.OrderCarDetail2Module;

import com.irontower.gcbuser.mvp.ui.activity.OrderCarDetail2Activity;

@ActivityScope
@Component(modules = OrderCarDetail2Module.class, dependencies = AppComponent.class)
public interface OrderCarDetail2Component {

  void inject(OrderCarDetail2Activity activity);
}