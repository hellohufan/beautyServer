package com.irontower.gcbuser.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.CityPickerModule;

import com.irontower.gcbuser.mvp.ui.activity.CityPickerActivity;

@ActivityScope
@Component(modules = CityPickerModule.class, dependencies = AppComponent.class)
public interface CityPickerComponent {

  void inject(CityPickerActivity activity);
}