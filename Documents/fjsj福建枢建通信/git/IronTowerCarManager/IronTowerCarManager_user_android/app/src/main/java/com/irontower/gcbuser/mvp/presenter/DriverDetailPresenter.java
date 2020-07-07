package com.irontower.gcbuser.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.DriverDetailContract;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
import java.util.List;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class DriverDetailPresenter extends
    BasePresenter<DriverDetailContract.Model, DriverDetailContract.View> {

  private RxErrorHandler mErrorHandler;
  private Application mApplication;
  private ImageLoader mImageLoader;
  private AppManager mAppManager;

  @Inject
  public DriverDetailPresenter(DriverDetailContract.Model model, DriverDetailContract.View rootView
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
            ArmsUtils.getString(mApplication, R.string.no_permission_forerver));
      }
    }, mRootView.getRxPermissions(), mErrorHandler);


  }

}
