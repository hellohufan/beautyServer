package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.mvp.contract.MainContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.BaseSimpleJson;
import com.irontower.gcbuser.mvp.model.entity.CarUse;
import com.irontower.gcbuser.mvp.model.entity.Version;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import java.io.File;
import java.util.Map;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class MainModel extends BaseModel implements MainContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public MainModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
  public Observable<BaseJson<Map<String, Object>>> logout() {

    return mRepositoryManager.obtainRetrofitService(CommonService.class).logout(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN));
  }

  @Override
  public Observable<BaseJson<Map<String, Object>>> getDictList() {
    FormBody body = new FormBody.Builder()
        .add("keyArr",
            "DICT_CAR_TYPE,DICT_ORDER_STATE,DICT_CAR_USE_STATE,DICT_DRIVER_STATE,DICT_CAR_OUT_TYPE")
        .build();
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .getDictList(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), body);
  }

  @Override
  public Observable<BaseJson<Map<String, Object>>> uploadImage(File file) {
    FormBody body = new FormBody.Builder()
        .add("headImage", AppUtil.encodeImage(file.getAbsolutePath()))
        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .uploadImage(body, SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN));
  }

  @Override
  public Observable<BaseJson<Version>> getAppVersionInfo() {
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .getAppVersionInfo(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN),
            "versionUser");
  }

  @Override
  public Observable<BaseSimpleJson<Map<String, Object>>> isUrgentUser() {
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .isUrgentUser(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN));
  }

  @Override
  public Observable<BaseRowJson<CarUse>> getCarUses() {
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .getCarUses(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN));
  }
}