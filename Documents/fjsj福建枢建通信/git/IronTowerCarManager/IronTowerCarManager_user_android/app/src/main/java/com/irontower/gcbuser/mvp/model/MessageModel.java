package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.MessageContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.Message;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class MessageModel extends BaseModel implements MessageContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public MessageModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
  public Observable<BaseRowJson<Message>> getMessages(int currentPage, int i) {
    FormBody formBody = new FormBody.Builder().
        add("state", "2")
        .add("page", String.valueOf(currentPage))
        .add("rows", "7").build();
    return mRepositoryManager.obtainRetrofitService(CommonService.class).getMessages(
        SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), formBody);
  }

}