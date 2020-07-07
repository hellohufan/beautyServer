package com.irontower.gcbuser.di.module;

import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbuser.mvp.contract.RentVehicleContract;
import com.irontower.gcbuser.mvp.model.RentVehicleModel;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;


@Module
public class RentVehicleModule {

  private RentVehicleContract.View view;

  /**
   * 构建RentVehicleModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public RentVehicleModule(RentVehicleContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  RentVehicleContract.View provideRentVehicleView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  RentVehicleContract.Model provideRentVehicleModel(RentVehicleModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  MaterialDialog provideMaterialDialog() {
    return new MaterialDialog.Builder(view.getActivity())
        .content("加载中")
        .progress(true, 0).build()
        ;
  }
}