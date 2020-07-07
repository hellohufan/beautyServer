package com.irontower.gcbuser.di.module;

import com.irontower.gcbuser.mvp.contract.ManagePeopleCarContract;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;


@Module
public class ManagePeopleCarModule {

  private ManagePeopleCarContract.View view;

  /**
   * 构建ManagePeopleCarModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ManagePeopleCarModule(ManagePeopleCarContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ManagePeopleCarContract.View provideManagePeopleCarView() {
    return this.view;
  }


}