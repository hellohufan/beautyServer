package com.irontower.gcbuser.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.DriverPositionContract;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class DriverPositionPresenter extends
    BasePresenter<DriverPositionContract.Model, DriverPositionContract.View> {

  @Inject
  RxErrorHandler mErrorHandler;
  @Inject
  Application mApplication;
  @Inject
  ImageLoader mImageLoader;
  @Inject
  AppManager mAppManager;

  @Inject
  public DriverPositionPresenter(DriverPositionContract.Model model,
      DriverPositionContract.View rootView) {
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

  public void getData(Integer carId) {
    Observable.interval(0, 30, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .compose(RxLifecycleUtils
            .bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁

        .subscribe(new ErrorHandleSubscriber<Long>(mErrorHandler) {
          @Override
          public void onNext(Long aLong) {
            mModel.getData(carId).subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> mRootView.showLoading())
                .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
              mRootView.hideLoading();
            }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseJson<Map<String, Object>>>(mErrorHandler) {
                  @Override
                  public void onNext(BaseJson<Map<String, Object>> stringBaseJson) {
                    if (stringBaseJson.isSuccess()) {
                      mRootView.init(stringBaseJson.getData());


                    } else {

                      ArmsUtils
                          .makeText(mRootView.getActivity(), stringBaseJson.getMessage());
                    }
                  }


                });

          }
        });


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
        ArmsUtils.makeText(mApplication, ArmsUtils.getString(mApplication, R.string.no_permission));
      }

      @Override
      public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
        ArmsUtils.makeText(mApplication,
            ArmsUtils.getString(mApplication, R.string.no_permission_forerver));
      }
    }, mRootView.getRxpermission(), mErrorHandler);

  }
}
