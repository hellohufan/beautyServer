package com.irontower.gcbdriver.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbdriver.mvp.contract.ApplyFeeDetailContract;
import com.irontower.gcbdriver.mvp.model.api.service.CommonService;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.irontower.gcbdriver.mvp.model.entity.Report;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class ApplyFeeDetailModel extends BaseModel implements ApplyFeeDetailContract.Model {

  @Inject
  Gson mGson;
  @Inject
  Application mApplication;

  @Inject
  public ApplyFeeDetailModel(IRepositoryManager repositoryManager) {
    super(repositoryManager);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mGson = null;
    this.mApplication = null;
  }

  @Override
  public Observable<BaseJson<Object>> submit(String orderCarNo, String toll, String park,
      String rest, String mile, String minutes, String gasCost, String otherCost) {
    Report report = new Report();
    report.setOrderCarNo(orderCarNo);
    report.setToll(toll);
    report.setPark(park);
    report.setRest(rest);
    report.setMile(mile);
    report.setMinutes(minutes);
    report.setGasCost(gasCost);
    report.setOtherCost(otherCost);
    FormBody body = new FormBody.Builder()
        .add("orderCarNo", orderCarNo)
        .add("report", mGson.toJson(report))

        .build();
    return mRepositoryManager.obtainRetrofitService(CommonService.class).submitReport(
        SPUtils.getInstance().getString(CommonService.ACCESS_TOKEN), body);
  }
}