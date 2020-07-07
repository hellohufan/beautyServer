package com.irontower.gcbuser.di.module;

import com.irontower.gcbuser.mvp.contract.DriverPositionContract;
import com.irontower.gcbuser.mvp.model.DriverPositionModel;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Module;
import dagger.Provides;


@Module
public class DriverPositionModule {

  private DriverPositionContract.View view;

  /**
   * 构建DriverPositionModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public DriverPositionModule(DriverPositionContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  DriverPositionContract.View provideDriverPositionView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  DriverPositionContract.Model provideDriverPositionModel(DriverPositionModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  RxPermissions provideRxPermissions() {
    return new RxPermissions(view.getActivity());
  }
}