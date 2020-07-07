package com.irontower.gcbuser.mvp.presenter;

import android.app.Application;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.mvp.contract.RentVehicleContract;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.CarCenter;
import com.irontower.gcbuser.mvp.model.entity.FeeType;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import org.simple.eventbus.EventBus;


@ActivityScope
public class RentVehiclePresenter extends
    BasePresenter<RentVehicleContract.Model, RentVehicleContract.View> {

  private RxErrorHandler mErrorHandler;
  private Application mApplication;
  private ImageLoader mImageLoader;
  private AppManager mAppManager;

  @Inject
  public RentVehiclePresenter(RentVehicleContract.Model model, RentVehicleContract.View rootView
      , RxErrorHandler handler, Application application
      , ImageLoader imageLoader, AppManager appManager) {
    super(model, rootView);
    this.mErrorHandler = handler;
    this.mApplication = application;
    this.mImageLoader = imageLoader;
    this.mAppManager = appManager;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;
    this.mAppManager = null;
    this.mImageLoader = null;
    this.mApplication = null;
  }

  public void getCarCenter(boolean rent) {
    mModel.getCarCenters(rent).subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<List<CarCenter>>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<List<CarCenter>> stringBaseJson) {
            if (stringBaseJson.isSuccess()) {
              mRootView.init(stringBaseJson.getData());

            } else {

              ArmsUtils
                  .makeText(mRootView.getActivity(), stringBaseJson.getMessage());
            }
          }


        });

  }

  public void getFeeTypes(Integer orgId) {
    mModel.getFeetTypes(String.valueOf(orgId)).subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseRowJson<FeeType>>(mErrorHandler) {
          @Override
          public void onNext(BaseRowJson<FeeType> stringBaseJson) {
            if (stringBaseJson.isSuccess()) {
              mRootView.initFeeTypes(stringBaseJson.getRows());

            } else {

              ArmsUtils
                  .makeText(mRootView.getActivity(), stringBaseJson.getMessage());
            }
          }


        });
  }

  public void submitRentCar(String todoId, String carType, String orderCarNo, String orgId,
      String feeId) {
    mModel.submitRentCar(todoId, carType, orderCarNo, orgId, feeId).subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Object>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Object> stringBaseJson) {
            if (stringBaseJson.isSuccess()) {

              EventBus.getDefault().post("1", EventBusTags.RENT_CAR_SUCCESS);


            } else {

              ArmsUtils
                  .makeText(mRootView.getActivity(), stringBaseJson.getMessage());
            }
          }


        });
  }
}
