package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.RentVehicleContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.CarCenter;
import com.irontower.gcbuser.mvp.model.entity.FeeType;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import java.util.List;
import javax.inject.Inject;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;


@ActivityScope
public class RentVehicleModel extends BaseModel implements RentVehicleContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public RentVehicleModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseRowJson<FeeType>> getFeetTypes(String orgId) {
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .getFeetTypes(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), orgId);
  }


  @Override
  public Observable<BaseJson<List<CarCenter>>> getCarCenters(boolean rent) {
    FormBody formBody = new Builder()
        .add("charge", rent + "").build();
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .getCarCenters(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), formBody);
  }

  @Override
  public Observable<BaseJson<Object>> submitRentCar(String todoId, String carType,
      String orderCarNo,
      String orgId, String feeType) {
    Builder formBody = new Builder()
        .add("todoId", todoId)
        .add("carType", carType)

        .add("orgId", orgId);
    if (!StringUtils.isEmpty(orderCarNo)) {
      formBody.add("orderCarNo", orderCarNo);
    }
    if (!StringUtils.isEmpty(feeType)) {
      formBody.add("feeType", feeType);
    }

    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .submitRentCar(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN),
            formBody.build());
  }
}