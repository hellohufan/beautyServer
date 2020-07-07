package com.irontower.gcbdriver.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbdriver.mvp.contract.ApplyBegOffDetailContract;
import com.irontower.gcbdriver.mvp.model.api.service.CommonService;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import java.util.Map;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class ApplyBegOffDetailModel extends BaseModel implements ApplyBegOffDetailContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public ApplyBegOffDetailModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseJson<Map<String, Object>>> applyBegOff(String beginTime, String endTime,
      String type, String reason, String submit) {
    FormBody body = new FormBody.Builder()
        .add("beginTime", beginTime)

        .add("endTime", endTime)
        .add("type", type)

        .add("reason", reason)

        .add("submit", submit)

        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).applyBegOff(body,
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN));
  }
}