package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.ManyAttachmentContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import javax.inject.Inject;
import okhttp3.ResponseBody;


@ActivityScope
public class ManyAttachmentModel extends BaseModel implements ManyAttachmentContract.Model {

  @Inject
  Gson mGson;
  @Inject
  Application mApplication;

  @Inject
  public ManyAttachmentModel(IRepositoryManager repositoryManager) {
    super(repositoryManager);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mGson = null;
    this.mApplication = null;
  }

  @Override
  public Observable<ResponseBody> getFile(String url) {
    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .downloadFileWithDynamicUrlSync(
            url);
  }
}