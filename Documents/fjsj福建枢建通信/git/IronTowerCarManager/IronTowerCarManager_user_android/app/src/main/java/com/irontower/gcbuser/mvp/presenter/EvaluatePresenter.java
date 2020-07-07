package com.irontower.gcbuser.mvp.presenter;

import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.mvp.contract.EvaluateContract;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.jess.arms.di.scope.ActivityScope;
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
public class EvaluatePresenter extends
    BasePresenter<EvaluateContract.Model, EvaluateContract.View> {

  private RxErrorHandler mErrorHandler;


  @Inject
  public EvaluatePresenter(EvaluateContract.Model model, EvaluateContract.View rootView
      , RxErrorHandler handler) {
    super(model, rootView);
    this.mErrorHandler = handler;

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;

  }

  public void evaluate(String orderNo, int grade1, int grade2, int grade3, String s) {
    mModel.evaluate(orderNo, grade1, grade2, grade3, s).subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Object>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Object> stringBaseJson) {
            if (stringBaseJson.isSuccess()) {
              ArmsUtils.makeText(mRootView.getActivity(), stringBaseJson.getMessage());

              EventBus.getDefault().post("1", EventBusTags.EVALUATE_SUCCESS);


            } else {

              ArmsUtils
                  .makeText(mRootView.getActivity(), stringBaseJson.getMessage());
            }
          }


        });
  }
}
