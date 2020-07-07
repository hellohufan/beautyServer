package com.irontower.gcbuser.mvp.presenter;

import android.app.Application;
import com.irontower.gcbuser.mvp.contract.CarTraceContract;
import com.irontower.gcbuser.mvp.model.entity.MultipleOrg;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


/**
 * ================================================ Description:
 * <p>
 * Created by MVPArmsTemplate on 06/12/2019 10:53
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class CarTracePresenter extends
    BasePresenter<CarTraceContract.Model, CarTraceContract.View> {

  @Inject
  RxErrorHandler mErrorHandler;
  @Inject
  Application mApplication;
  @Inject
  ImageLoader mImageLoader;
  @Inject
  AppManager mAppManager;

  @Inject
  public CarTracePresenter(CarTraceContract.Model model, CarTraceContract.View rootView) {
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

  public void getData() {
    mModel.getData().subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {

      mRootView.hideLoading();
    })
        .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<List<Map<String, Object>>>(mErrorHandler) {
          @Override
          public void onNext(List<Map<String, Object>> list) {
            mRootView.dealData(list);
          }


        });


  }

  public void getChildren(MultipleOrg multipleOrg, int pos) {
    mModel.loadTreeChildren(multipleOrg.getId()).subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {

      mRootView.hideLoading();
    })
        .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<List<Map<String, Object>>>(mErrorHandler) {
          @Override
          public void onNext(List<Map<String, Object>> list) {
            mRootView.dealChildren(list, multipleOrg, pos);
          }


        });

  }
}
