package com.irontower.gcbuser.di.module;

import com.irontower.gcbuser.mvp.contract.DriverDetailContract;
import com.irontower.gcbuser.mvp.model.DriverDetailModel;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Module;
import dagger.Provides;


@Module
public class DriverDetailModule {

  private DriverDetailContract.View view;

  /**
   * 构建DriverDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public DriverDetailModule(DriverDetailContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  DriverDetailContract.View provideDriverDetailView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  DriverDetailContract.Model provideDriverDetailModel(DriverDetailModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  RxPermissions provideRxPermissions() {
    return new RxPermissions(view.getActivity());
  }
}