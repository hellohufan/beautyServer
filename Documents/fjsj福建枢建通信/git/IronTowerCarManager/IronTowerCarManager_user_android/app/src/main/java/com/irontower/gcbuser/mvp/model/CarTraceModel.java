package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.CarTraceContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;


/**
 * ================================================ Description:
 * <p>
 * Created by MVPArmsTemplate on 06/12/2019 10:53
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class CarTraceModel extends BaseModel implements CarTraceContract.Model {

  @Inject
  Gson mGson;
  @Inject
  Application mApplication;

  @Inject
  public CarTraceModel(IRepositoryManager repositoryManager) {
    super(repositoryManager);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mGson = null;
    this.mApplication = null;
  }

  @Override
  public Observable<List<Map<String, Object>>> getData() {
    return mRepositoryManager.obtainRetrofitService(CommonService.class).getTreeData(
        SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN));
  }

  @Override
  public Observable<List<Map<String, Object>>> loadTreeChildren(String id) {
    return mRepositoryManager.obtainRetrofitService(CommonService.class).loadTreeChildren(
        SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), id);
  }
}