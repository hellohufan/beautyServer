package com.irontower.gcbdriver.mvp.presenter;

import com.irontower.gcbdriver.app.EventBusTags;
import com.irontower.gcbdriver.mvp.contract.BegGoOutDetailContract;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.jess.arms.di.scope.ActivityScope;
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
import timber.log.Timber;


@ActivityScope
public class BegGoOutDetailPresenter extends
    BasePresenter<BegGoOutDetailContract.Model, BegGoOutDetailContract.View> {

  private RxErrorHandler mErrorHandler;


  @Inject
  public BegGoOutDetailPresenter(BegGoOutDetailContract.Model model,
      BegGoOutDetailContract.View rootView
      , RxErrorHandler handler) {
    super(model, rootView);
    this.mErrorHandler = handler;

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;

  }

  public void changeGoOut(String hid, String state) {
    mModel.changeBegGoOut(hid, state).subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Map<String, Object>>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Map<String, Object>> stringBaseJson) {
            Timber.w(stringBaseJson.toString());
            if (stringBaseJson.isSuccess()) {

              ArmsUtils
                  .makeText(mRootView.getActivity(), stringBaseJson.getMessage());
              EventBus.getDefault().post("1", EventBusTags.CHANGE_GO_OUT_SUCCESS);


            } else {

              ArmsUtils
                  .makeText(mRootView.getActivity(), stringBaseJson.getMessage());
            }
          }

          @Override
          public void onError(Throwable t) {
            super.onError(t);

          }
        });//


  }
}
