package com.irontower.gcbdriver.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbdriver.mvp.contract.MessageDetailContract;
import com.irontower.gcbdriver.mvp.model.api.service.CommonService;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.irontower.gcbdriver.mvp.model.entity.Message;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class MessageDetailModel extends BaseModel implements MessageDetailContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public MessageDetailModel(IRepositoryManager repositoryManager, Gson gson,
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
  public io.reactivex.Observable<BaseJson<Message>> getMessageDetail(String eid) {
    FormBody formBody = new FormBody.Builder()
        .add("eid", eid).build();
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .getMessageDetail(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), formBody);
  }
}