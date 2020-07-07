package com.irontower.gcbuser.mvp.presenter;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import cn.jpush.android.api.JPushInterface;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.LoginContract;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.Module;
import com.irontower.gcbuser.mvp.ui.activity.MainActivity;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {

  private RxErrorHandler mErrorHandler;
  private Application mApplication;
  private Gson mGson;


  @Inject
  public LoginPresenter(LoginContract.Model model, LoginContract.View rootView
      , RxErrorHandler handler, Application application, Gson gson) {
    super(model, rootView);
    this.mErrorHandler = handler;
    this.mApplication = application;
    this.mGson = gson;

  }


  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;
    this.mGson = null;
    this.mApplication = null;
  }


  public void login(String userName, String password) {
    mModel.login(userName, password, JPushInterface.getUdid(mApplication))
        .subscribeOn(Schedulers.io()).doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Map<String, Object>>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Map<String, Object>> stringBaseJson) {
            if (stringBaseJson.isSuccess()) {
              SPUtils.getInstance().put("ACCESS_TOKEN",
                  stringBaseJson.getData().get("loginToken") + JPushInterface
                      .getUdid(mApplication));
              SPUtils.getInstance().put("user", mGson.toJson(stringBaseJson.getData().get("user")));
              JPushInterface.setAlias(mRootView.getActivity(), (int) System.currentTimeMillis(),
                  JPushInterface.getUdid(mRootView.getActivity()));

              getPermissions();


            } else {

              ArmsUtils
                  .makeText(mRootView.getActivity(), stringBaseJson.getMessage());
            }
          }


        });


  }

  private void getPermissions() {
    mModel.getPermission().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<List<Module>>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<List<Module>> stringBaseJson) {
            if (stringBaseJson.isSuccess()) {

              SPUtils.getInstance().put("modules", mGson.toJson(stringBaseJson.getData()));
              SPUtils.getInstance().put("isLogin", true);
              SPUtils.getInstance().put("sound", true);

              Intent intent = new Intent();
              intent.setClass(mRootView.getActivity(), MainActivity.class);
              mRootView.launchActivity(intent);
              mRootView.killMyself();


            } else {

              ArmsUtils
                  .makeText(mRootView.getActivity(), stringBaseJson.getMessage());
            }
          }


        });

  }

  public void init() {
    PermissionUtil.requestPermission(new PermissionUtil.RequestPermission() {
                                       @Override
                                       public void onRequestPermissionSuccess() {
                                         //request permission success, do something.
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
                                     }, mRootView.getRxPermissions(), mErrorHandler, Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION);

  }

}
