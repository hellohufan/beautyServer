package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.DispatchCarHistoryDetailContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.FeeType;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import java.util.Map;
import javax.inject.Inject;
import okhttp3.FormBody;
import okhttp3.ResponseBody;


@ActivityScope
public class DispatchCarHistoryDetailModel extends BaseModel implements
    DispatchCarHistoryDetailContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public DispatchCarHistoryDetailModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseJson<Map<String, Object>>> getOrderRecordDetail(String orderNo) {
    FormBody body = new FormBody.Builder()
        .add("orderNo", orderNo)
        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).getOrderRecordDetail(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), body);
  }

  @Override
  public Observable<BaseJson<Object>> delDispatchCar(String orderCarNo) {
    FormBody formBody = new FormBody.Builder()
        .add("orderCarNo", orderCarNo).build();
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .delDispatchCar(SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), formBody);
  }

  @Override
  public Observable<BaseJson<Map<String, Object>>> getTodoOrderDetail(String todoId) {
    return mRepositoryManager.obtainRetrofitService(CommonService.class).getTodoOrderDetail(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), todoId);
  }

  @Override
  public Observable<BaseJson<Map<String, Object>>> cancelUseCar(String orderCarNo, String remark) {

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


  @Override
  public Observable<BaseRowJson<FeeType>> getFeetTypes(String orgId) {
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .getFeetTypes(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), orgId);
  }

  @Override
  public Observable<BaseJson<Object>> rejectUseCar(String s, String todoId) {
    FormBody formBody = new FormBody.Builder()
        .add("remark", s)
        .add("todoId", todoId).build();
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .rejectUseCar(SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), formBody);
  }

}