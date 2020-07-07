package com.irontower.gcbuser.di.module;

import com.irontower.gcbuser.mvp.contract.CityPickerContract;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;


@Module
public class CityPickerModule {

  private CityPickerContract.View view;

  /**
   * 构建CityPickerModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public CityPickerModule(CityPickerContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  CityPickerContract.View provideCityPickerView() {
    return this.view;
  }


}