package com.irontower.gcbuser.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import javax.inject.Inject;

import com.irontower.gcbuser.mvp.contract.VehicleDetailContract;


@ActivityScope
public class VehicleDetailPresenter extends
    BasePresenter<VehicleDetailContract.Model, VehicleDetailContract.View> {

  private RxErrorHandler mErrorHandler;
  private Application mApplication;
  private ImageLoader mImageLoader;
  private AppManager mAppManager;

  @Inject
  public VehicleDetailPresenter(VehicleDetailContract.Model model,
      VehicleDetailContract.View rootView
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

}
