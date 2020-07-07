package com.irontower.gcbuser.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.irontower.gcbuser.mvp.contract.VehicleDetailContract;
import com.irontower.gcbuser.mvp.model.VehicleDetailModel;


@Module
public class VehicleDetailModule {

  private VehicleDetailContract.View view;

  /**
   * 构建VehicleDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public VehicleDetailModule(VehicleDetailContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  VehicleDetailContract.View provideVehicleDetailView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  VehicleDetailContract.Model provideVehicleDetailModel(VehicleDetailModel model) {
    return model;
  }
}