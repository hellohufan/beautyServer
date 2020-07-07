package com.irontower.gcbdriver.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbdriver.mvp.contract.ChooseCarContract;
import com.irontower.gcbdriver.mvp.model.api.service.CommonService;
import com.irontower.gcbdriver.mvp.model.entity.BaseRowJson;
import com.irontower.gcbdriver.mvp.model.entity.Car;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class ChooseCarModel extends BaseModel implements ChooseCarContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public ChooseCarModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
  public Observable<BaseRowJson<Car>> getVehicles(String s, int currentPage, int i,
      String beginTime, String endTime) {
    FormBody formBody = new FormBody.Builder()
        .add("carNo", s)

        .add("page", String.valueOf(currentPage))
        .add("rows", "10")
        .add("orderBeginTime", beginTime)
        .add("orderEndTime", endTime)
        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).getCars(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), formBody);
  }
}