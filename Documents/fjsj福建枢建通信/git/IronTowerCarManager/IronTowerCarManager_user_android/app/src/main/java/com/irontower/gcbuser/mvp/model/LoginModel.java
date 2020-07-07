package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.LoginContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.Module;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class LoginModel extends BaseModel implements LoginContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public LoginModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
  public Observable<BaseJson<Map<String, Object>>> login(String userName, String password,
      String uuid) {
    FormBody body = new FormBody.Builder()
        .add("userNo", userName)
        .add("userPwd", password)
        .add("loginSrc", "2")
        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).login(body, uuid);
  }

  @Override
  public Observable<BaseJson<List<Module>>> getPermission() {
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .get(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN));
  }
}