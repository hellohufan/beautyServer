package com.irontower.gcbdriver.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbdriver.mvp.contract.OrderDetailContract;
import com.irontower.gcbdriver.mvp.model.api.service.CommonService;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import java.util.Map;
import javax.inject.Inject;


@ActivityScope
public class OrderDetailModel extends BaseModel implements OrderDetailContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public OrderDetailModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseJson<Map<String, Object>>> getOrderDetail(String orderCarNo) {
    return mRepositoryManager.obtainRetrofitService(CommonService.class).getOrderDetail(
        SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), orderCarNo);
  }

  @Override
  public Observable<BaseJson<Object>> takeOrder(String orderCarNo) {

    return mRepositoryManager.obtainRetrofitService(CommonService.class).takeOrder(
        SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), "5", orderCarNo);
  }
}