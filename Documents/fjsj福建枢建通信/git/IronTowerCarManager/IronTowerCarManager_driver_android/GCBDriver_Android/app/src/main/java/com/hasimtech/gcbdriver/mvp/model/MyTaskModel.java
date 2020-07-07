package com.irontower.gcbdriver.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbdriver.mvp.contract.MyTaskContract;
import com.irontower.gcbdriver.mvp.model.api.service.CommonService;
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
public class MyTaskModel extends BaseModel implements MyTaskContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public MyTaskModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
        .add("state", OrderState.ONGOING)
        .add("page", String.valueOf(page))

        .add("rows", String.valueOf(rows))
        .add("orderCol", "beginTime")
        .add("orderBy", "asc")

        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .getOrders(SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), body);
  }


}