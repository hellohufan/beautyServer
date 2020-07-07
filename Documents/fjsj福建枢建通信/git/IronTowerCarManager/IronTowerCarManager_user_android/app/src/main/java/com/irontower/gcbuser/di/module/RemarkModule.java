package com.irontower.gcbuser.di.module;

import com.irontower.gcbuser.mvp.contract.RemarkContract;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;


@Module
public class RemarkModule {

  private RemarkContract.View view;

  /**
   * 构建RemarkModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public RemarkModule(RemarkContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  RemarkContract.View provideRemarkView() {
    return this.view;
  }


}