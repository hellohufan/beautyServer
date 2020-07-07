package com.irontower.gcbuser.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.irontower.gcbuser.mvp.contract.ReasonContract;
import com.irontower.gcbuser.mvp.model.ReasonModel;


@Module
public class ReasonModule {

  private ReasonContract.View view;

  /**
   * 构建ReasonModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ReasonModule(ReasonContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ReasonContract.View provideReasonView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ReasonContract.Model provideReasonModel(ReasonModel model) {
    return model;
  }
}