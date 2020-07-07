package com.irontower.gcbdriver.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.irontower.gcbdriver.mvp.contract.ApplyFeeDetailContract;
import com.irontower.gcbdriver.mvp.model.ApplyFeeDetailModel;


@Module
public class ApplyFeeDetailModule {

  private ApplyFeeDetailContract.View view;

  /**
   * 构建ApplyFeeDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ApplyFeeDetailModule(ApplyFeeDetailContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ApplyFeeDetailContract.View provideApplyFeeDetailView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ApplyFeeDetailContract.Model provideApplyFeeDetailModel(ApplyFeeDetailModel model) {
    return model;
  }
}