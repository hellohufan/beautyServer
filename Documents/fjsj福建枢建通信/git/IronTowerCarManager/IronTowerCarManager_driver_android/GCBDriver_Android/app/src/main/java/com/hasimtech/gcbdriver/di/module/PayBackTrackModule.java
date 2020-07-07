package com.irontower.gcbdriver.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.irontower.gcbdriver.mvp.contract.PayBackTrackContract;
import com.irontower.gcbdriver.mvp.model.PayBackTrackModel;


@Module
public class PayBackTrackModule {

  private PayBackTrackContract.View view;

  /**
   * 构建PayBackTrackModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public PayBackTrackModule(PayBackTrackContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  PayBackTrackContract.View providePayBackTrackView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  PayBackTrackContract.Model providePayBackTrackModel(PayBackTrackModel model) {
    return model;
  }
}