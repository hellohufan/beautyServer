package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.AttachmentContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import okhttp3.MediaType;
import okhttp3.RequestBody;


@ActivityScope
public class AttachmentModel extends BaseModel implements AttachmentContract.Model {

  @Inject
  Gson mGson;
  @Inject
  Application mApplication;

  @Inject
  public AttachmentModel(IRepositoryManager repositoryManager) {
    super(repositoryManager);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mGson = null;
    this.mApplication = null;
  }

  @Override
  public Observable<BaseJson<List<Attach>>> uploadFile(Map<String, Object> fileMap) {
    Map<String, RequestBody> params = new HashMap<>();
    RequestBody body = RequestBody.create(MediaType.parse("text/plain"), "order");
    params.put("refType", body);
    for (Entry<String, Object> fileEntry : fileMap.entrySet()) {
      File file = new File(fileEntry.getKey());
      RequestBody body1 = RequestBody
          .create(MediaType.parse("multipart/form-data"), file);
      params.put("files" + "\"; filename=\"" + file.getName() + "", body1);

    }

    return mRepositoryManager.obtainRetrofitService(CommonService.class)
        .uploadFile(SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), params);
  }
}