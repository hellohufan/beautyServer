package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.ApproveDetailContract;
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
public class ApproveDetailModel extends BaseModel implements ApproveDetailContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public ApproveDetailModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseJson<Map<String, Object>>> approveOrder(String todoId, String status,
      String remark) {

    FormBody formBody = new FormBody.Builder()
        .add("todoId", todoId)
        .add("status", status)
        .add("remark", remark)
        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).approveOrder(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), formBody);
  }

  @Override
  public Observable<BaseJson<Map<String, Object>>> getTodoOrderDetail(String todoId) {
    return mRepositoryManager.obtainRetrofitService(CommonService.class).getTodoOrderDetail(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), todoId);
  }

  @Override
  public Observable<ResponseBody> getFile(String url) {
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .downloadFileWithDynamicUrlSync(
            url);
  }
}