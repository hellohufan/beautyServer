package com.irontower.gcbdriver.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbdriver.mvp.contract.PayBackTrackContract;
import com.irontower.gcbdriver.mvp.model.api.service.CommonService;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.irontower.gcbdriver.mvp.model.entity.Track;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import java.util.List;
import javax.inject.Inject;


@ActivityScope
public class PayBackTrackModel extends BaseModel implements PayBackTrackContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public PayBackTrackModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseJson<List<Track>>> getOrderTrack(String orderCarNo) {
    return mRepositoryManager.obtainRetrofitService(CommonService.class).getOrderTrack(
        SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), orderCarNo);
  }
}