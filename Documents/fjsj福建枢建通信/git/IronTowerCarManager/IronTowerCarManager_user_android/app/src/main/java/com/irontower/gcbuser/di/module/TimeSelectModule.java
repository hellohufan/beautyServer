package com.irontower.gcbuser.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.irontower.gcbuser.mvp.contract.TimeSelectContract;
import com.irontower.gcbuser.mvp.model.TimeSelectModel;


@Module
public class TimeSelectModule {

  private TimeSelectContract.View view;

  /**
   * 构建TimeSelectModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public TimeSelectModule(TimeSelectContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  TimeSelectContract.View provideTimeSelectView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  TimeSelectContract.Model provideTimeSelectModel(TimeSelectModel model) {
    return model;
  }
}