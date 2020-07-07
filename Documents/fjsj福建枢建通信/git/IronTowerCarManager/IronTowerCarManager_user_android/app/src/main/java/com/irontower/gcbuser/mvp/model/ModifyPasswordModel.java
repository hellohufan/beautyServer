package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.ModifyPasswordContract;
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
public class ModifyPasswordModel extends BaseModel implements ModifyPasswordContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public ModifyPasswordModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseJson<Map<String, Object>>> modifyPwd(String oldPwd, String newPwd) {
    FormBody body = new FormBody.Builder()
        .add("oldPwd", EncryptUtils.encryptMD5ToString(oldPwd).toLowerCase())
        .add("newPwd", newPwd)
        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).modifyPwd(
        SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), body);
  }

}