package com.irontower.gcbdriver.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.ChartStatisticsModule;

import com.irontower.gcbdriver.mvp.ui.activity.ChartStatisticsActivity;

@ActivityScope
@Component(modules = ChartStatisticsModule.class, dependencies = AppComponent.class)
public interface ChartStatisticsComponent {

  void inject(ChartStatisticsActivity activity);
}