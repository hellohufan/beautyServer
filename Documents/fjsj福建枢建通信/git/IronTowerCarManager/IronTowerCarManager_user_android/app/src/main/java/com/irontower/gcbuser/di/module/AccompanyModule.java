package com.irontower.gcbuser.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.irontower.gcbuser.mvp.contract.AccompanyContract;
import com.irontower.gcbuser.mvp.model.AccompanyModel;


@Module
public class AccompanyModule {

  private AccompanyContract.View view;

  /**
   * 构建AccompanyModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public AccompanyModule(AccompanyContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  AccompanyContract.View provideAccompanyView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  AccompanyContract.Model provideAccompanyModel(AccompanyModel model) {
    return model;
  }
}