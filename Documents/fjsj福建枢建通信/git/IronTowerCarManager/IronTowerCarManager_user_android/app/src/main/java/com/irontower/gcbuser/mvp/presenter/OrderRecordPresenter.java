package com.irontower.gcbuser.mvp.presenter;

import com.irontower.gcbuser.mvp.contract.OrderRecordContract;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.MultipleOrder;
import com.irontower.gcbuser.mvp.ui.adapter.OrderRecordAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class OrderRecordPresenter extends
    BasePresenter<OrderRecordContract.Model, OrderRecordContract.View> {

  private RxErrorHandler mErrorHandler;

  private OrderRecordAdapter mAdapter;
  private int currentPage = 1;
  private int totalNum = 0;

  @Inject
  public OrderRecordPresenter(OrderRecordContract.Model model, OrderRecordContract.View rootView
      , RxErrorHandler handler, OrderRecordAdapter adapter) {
    super(model, rootView);
    this.mErrorHandler = handler;

    this.mAdapter = adapter;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;

  }


  public void refresh(boolean isRefresh) {
    if (isRefresh) {
      currentPage = 1;
      mRootView.showLoading();
      mAdapter.setEnableLoadMore(false);

    }

    mModel.getMultipleOrders(currentPage, 5).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      if (isRefresh) {
        mAdapter.setEnableLoadMore(true);
        mRootView.hideLoading();
      }


    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseRowJson<MultipleOrder>>(mErrorHandler) {
          @Override
          public void onNext(BaseRowJson<MultipleOrder> stringBaseJson) {

            {

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
          }

          @Override
          public void onError(Throwable t) {
            super.onError(t);

          }
        });


  }

}
