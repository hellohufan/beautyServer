package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.ChooseDriverContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.Driver;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import javax.inject.Inject;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;


@ActivityScope
public class ChooseDriverModel extends BaseModel implements ChooseDriverContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public ChooseDriverModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseRowJson<Driver>> getDrivers(String s, int page, int rows,
      int type,
      String orderBeginTime, String orderEndTime, String orgId, String selectedDriverId) {
    FormBody formBody;
    if (StringUtils.isEmpty(selectedDriverId)) {
      formBody = new Builder()
          .add("userName", s)
          .add("page", String.valueOf(page))
          .add("rows", "10")
          .add("orderBeginTime", orderBeginTime)
          .add("orderEndTime", orderEndTime)
          .add("orgId", orgId).build();
    } else {
      formBody = new Builder()
          .add("userName", s)
          .add("page", String.valueOf(page))
          .add("rows", "10")
          .add("orderBeginTime", orderBeginTime)
          .add("orderEndTime", orderEndTime)
          .add("orgId", orgId)
          .add("selectedDriverId", selectedDriverId).build();
    }

    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .getDrivers(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), formBody);
  }

  @Override
  public Observable<BaseJson<Object>> submit(String todoId, String orderCarNo, String carId,
      String driverId, String carType, String feeType) {
    if (StringUtils.isEmpty(carType) || StringUtils.equals(carType, "null")) {
      carType = "";
    }
    Builder formBody = new Builder()
        .add("todoId", todoId)
        .add("carId", carId)
        .add("carType", carType)
        .add("driverId", driverId);

    if (!StringUtils.isEmpty(orderCarNo)) {
      formBody.add("orderCarNo", orderCarNo);

    }
    if (!StringUtils.isEmpty(feeType)) {
      formBody.add("feeType", feeType);
    }
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .submitDispatchCar(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN),
            formBody.build());
  }
}