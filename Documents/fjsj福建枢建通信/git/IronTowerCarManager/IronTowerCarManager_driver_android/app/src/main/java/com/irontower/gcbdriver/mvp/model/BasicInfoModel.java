package com.irontower.gcbdriver.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbdriver.app.utils.AppUtil;
import com.irontower.gcbdriver.mvp.contract.BasicInfoContract;
import com.irontower.gcbdriver.mvp.model.api.service.CommonService;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.irontower.gcbdriver.mvp.model.entity.Version;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import java.io.File;
import java.util.Map;
import javax.inject.Inject;
import okhttp3.FormBody;
import timber.log.Timber;


@ActivityScope
public class BasicInfoModel extends BaseModel implements BasicInfoContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public BasicInfoModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
  public Observable<BaseJson<Map<String, Object>>> getBasicInfo(String userNo, String userName,
      String phoneNo) {
//    FormBody body = new FormBody.Builder()
//        .add("userNo", userNo)
//        .add("userName", userName)
//        .add("phoneNo", phoneNo)
//        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .getBasicInfo(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), userNo, userName,
            phoneNo);
  }


  @Override
  public Observable<BaseJson<Map<String, Object>>> uploadImage(File file) {
    Timber.w(file.getName());
    FormBody body = new FormBody.Builder()
        .add("headImage", AppUtil.encodeImage(file.getAbsolutePath()))
        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .uploadImage(body, SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN));
  }

  @Override
  public Observable<BaseJson<Version>> getAppVersionInfo() {

    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .getAppVersionInfo(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN));
  }
}