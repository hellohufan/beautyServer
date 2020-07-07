package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.TraceViewContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.Track;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import javax.inject.Inject;


/**
 * ================================================ Description:
 * <p>
 * Created by MVPArmsTemplate on 06/12/2019 22:13
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class TraceViewModel extends BaseModel implements TraceViewContract.Model {

  @Inject
  Gson mGson;
  @Inject
  Application mApplication;

  @Inject
  public TraceViewModel(IRepositoryManager repositoryManager) {
    super(repositoryManager);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mGson = null;
    this.mApplication = null;
  }

  @Override
  public Observable<BaseRowJson<Track>> getData(String carId, String carNo,
      CharSequence bTime, CharSequence eTime) {
//    return mRepositoryManager.obtainRetrofitService(CommonService.class).getTrackData(
//        SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), true, 5000, "167", "闽AG1536(014749545215)",
//        "2019-06-15 00:00", "2019-06-15 23:59");
    return mRepositoryManager.obtainRetrofitService(CommonService.class).getTrackData(
        SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), true, 5000, carId, carNo,
        bTime.toString(), eTime.toString());
  }

}