package com.irontower.gcbdriver.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbdriver.mvp.contract.ApplyBegOffContract;
import com.irontower.gcbdriver.mvp.model.api.service.CommonService;
import com.irontower.gcbdriver.mvp.model.entity.BaseRowJson;
import com.irontower.gcbdriver.mvp.model.entity.BegOff;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class ApplyBegOffModel extends BaseModel implements ApplyBegOffContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public ApplyBegOffModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseRowJson<BegOff>> getBegOff(int mNextRequestPage, int rows) {
    FormBody body = new FormBody.Builder()
        .add("page", String.valueOf(mNextRequestPage))

        .add("rows", String.valueOf(rows))

        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).getBegOff(body,
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN));
  }
}