package com.irontower.gcbdriver.mvp.presenter;

import android.app.Application;
import com.irontower.gcbdriver.mvp.contract.PayBackTrackContract;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.irontower.gcbdriver.mvp.model.entity.Track;
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


@ActivityScope
public class PayBackTrackPresenter extends
    BasePresenter<PayBackTrackContract.Model, PayBackTrackContract.View> {

  private RxErrorHandler mErrorHandler;
  private Application mApplication;
  private ImageLoader mImageLoader;
  private AppManager mAppManager;

  @Inject
  public PayBackTrackPresenter(PayBackTrackContract.Model model, PayBackTrackContract.View rootView
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

  public void getOrderTrack(String orderCarNo) {
    mModel.getOrderTrack(orderCarNo).subscribeOn(Schedulers.io()).doOnSubscribe(disposable -> {
      mRootView.showLoading();


    })
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {

      mRootView.hideLoading();


    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<List<Track>>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<List<Track>> stringBaseJson) {

            if (stringBaseJson.isSuccess()) {

              mRootView.initLine(stringBaseJson.getData());


            } else {
              ArmsUtils.makeText(mRootView.getActivity(), stringBaseJson.getMessage());
            }
          }


        });

  }
}
