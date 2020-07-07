package com.irontower.gcbdriver.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbdriver.mvp.contract.WaitingOrderContract;
import com.irontower.gcbdriver.mvp.model.api.service.CommonService;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.irontower.gcbdriver.mvp.model.entity.BaseRowJson;
import com.irontower.gcbdriver.mvp.model.entity.Order;
import com.irontower.gcbdriver.mvp.model.entity.OrderState;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class WaitingOrderModel extends BaseModel implements WaitingOrderContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public WaitingOrderModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseRowJson<Order>> getOrders(int page, int rows) {
    FormBody body = new FormBody.Builder()
        .add("state", OrderState.NOTSTART)
        .add("page", String.valueOf(page))

        .add("rows", String.valueOf(rows))
        .add("orderCol", "beginTime")
        .add("orderBy", "asc")
        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).getOrders(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), body);
  }

  @Override
  public Observable<BaseJson<Object>> takeOrder(String orderCarNo) {

    return mRepositoryManager.obtainRetrofitService(CommonService.class).takeOrder(
        SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), "5", orderCarNo);
  }
}