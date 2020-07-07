package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.DispatchVehicleHistoryContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.ApproveOrder;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import java.util.Calendar;
import java.util.Date;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class DispatchVehicleHistoryModel extends BaseModel implements
    DispatchVehicleHistoryContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public DispatchVehicleHistoryModel(IRepositoryManager repositoryManager, Gson gson,
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
  public Observable<BaseRowJson<ApproveOrder>> getApproveOrder(String s, int page,
      int rows, String orderBy, String orderCol, String beginCreateTime,
      String endCreateTIme, String beginTime, String endTime) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date());
    calendar.set(Calendar.HOUR_OF_DAY, 23);
    calendar.set(Calendar.MINUTE, 59);
    calendar.set(Calendar.SECOND, 59);
    FormBody formBody = new FormBody.Builder()
        .add("passenger", s)
        .add("rows", String.valueOf(rows))
        .add("page", String.valueOf(page))
        .add("type", "2")
        .add("isDeal", "1")
        .add("orderBy", orderBy)

        .add("orderCol", orderCol)

        .add("beginCreateTime",
            beginCreateTime.trim() + " 00:00:00")
        .add("endCreateTime", endCreateTIme.trim() + " 23:59:59")
        .add("beginTime", !StringUtils.isEmpty(beginTime) ? beginTime + " 00:00:00" : "")

        .add("endTime", !StringUtils.isEmpty(endTime) ? endTime + " 23:59:59" : "")
        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).getApproveOrder(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), formBody);
  }

}