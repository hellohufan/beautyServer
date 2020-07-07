package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.ApproveBegOffContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.BegOff;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class ApproveBegOffModel extends BaseModel implements ApproveBegOffContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public ApproveBegOffModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseRowJson<BegOff>> getBegOffs(int page, int rows) {
    FormBody body = new FormBody.Builder()
        .add("isApproval", "true")
        .add("state", "1")
        .add("page", String.valueOf(page))

        .add("rows", String.valueOf(rows))

        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).getBegOffs(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), body);
  }

  @Override
  public Observable<BaseJson<Object>> approvalBegOff(String s, Integer leaveId,
      String state) {
    FormBody body = new FormBody.Builder()
        .add("leaveId", String.valueOf(leaveId))

        .add("state", state)

        .add("remark", s)

        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).approvalBegOff(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), body);
  }
}