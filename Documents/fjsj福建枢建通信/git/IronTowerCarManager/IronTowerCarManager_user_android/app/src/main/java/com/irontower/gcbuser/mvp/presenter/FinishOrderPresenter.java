package com.irontower.gcbuser.mvp.presenter;

import android.app.Application;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.mvp.contract.FinishOrderContract;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.Order;
import com.irontower.gcbuser.mvp.ui.adapter.FinishOrderAdapter;
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
public class FinishOrderPresenter extends
    BasePresenter<FinishOrderContract.Model, FinishOrderContract.View> {

  private RxErrorHandler mErrorHandler;
  private FinishOrderAdapter mAdapter;
  private Application mApplication;

  private int currentPage = 1;
  private int totalNum = 0;

  @Inject
  public FinishOrderPresenter(FinishOrderContract.Model model, FinishOrderContract.View rootView
      , RxErrorHandler handler, FinishOrderAdapter adapter, Application application) {
    super(model, rootView);
    this.mErrorHandler = handler;
    this.mAdapter = adapter;
    this.mApplication = application;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;
    this.mApplication = null;

  }

  public void refresh(boolean isRefresh) {
    if (isRefresh) {
      currentPage = 1;
      mRootView.showLoading();
      mAdapter.setEnableLoadMore(false);

    }

    mModel.getOrders(currentPage, 5).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      if (isRefresh) {
        mAdapter.setEnableLoadMore(true);
        mRootView.hideLoading();
      }


    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseRowJson<Order>>(mErrorHandler) {
          @Override
          public void onNext(BaseRowJson<Order> stringBaseJson) {

            if (stringBaseJson.isSuccess()) {

              totalNum = stringBaseJson.getTotal();
              currentPage++;
              if (isRefresh) {
                if (stringBaseJson.getRows() != null) {
                  mAdapter.setNewData(stringBaseJson.getRows());

                }
              } else {
                if (stringBaseJson.getRows() != null) {
                  mAdapter.addData(stringBaseJson.getRows());
                }
              }
              if (mAdapter.getData().size() >= totalNum) {
                //第一页如果不够一页就不显示没有更多数据布局
                mAdapter.loadMoreEnd(isRefresh);

              } else {
                mAdapter.loadMoreComplete();
              }


            }

          }

          @Override
          public void onError(Throwable t) {
            super.onError(t);

          }
        });


  }

  public void delOrder(String orderNo) {
    mModel.delOrder(orderNo)
        .subscribeOn(Schedulers.io()).doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Object>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Object> stringBaseJson) {

            if (stringBaseJson.isSuccess()) {

              ArmsUtils
                  .makeText(mApplication, "删除订单成功");
              EventBus.getDefault().post("1", EventBusTags.DEL_ORDER);

            } else {

              ArmsUtils
                  .makeText(mApplication, stringBaseJson.getMessage());
            }
          }


        });

  }

}
