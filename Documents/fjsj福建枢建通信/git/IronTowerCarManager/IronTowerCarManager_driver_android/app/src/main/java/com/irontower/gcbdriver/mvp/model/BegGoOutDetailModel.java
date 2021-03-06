package com.irontower.gcbdriver.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbdriver.mvp.model.api.service.CommonService;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;
import io.reactivex.Observable;
import java.util.Map;
import javax.inject.Inject;

import com.irontower.gcbdriver.mvp.contract.BegGoOutDetailContract;
import okhttp3.FormBody;


@ActivityScope
public class BegGoOutDetailModel extends BaseModel implements BegGoOutDetailContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public BegGoOutDetailModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseJson<Map<String, Object>>> changeBegGoOut(String hid, String state) {
    FormBody formBody = new FormBody.Builder()
        .add("hid", hid)
        .add("operDesc", "操作")
        .add("state", state).build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .changeBegGoOut(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), formBody);
  }
}