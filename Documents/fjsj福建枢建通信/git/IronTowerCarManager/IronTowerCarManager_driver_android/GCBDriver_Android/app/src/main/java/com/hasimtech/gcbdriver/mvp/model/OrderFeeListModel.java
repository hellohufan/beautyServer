package com.irontower.gcbdriver.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbdriver.mvp.contract.OrderFeeListContract;
import com.irontower.gcbdriver.mvp.model.api.service.CommonService;
import com.irontower.gcbdriver.mvp.model.entity.BaseRowJson;
import com.irontower.gcbdriver.mvp.model.entity.Order;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class OrderFeeListModel extends BaseModel implements OrderFeeListContract.Model {

  @Inject
  Gson mGson;
  @Inject
  Application mApplication;

  @Inject
  public OrderFeeListModel(IRepositoryManager repositoryManager) {
    super(repositoryManager);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mGson = null;
    this.mApplication = null;
  }

  @Override
  public Observable<BaseRowJson<Order>> getFeeOrders(int page, int rows) {
    FormBody body = new FormBody.Builder()
        .add("page", String.valueOf(page))

        .add("rows", String.valueOf(rows))

        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).getFeeOrders(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), body);
  }

}