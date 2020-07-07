package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.GoBackRecordContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.BegGoOut;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class GoBackRecordModel extends BaseModel implements GoBackRecordContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public GoBackRecordModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseRowJson<BegGoOut>> getGoBack(int currentPage, int i) {
    FormBody body = new FormBody.Builder()
        .add("page", String.valueOf(currentPage))
        .add("rows", String.valueOf(i))
        .add("queryType", "4")
        .add("check", "check")

        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).getGoOuts(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), body);
  }
}