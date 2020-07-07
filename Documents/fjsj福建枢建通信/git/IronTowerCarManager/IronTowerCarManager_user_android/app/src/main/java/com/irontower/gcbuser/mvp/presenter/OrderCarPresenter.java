package com.irontower.gcbuser.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.mvp.contract.OrderCarContract;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.SubmitOrder;
import com.irontower.gcbuser.mvp.ui.activity.OrderRecordDetailActivity;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import org.simple.eventbus.EventBus;


@ActivityScope
public class OrderCarPresenter extends
    BasePresenter<OrderCarContract.Model, OrderCarContract.View> {

  private RxErrorHandler mErrorHandler;

  @Inject
  public OrderCarPresenter(OrderCarContract.Model model, OrderCarContract.View rootView
      , RxErrorHandler handler, Application application
      , ImageLoader imageLoader, AppManager appManager) {
    super(model, rootView);
    this.mErrorHandler = handler;

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;

  }

  public void submit(SubmitOrder submitOrder,
      List<Attach> attachList) {
    mModel.submitOrder(submitOrder, attachList).subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Map<String, Object>>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Map<String, Object>> stringBaseJson) {
            if (stringBaseJson.isSuccess()) {
              ArmsUtils.makeText(mRootView.getActivity(), "申请用车成功");
              Intent intent = new Intent();
              intent.putExtra("orderNo",
                  String.valueOf(stringBaseJson.getData().get("orderNo")));
              intent.setClass(mRootView.getActivity(), OrderRecordDetailActivity.class);
              mRootView.launchActivity(intent);
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

  public void updateOrder(String orderNo, SubmitOrder submitOrder,
      List<Attach> attachList) {
    mModel.updateOrder(orderNo, submitOrder, attachList).subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Map<String, Object>>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Map<String, Object>> stringBaseJson) {
            if (stringBaseJson.isSuccess()) {
              ArmsUtils.makeText(mRootView.getActivity(), "申请用车成功");
              EventBus.getDefault().post("1", EventBusTags.UPDATE_ORDER);
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
}
