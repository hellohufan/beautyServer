package com.irontower.gcbuser.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.irontower.gcbuser.mvp.contract.ChoosePlatformContract;
import com.irontower.gcbuser.mvp.model.ChoosePlatformModel;


@Module
public class ChoosePlatformModule {

  private ChoosePlatformContract.View view;

  /**
   * 构建ChoosePlatformModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ChoosePlatformModule(ChoosePlatformContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ChoosePlatformContract.View provideChoosePlatformView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ChoosePlatformContract.Model provideChoosePlatformModel(ChoosePlatformModel model) {
    return model;
  }
}