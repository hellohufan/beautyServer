package com.irontower.gcbuser.di.module;

import com.irontower.gcbuser.mvp.contract.ChoosePositionContract;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;


@Module
public class ChoosePositionModule {

  private ChoosePositionContract.View view;

  /**
   * 构建ChoosePositionModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ChoosePositionModule(ChoosePositionContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ChoosePositionContract.View provideChoosePositionView() {
    return this.view;
  }


}