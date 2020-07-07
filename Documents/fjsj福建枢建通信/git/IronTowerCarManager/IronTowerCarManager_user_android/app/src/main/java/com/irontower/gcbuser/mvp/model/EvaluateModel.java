package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;
import io.reactivex.Observable;
import javax.inject.Inject;

import com.irontower.gcbuser.mvp.contract.EvaluateContract;
import okhttp3.FormBody;


@ActivityScope
public class EvaluateModel extends BaseModel implements EvaluateContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public EvaluateModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
  public Observable<BaseJson<Object>> evaluate(String orderCarNo, int grade1, int grade2,
      int grade3, String s) {
    FormBody formBody = new FormBody.Builder()
        .add("refVal", orderCarNo)
        .add("grade1", String.valueOf(grade1))
        .add("grade2", String.valueOf(grade2))
        .add("grade3", String.valueOf(grade3))

        .add("idea", s).build();
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .evaluate(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), formBody);
  }


}