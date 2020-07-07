package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.DriverPositionContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import java.util.Map;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class DriverPositionModel extends BaseModel implements DriverPositionContract.Model {

  @Inject
  Gson mGson;
  @Inject
  Application mApplication;

  @Inject
  public DriverPositionModel(IRepositoryManager repositoryManager) {
    super(repositoryManager);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mGson = null;
    this.mApplication = null;
  }

  @Override
  public Observable<BaseJson<Map<String, Object>>> getData(Integer carId) {
    FormBody body = new FormBody.Builder()
        .add("refId", String.valueOf(carId))
        .add("queryInfo", "false")
        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).getDriverPosition(SPUtils
        .getInstance().getString(
            CommonService.ACCESS_TOKEN), body);
  }
}