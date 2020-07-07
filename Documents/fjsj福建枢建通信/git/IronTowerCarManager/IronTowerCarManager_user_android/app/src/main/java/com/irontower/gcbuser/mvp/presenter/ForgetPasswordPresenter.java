package com.irontower.gcbuser.mvp.presenter;

import android.widget.TextView;
import com.irontower.gcbuser.mvp.contract.ForgetPasswordContract;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class ForgetPasswordPresenter extends
    BasePresenter<ForgetPasswordContract.Model, ForgetPasswordContract.View> {

  private RxErrorHandler mErrorHandler;


  @Inject
  public ForgetPasswordPresenter(ForgetPasswordContract.Model model,
      ForgetPasswordContract.View rootView
      , RxErrorHandler handler) {
    super(model, rootView);
    this.mErrorHandler = handler;

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;

  }

  public void getCode(String phone, TextView textView) {
    mModel.getSMS(phone).subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Map<String, Object>>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Map<String, Object>> stringBaseJson) {

            if (stringBaseJson.isSuccess()) {
              ArmsUtils.makeText(mRootView.getActivity(), stringBaseJson.getMessage());
              countDown(textView);
            } else {
              ArmsUtils.makeText(mRootView.getActivity(), stringBaseJson.getMessage());
            }
          }

          @Override
          public void onError(Throwable t) {
            super.onError(t);

          }
        });//
  }

  public void resetPwd(String phone, String code, String pwd) {
    mModel.resetPwd(phone, code, pwd).subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Integer>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Integer> stringBaseJson) {
            if (stringBaseJson.isSuccess()) {
              ArmsUtils.makeText(mRootView.getActivity(), stringBaseJson.getMessage());
              mRootView.killMyself();
            } else {
              ArmsUtils.makeText(mRootView.getActivity(), stringBaseJson.getMessage());
            }

          }

          @Override
          public void onError(Throwable t) {
            super.onError(t);

          }
        });//

  }

  private void countDown(TextView textView) {
    Observable.interval(0, 1, TimeUnit.SECONDS)
        .take(60)
        .map(new Function<Long, Long>() {
          @Override
          public Long apply(Long aLong) throws Exception {
            return 60 - aLong;
          }
        })  //设
        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            mRootView.disable();
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .compose(RxLifecycleUtils.bindUntilEvent(mRootView, ActivityEvent.DESTROY))

        .subscribe(new ErrorHandleSubscriber<Long>(mErrorHandler) {
          @Override
          public void onNext(Long aLong) {

            textView.setText(aLong + "s后重发");
          }

          @Override
          public void onComplete() {
            super.onComplete();
            mRootView.able();
          }
        });
  }
}
