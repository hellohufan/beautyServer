package com.irontower.gcbdriver.mvp.presenter;

import android.content.Intent;
import com.blankj.utilcode.util.SPUtils;
import com.irontower.gcbdriver.mvp.contract.WelcomeContract;
import com.irontower.gcbdriver.mvp.ui.activity.LoginActivity;
import com.irontower.gcbdriver.mvp.ui.activity.MainActivity;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.mvp.IModel;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

/**
 * Created by jianghaifeng on 2017/11/30.
 */
@ActivityScope
public class WelcomePresenter extends BasePresenter<IModel, WelcomeContract.View> {

  @Inject
  public WelcomePresenter(WelcomeContract.View view) {
    super(view);
  }


  public void jump() {

    Observable.timer(1500, TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .compose(RxLifecycleUtils
            .bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁

        .subscribe(aLong -> {
          if (SPUtils.getInstance().contains("isLogin")) {
            mRootView.launchActivity(new Intent(mRootView.getActivity(), MainActivity.class));
          } else {
            mRootView.launchActivity(new Intent(mRootView.getActivity(), LoginActivity.class));

          }
          mRootView.killMyself();

        });
  }
}
