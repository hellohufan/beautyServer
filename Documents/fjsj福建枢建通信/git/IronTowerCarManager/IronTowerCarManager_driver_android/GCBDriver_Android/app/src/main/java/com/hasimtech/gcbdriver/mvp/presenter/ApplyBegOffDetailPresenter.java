package com.irontower.gcbdriver.mvp.presenter;

import android.app.Application;
import com.irontower.gcbdriver.app.EventBusTags;
import com.irontower.gcbdriver.mvp.contract.ApplyBegOffDetailContract;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.Map;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import org.simple.eventbus.EventBus;


@ActivityScope
public class ApplyBegOffDetailPresenter extends
    BasePresenter<ApplyBegOffDetailContract.Model, ApplyBegOffDetailContract.View> {

  private RxErrorHandler mErrorHandler;
  private Application mApplication;
  private ImageLoader mImageLoader;
  private AppManager mAppManager;

  @Inject
  public ApplyBegOffDetailPresenter(ApplyBegOffDetailContract.Model model,
      ApplyBegOffDetailContract.View rootView
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

  public void submit(String s, String s1, String type, String s2) {
    mModel.applyBegOff(s, s1, type, s2, "true").subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    })
        .doFinally(() -> {

        }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Map<String, Object>>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Map<String, Object>> stringBaseJson) {

            if (stringBaseJson.isSuccess()) {

              ArmsUtils.makeText(mRootView.getActivity(), "请假申请成功");
              EventBus.getDefault().post("refresh", EventBusTags.REFRESHBEGOFF);
              mRootView.killMyself();


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
