package com.irontower.gcbuser.mvp.presenter;

import android.app.Application;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.irontower.gcbuser.mvp.contract.UserCarTimeContract;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.ChartData;
import com.irontower.gcbuser.mvp.ui.adapter.UseCarTimeAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class UserCarTimePresenter extends
    BasePresenter<UserCarTimeContract.Model, UserCarTimeContract.View> {

  private RxErrorHandler mErrorHandler;
  private Application mApplication;
  private Gson mGson;
  private UseCarTimeAdapter mAdapter;


  @Inject
  public UserCarTimePresenter(UserCarTimeContract.Model model, UserCarTimeContract.View rootView
      , RxErrorHandler handler, Application application,
      UseCarTimeAdapter adapter, Gson gosn) {
    super(model, rootView);
    this.mErrorHandler = handler;
    this.mApplication = application;
    this.mAdapter = adapter;
    this.mGson = gosn;

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;

    this.mApplication = null;
  }

  public void getChartData() {
    mModel.countApplyOrderHalfYear().subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Map<String, Object>>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Map<String, Object>> mapBaseJson) {
            if (mapBaseJson.isSuccess()) {
              List<ChartData> shortData = new ArrayList<>();
              List<ChartData> longData = new ArrayList<>();
              for (JsonElement jsonElement : new JsonParser()
                  .parse(mGson.toJson(mapBaseJson.getData().get("shortDis"))).getAsJsonArray()) {
                shortData.add(mGson.fromJson(jsonElement, ChartData.class));
              }
              for (JsonElement jsonElement : new JsonParser()
                  .parse(mGson.toJson(mapBaseJson.getData().get("longDis"))).getAsJsonArray()) {
                longData.add(mGson.fromJson(jsonElement, ChartData.class));
              }
              for (ChartData shortDatum : shortData) {
                for (ChartData longDatum : longData) {
                  if (StringUtils.equals(shortDatum.getMonth(), longDatum.getMonth())) {
                    shortDatum.setNum1(longDatum.getNum());
                  }
                }
              }
              mAdapter.setNewData(shortData);
              mRootView.initChartData(shortData, longData);


            } else {

              ArmsUtils
                  .makeText(mApplication, mapBaseJson.getMessage());
            }
          }

          @Override
          public void onError(Throwable t) {
            super.onError(t);

          }
        });//
  }

}
