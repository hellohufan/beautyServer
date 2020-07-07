package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.mvp.contract.OrderCarContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.SubmitOrder;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class OrderCarModel extends BaseModel implements OrderCarContract.Model {

  private Gson mGson;
  private Application mApplication;

  @Inject
  public OrderCarModel(IRepositoryManager repositoryManager, Gson gson, Application application) {
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
  public Observable<BaseJson<Map<String, Object>>> submitOrder(SubmitOrder submitOrder,
      List<Attach> attachList) {
    StringBuilder attachIds = new StringBuilder();
    if (attachList.size() != 0) {
      for (int i = 0; i < attachList.size(); i++) {

        if (i != 0) {
          attachIds.append(",");
        }
        attachIds.append(attachList.get(i).getAttachId());
      }
    }
    FormBody.Builder builder = new FormBody.Builder()
        .add("passenger", submitOrder.getPassenger())
        .add("passengerTel", submitOrder.getPassengerTel())
        .add("passengerNum", submitOrder.getPassengerNum())
        .add("tripType", submitOrder.getTripType())

        .add("beginTime", submitOrder.getBeginTime().length() > 16 ? submitOrder.getBeginTime()
            : submitOrder.getBeginTime() + ":00")
        .add("endTime", submitOrder.getEndTime().length() > 16 ? submitOrder.getEndTime()
            : submitOrder.getEndTime() + ":00")
        .add("useId", String.valueOf(submitOrder.getUseId()))
        .add("distanceType", String.valueOf(submitOrder.getDistanceType()))
        .add("beginAddr", submitOrder.getBeginAddr())
        .add("endAddr", submitOrder.getEndAddr())
        .add("beginGps", submitOrder.getBeginGps())
        .add("endGps", submitOrder.getEndGps())
        .add("remark", submitOrder.getRemark())
        .add("accompany", submitOrder.getAccompany())

        .add("orderReason", submitOrder.getOrderReason())

        .add("submit", "true");
    if (!StringUtils.isEmpty(attachIds)) {
      builder.add("attachIds", attachIds.toString());
    }

    return mRepositoryManager.obtainRetrofitService(CommonService.class).submitOrder(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), builder.build());
  }

  @Override
  public Observable<BaseJson<Map<String, Object>>> updateOrder(String orderNo,
      SubmitOrder submitOrder,
      List<Attach> attachList) {
    StringBuilder attachIds = new StringBuilder();
    if (attachList.size() != 0) {
      for (int i = 0; i < attachList.size(); i++) {

        if (i != 0) {
          attachIds.append(",");
        }
        attachIds.append(attachList.get(i).getAttachId());
      }
    }
    FormBody.Builder builder = new FormBody.Builder()
        .add("orderNo", orderNo)
        .add("passenger", submitOrder.getPassenger())
        .add("passengerTel", submitOrder.getPassengerTel())
        .add("passengerNum", submitOrder.getPassengerNum())
        .add("tripType", submitOrder.getTripType())

        .add("beginTime", submitOrder.getBeginTime().length() > 16 ? submitOrder.getBeginTime()
            : submitOrder.getBeginTime() + ":00")
        .add("endTime", submitOrder.getEndTime().length() > 16 ? submitOrder.getEndTime()
            : submitOrder.getEndTime() + ":00")
        .add("useId", String.valueOf(submitOrder.getUseId()))
        .add("distanceType", String.valueOf(submitOrder.getDistanceType()))
        .add("beginAddr", submitOrder.getBeginAddr())
        .add("endAddr", submitOrder.getEndAddr())
        .add("beginGps", submitOrder.getBeginGps())
        .add("endGps", submitOrder.getEndGps())
        .add("remark", submitOrder.getRemark())
        .add("accompany", submitOrder.getAccompany())

        .add("orderReason", submitOrder.getOrderReason())

        .add("submit", "true");
    if (!StringUtils.isEmpty(attachIds)) {
      builder.add("attachIds", attachIds.toString());
    }

    return mRepositoryManager.obtainRetrofitService(CommonService.class).updateOrder(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), builder.build());
  }
}