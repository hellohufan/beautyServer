package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.OrderCarDetailContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import java.util.Map;
import javax.inject.Inject;
import okhttp3.FormBody;
import okhttp3.ResponseBody;


@ActivityScope
public class OrderCarDetailModel extends BaseModel implements OrderCarDetailContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public OrderCarDetailModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseJson<Map<String, Object>>> cancelOrderCar(String orderCarNo,
      String remark) {
    FormBody body = new FormBody.Builder()
        .add("orderCarNo", orderCarNo)
        .add("remark", remark)

        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).cancelOrderCar(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), body);
  }


  @Override
  public Observable<ResponseBody> getFile(String url) {
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .downloadFileWithDynamicUrlSync(
            url);
  }

}