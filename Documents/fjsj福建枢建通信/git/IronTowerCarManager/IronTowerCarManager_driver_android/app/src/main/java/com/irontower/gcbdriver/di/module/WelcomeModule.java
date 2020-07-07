package com.irontower.gcbdriver.di.module;

import com.irontower.gcbdriver.mvp.contract.WelcomeContract;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;

/**
 * Created by jianghaifeng on 2017/11/30.
 */
@Module
public class WelcomeModule {


  private WelcomeContract.View view;

  /**
   * 构建WelcomeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public WelcomeModule(WelcomeContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  WelcomeContract.View provideWelcomeView() {
    return this.view;
  }


}
