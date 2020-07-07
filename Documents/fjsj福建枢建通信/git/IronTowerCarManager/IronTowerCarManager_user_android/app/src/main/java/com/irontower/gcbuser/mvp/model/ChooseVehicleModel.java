package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.ChooseVehicleContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.Vehicle;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class ChooseVehicleModel extends BaseModel implements ChooseVehicleContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public ChooseVehicleModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseRowJson<Vehicle>> getVehicles(String s, int page, int rows,
      String orderBeginTime,
      String orderEndTime, String useOrgId, String carType, String selectedCarId,
      String feeType, String orderNo) {

    FormBody.Builder formBody;
    if (StringUtils.isEmpty(selectedCarId)) {
      formBody = new FormBody.Builder()
          .add("carNo", s)

          .add("page", String.valueOf(page))
          .add("rows", "10")
          .add("orderBeginTime", orderBeginTime)
          .add("orderEndTime", orderEndTime)
          .add("useOrgId", useOrgId)
          .add("carType", StringUtils.isEmpty(carType) ? "" : carType);

    } else {
      formBody = new FormBody.Builder()
          .add("carNo", s)

          .add("page", String.valueOf(page))
          .add("rows", "10")
          .add("orderBeginTime", orderBeginTime)
          .add("orderEndTime", orderEndTime)
          .add("useOrgId", useOrgId)
          .add("carType", StringUtils.isEmpty(carType) ? "" : carType)
          .add("selectedCarId", selectedCarId);
    }
    if (!StringUtils.isEmpty(feeType)) {
      formBody.add("feeType", feeType);
    }
    formBody.add("orderNo", orderNo);
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .getVehicles(SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), formBody.build());
  }
}