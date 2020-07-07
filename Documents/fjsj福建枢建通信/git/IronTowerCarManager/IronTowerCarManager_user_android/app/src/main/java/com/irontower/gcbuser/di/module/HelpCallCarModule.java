package com.irontower.gcbuser.di.module;

import com.jess.arms.di.scope.ActivityScope;

import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Module;
import dagger.Provides;

import com.irontower.gcbuser.mvp.contract.HelpCallCarContract;
import com.irontower.gcbuser.mvp.model.HelpCallCarModel;


@Module
public class HelpCallCarModule {

  private HelpCallCarContract.View view;

  /**
   * 构建HelpCallCarModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public HelpCallCarModule(HelpCallCarContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  HelpCallCarContract.View provideHelpCallCarView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  HelpCallCarContract.Model provideHelpCallCarModel(HelpCallCarModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  RxPermissions getRxPermissions() {
    return new RxPermissions(view.getActivity());
  }

}