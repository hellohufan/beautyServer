package com.irontower.gcbdriver.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbdriver.mvp.contract.ApplyGoOutDetailContract;
import com.irontower.gcbdriver.mvp.model.api.service.CommonService;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class ApplyGoOutDetailModel extends BaseModel implements ApplyGoOutDetailContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public ApplyGoOutDetailModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseJson<Object>> submitGoOut(String carId, String outType,
      String outTime, String inTime, String applyDesc) {
    FormBody formBody = new FormBody.Builder()
        .add("carId", carId)
        .add("outType", outType)
        .add("outTime", outTime + ":00")
        .add("inTime", inTime + ":00")
        .add("applyDesc", applyDesc).build();
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .submitGoOut(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), formBody);
  }
}