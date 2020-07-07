package com.irontower.gcbdriver.mvp.presenter;

import com.irontower.gcbdriver.mvp.contract.MessageDetailContract;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.irontower.gcbdriver.mvp.model.entity.Message;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class MessageDetailPresenter extends
    BasePresenter<MessageDetailContract.Model, MessageDetailContract.View> {

  private RxErrorHandler mErrorHandler;


  @Inject
  public MessageDetailPresenter(MessageDetailContract.Model model,
      MessageDetailContract.View rootView
      , RxErrorHandler handler) {
    super(model, rootView);
    this.mErrorHandler = handler;

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;

  }

  public void getDetail(String eid) {
    mModel.getMessageDetail(eid).subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Message>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Message> stringBaseJson) {
            if (stringBaseJson.isSuccess()) {
              mRootView.init(stringBaseJson.getData());
            } else {

            }
          }


        });//
  }
}
