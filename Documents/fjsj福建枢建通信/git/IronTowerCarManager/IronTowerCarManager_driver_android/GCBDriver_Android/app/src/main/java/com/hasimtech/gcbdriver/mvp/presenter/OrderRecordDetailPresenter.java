package com.irontower.gcbdriver.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.OrderRecordDetailContract;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.irontower.gcbdriver.mvp.model.entity.Evaluate;
import com.irontower.gcbdriver.mvp.model.entity.Order;
import com.irontower.gcbdriver.mvp.model.entity.OrderCar;
import com.irontower.gcbdriver.mvp.model.entity.OrderTrack;
import com.irontower.gcbdriver.mvp.model.entity.Report;
import com.irontower.gcbdriver.mvp.ui.adapter.TimeLineAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
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
public class OrderRecordDetailPresenter extends
    BasePresenter<OrderRecordDetailContract.Model, OrderRecordDetailContract.View> {

  private RxErrorHandler mErrorHandler;
  private Application mApplication;
  private Gson mGson;
  private TimeLineAdapter mTimeLineAdapter;

  @Inject
  public OrderRecordDetailPresenter(OrderRecordDetailContract.Model model,
      OrderRecordDetailContract.View rootView
      , RxErrorHandler handler, Application application, Gson gson,
      TimeLineAdapter timeLineAdapter) {
    super(model, rootView);
    this.mErrorHandler = handler;
    this.mApplication = application;
    this.mGson = gson;
    this.mTimeLineAdapter = timeLineAdapter;

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;
    this.mGson = null;
    this.mTimeLineAdapter = null;
    this.mApplication = null;
  }


  public void call(String phone) {
    PermissionUtil.callPhone(new PermissionUtil.RequestPermission() {
      @Override
      public void onRequestPermissionSuccess() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        mRootView.launchActivity(intent);

      }

      @Override
      public void onRequestPermissionFailure(List<String> permissions) {
        ArmsUtils.makeText(mRootView.getActivity(),
            ArmsUtils.getString(mApplication, R.string.no_permission));
      }

      @Override
      public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
        ArmsUtils.makeText(mRootView.getActivity(),
            ArmsUtils.getString(mApplication, R.string.no_permission));
      }
    }, mRootView.getRxPermissions(), mErrorHandler);

  }

  public void getData(String orderCarNo) {
    mModel.getOrderDetail(orderCarNo).subscribeOn(Schedulers.io()).doOnSubscribe(disposable -> {
      mRootView.showLoading();


    })
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {

      mRootView.hideLoading();


    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Map<String, Object>>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Map<String, Object>> stringBaseJson) {

            if (stringBaseJson.isSuccess()) {
              List<OrderCar> orderCars = new ArrayList<>();
              List<OrderTrack> orderTracks = new ArrayList<>();

              Order order = mGson
                  .fromJson(mGson.toJson(stringBaseJson.getData().get("order")), Order.class);

              Evaluate evaluate = mGson
                  .fromJson(mGson.toJson(stringBaseJson.getData().get("valuate")), Evaluate.class);
              Report report;
              if (stringBaseJson.getData().get("report") == null) {
                report = null;
              } else {
                report = mGson
                    .fromJson(stringBaseJson.getData().get("report").toString(), Report.class);
              }

              for (JsonElement jsonElement : new JsonParser()
                  .parse(mGson.toJson(stringBaseJson.getData().get("orderCars")))
                  .getAsJsonArray()) {
                orderCars.add(mGson.fromJson(jsonElement, OrderCar.class));
              }
              for (JsonElement jsonElement : new JsonParser()
                  .parse(mGson.toJson(stringBaseJson.getData().get("orderTracks")))
                  .getAsJsonArray()) {
                orderTracks.add(mGson.fromJson(jsonElement, OrderTrack.class));
              }
              String node = null;
              if (orderTracks.size() > 0) {
                node = orderTracks.get(0).getLevelName();
              }

              mRootView.init(order, orderCars, node, evaluate, orderTracks, report);
              mTimeLineAdapter.setNewData(orderTracks);


            }
          }

          @Override
          public void onError(Throwable t) {
            super.onError(t);

          }
        });

  }

  public void callPhone(String phone) {
    PermissionUtil.callPhone(new PermissionUtil.RequestPermission() {
      @Override
      public void onRequestPermissionSuccess() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        mRootView.launchActivity(intent);

      }

      @Override
      public void onRequestPermissionFailure(List<String> permissions) {
        ArmsUtils.makeText(mRootView.getActivity(),
            ArmsUtils.getString(mApplication, R.string.no_permission));
      }

      @Override
      public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
        ArmsUtils.makeText(mRootView.getActivity(),
            ArmsUtils.getString(mApplication, R.string.no_permission));
      }
    }, mRootView.getRxPermissions(), mErrorHandler);

  }

}
