package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.GoingOrderContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.Order;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class GoingOrderModel extends BaseModel implements GoingOrderContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public GoingOrderModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
        .add("page", String.valueOf(page))
        .add("rows", String.valueOf(rows))
        .add("states", "1")

        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).getOrders(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), body);
  }

  @Override
  public Observable<BaseJson<Object>> delOrder(String orderNo) {
    return mRepositoryManager.obtainRetrofitService(CommonService.class).delOrder(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), orderNo);
  }
}