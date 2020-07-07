package com.irontower.gcbuser.di.module;

import com.irontower.gcbuser.mvp.contract.ChartStatisticsContract;
import com.irontower.gcbuser.mvp.model.ChartStatisticsModel;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;


@Module
public class ChartStatisticsModule {

  private ChartStatisticsContract.View view;

  /**
   * 构建ChartStatisticsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ChartStatisticsModule(ChartStatisticsContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ChartStatisticsContract.View provideChartStatisticsView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ChartStatisticsContract.Model provideChartStatisticsModel(ChartStatisticsModel model) {
    return model;
  }


}