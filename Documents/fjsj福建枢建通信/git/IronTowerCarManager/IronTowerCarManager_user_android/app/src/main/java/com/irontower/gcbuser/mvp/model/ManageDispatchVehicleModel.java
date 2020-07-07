package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.ManageDispatchVehicleContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.ApproveOrder;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class ManageDispatchVehicleModel extends BaseModel implements
    ManageDispatchVehicleContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public ManageDispatchVehicleModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseRowJson<ApproveOrder>> getApproveOrder(String s, int page,
      int rows) {
    FormBody formBody = new FormBody.Builder()
        .add("passenger", s)
        .add("page", String.valueOf(page))
        .add("rows", String.valueOf(rows))
        .add("type", "2")
        .add("isDeal", "0")
        .add("orderBy", "asc")

        .add("orderCol", "beginTime")
        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).getApproveOrder(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), formBody);
  }

}