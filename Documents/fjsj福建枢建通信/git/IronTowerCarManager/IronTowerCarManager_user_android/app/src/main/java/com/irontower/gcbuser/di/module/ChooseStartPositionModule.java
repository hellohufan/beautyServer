package com.irontower.gcbuser.di.module;

import com.irontower.gcbuser.mvp.contract.ChooseStartPositionContract;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;


@Module
public class ChooseStartPositionModule {

  private ChooseStartPositionContract.View view;

  /**
   * 构建ChooseStartPositionModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ChooseStartPositionModule(ChooseStartPositionContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ChooseStartPositionContract.View provideChooseStartPositionView() {
    return this.view;
  }

}