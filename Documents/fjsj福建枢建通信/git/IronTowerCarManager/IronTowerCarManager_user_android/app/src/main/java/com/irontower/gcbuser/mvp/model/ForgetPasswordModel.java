package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.ForgetPasswordContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import java.util.Map;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class ForgetPasswordModel extends BaseModel implements ForgetPasswordContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public ForgetPasswordModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseJson<Map<String, Object>>> getSMS(String phone) {
    FormBody body = new FormBody.Builder()
        .add("userNo", phone)

        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).getSMS(body);
  }

  @Override
  public Observable<BaseJson<Integer>> resetPwd(String userNo,
      String vcode, String newPwd) {
    FormBody body = new FormBody.Builder()
        .add("userNo", userNo)
        .add("vcode", vcode)

        .add("newPwd", newPwd)

        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).resetPwd(body);
  }

}