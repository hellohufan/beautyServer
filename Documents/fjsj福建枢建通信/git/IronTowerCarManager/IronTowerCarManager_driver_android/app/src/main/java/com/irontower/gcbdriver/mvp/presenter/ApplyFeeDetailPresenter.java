package com.irontower.gcbdriver.mvp.presenter;

import android.app.Application;
import com.irontower.gcbdriver.app.EventBusTags;
import com.irontower.gcbdriver.mvp.contract.ApplyFeeDetailContract;
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
public class ApplyFeeDetailPresenter extends
    BasePresenter<ApplyFeeDetailContract.Model, ApplyFeeDetailContract.View> {

  @Inject
  RxErrorHandler mErrorHandler;
  @Inject
  Application mApplication;
  @Inject
  ImageLoader mImageLoader;
  @Inject
  AppManager mAppManager;

  @Inject
  public ApplyFeeDetailPresenter(ApplyFeeDetailContract.Model model,
      ApplyFeeDetailContract.View rootView) {
    super(model, rootView);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;
    this.mAppManager = null;
    this.mImageLoader = null;
    this.mApplication = null;
  }

  public void submit(String orderCarNo, String toll, String park, String rest, String mile,
      String minutes, String gasCost, String otherCost) {

    mModel.submit(orderCarNo, toll, park, rest, mile, minutes, gasCost, otherCost)
        .subscribeOn(Schedulers.io()).doOnSubscribe(disposable -> {
      mRootView.showLoading();
    })
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {

      mRootView.hideLoading();

    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson>(mErrorHandler) {
          @Override
          public void onNext(BaseJson stringBaseJson) {
            if (stringBaseJson.isSuccess()) {
              EventBus.getDefault().post("1", EventBusTags.SUBMITREPORT);
              mRootView.killMyself();
              ArmsUtils.makeText(mRootView.getActivity(), "成功提交填报费用!");
            } else {
              ArmsUtils.makeText(mRootView.getActivity(), stringBaseJson.getMessage());

            }
          }

          @Override
          public void onError(Throwable t) {
            super.onError(t);

          }
        });

  }
}
