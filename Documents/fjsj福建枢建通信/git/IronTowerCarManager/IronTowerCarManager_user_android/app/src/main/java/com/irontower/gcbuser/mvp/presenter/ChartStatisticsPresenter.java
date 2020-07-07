package com.irontower.gcbuser.mvp.presenter;

import com.irontower.gcbuser.mvp.contract.ChartStatisticsContract;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import javax.inject.Inject;


@ActivityScope
public class ChartStatisticsPresenter extends
    BasePresenter<ChartStatisticsContract.Model, ChartStatisticsContract.View> {


  @Inject
  public ChartStatisticsPresenter(ChartStatisticsContract.Model model,
      ChartStatisticsContract.View rootView
  ) {
    super(model, rootView);

  }

  @Override
  public void onDestroy() {
    super.onDestroy();

  }


}
