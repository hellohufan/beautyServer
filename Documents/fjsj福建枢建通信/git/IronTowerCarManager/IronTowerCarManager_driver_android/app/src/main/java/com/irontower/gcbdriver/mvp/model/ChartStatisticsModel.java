package com.irontower.gcbdriver.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbdriver.mvp.contract.ChartStatisticsContract;
import com.irontower.gcbdriver.mvp.model.api.service.CommonService;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import java.util.Map;
import javax.inject.Inject;


@ActivityScope
public class ChartStatisticsModel extends BaseModel implements ChartStatisticsContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public ChartStatisticsModel(IRepositoryManager repositoryManager, Gson gson,
      Application application) {
    super(repositoryManager);
    this.mGson = gson;
    this.mApplication = application;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mGson = null;
    this.mApplication = null;
  }

  @Override
  public io.reactivex.Observable<BaseJson<Map<String, Object>>> getChartData() {
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .getChartData(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN));
  }
}