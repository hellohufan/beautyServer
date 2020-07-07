package com.irontower.gcbdriver.mvp.presenter;

import android.content.Intent;
import android.net.Uri;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.app.EventBusTags;
import com.irontower.gcbdriver.mvp.contract.TaskDetailContract;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.irontower.gcbdriver.mvp.model.entity.Order;
import com.irontower.gcbdriver.mvp.model.entity.OrderCar;
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
import org.simple.eventbus.EventBus;


@ActivityScope
public class TaskDetailPresenter extends
    BasePresenter<TaskDetailContract.Model, TaskDetailContract.View> {

  private RxErrorHandler mErrorHandler;
  private Gson mGson;

  @Inject
  public TaskDetailPresenter(TaskDetailContract.Model model, TaskDetailContract.View rootView
      , RxErrorHandler handler, Gson gson) {
    super(model, rootView);
    this.mErrorHandler = handler;
    this.mGson = gson;

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;
    this.mGson = null;

  }

  public void
  getData(String orderCarNo) {
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
              for (JsonElement jsonElement : new JsonParser()
                  .parse(mGson.toJson(stringBaseJson.getData().get("orderCars")))
                  .getAsJsonArray()) {
                orderCars.add(mGson.fromJson(jsonElement, OrderCar.class));
              }
              mRootView.init(mGson.fromJson(mGson.toJson(stringBaseJson.getData().get("order")),
                  Order.class), orderCars);


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
            ArmsUtils.getString(mRootView.getActivity(), R.string.no_permission));
      }

      @Override
      public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
        ArmsUtils.makeText(mRootView.getActivity(),
            ArmsUtils.getString(mRootView.getActivity(), R.string.no_permission));
      }
    }, mRootView.getRxPermissions(), mErrorHandler);


  }

  public void changeState(String orderCarNo, int state, String feeType) {
    int newState = state;
    if (state == 6 && feeType == null) {
      newState++;
    }
    newState++;
    int finalNewState = newState;
    mModel.takeOrder(orderCarNo, newState, feeType).subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> {
          mRootView.showLoading();
        })
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {

      mRootView.hideLoading();

    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Object>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson stringBaseJson) {

            if (stringBaseJson.getCode() == 0) {
              ArmsUtils.makeText(mRootView.getActivity(), "操作成功");
              if (state == 7 || (state == 6 && feeType == null)) {
                EventBus.getDefault().post("1", EventBusTags.END_ORDER);

              } else {
                mRootView.initState(finalNewState);
              }
            } else {
              ArmsUtils.makeText(mRootView.getActivity(), stringBaseJson.getMessage());
            }
          }


        });

  }
}
