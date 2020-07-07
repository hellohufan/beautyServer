package com.irontower.gcbdriver.mvp.presenter;

import android.app.Application;
import com.irontower.gcbdriver.app.EventBusTags;
import com.irontower.gcbdriver.mvp.contract.ApplyGoOutDetailContract;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import org.simple.eventbus.EventBus;


@ActivityScope
public class ApplyGoOutDetailPresenter extends
    BasePresenter<ApplyGoOutDetailContract.Model, ApplyGoOutDetailContract.View> {

  private RxErrorHandler mErrorHandler;
  private Application mApplication;
  private ImageLoader mImageLoader;
  private AppManager mAppManager;

  @Inject
  public ApplyGoOutDetailPresenter(ApplyGoOutDetailContract.Model model,
      ApplyGoOutDetailContract.View rootView
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


  public void submit(String beginTime, String endTime, Integer carId, String dictType,
      String reason) {
    mModel.submitGoOut(String.valueOf(carId), dictType, beginTime, endTime, reason).subscribeOn(
        Schedulers.io()).doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Object>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Object> stringBaseJson) {
            if (stringBaseJson.isSuccess()) {
              ArmsUtils
                  .makeText(mRootView.getActivity(), stringBaseJson.getMessage());
              EventBus.getDefault().post("1", EventBusTags.APPLY_GO_OUT_SUCCESS);


            } else {

              ArmsUtils
                  .makeText(mRootView.getActivity(), stringBaseJson.getMessage());
            }
          }


        });//
  }
}
