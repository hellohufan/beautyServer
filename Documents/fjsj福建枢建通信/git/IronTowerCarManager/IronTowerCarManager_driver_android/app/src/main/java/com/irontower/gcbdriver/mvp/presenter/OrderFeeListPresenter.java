package com.irontower.gcbdriver.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.OrderFeeListContract;
import com.irontower.gcbdriver.mvp.model.entity.BaseRowJson;
import com.irontower.gcbdriver.mvp.model.entity.Order;
import com.irontower.gcbdriver.mvp.ui.adapter.OrderRecordAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class OrderFeeListPresenter extends
    BasePresenter<OrderFeeListContract.Model, OrderFeeListContract.View> {

  @Inject
  RxErrorHandler mErrorHandler;
  @Inject
  Application mApplication;
  @Inject
  ImageLoader mImageLoader;
  @Inject
  AppManager mAppManager;
  private int currentPage = 1;
  private int totalNum = 0;
  private OrderRecordAdapter mAdapter;


  @Inject
  public OrderFeeListPresenter(OrderFeeListContract.Model model,
      OrderFeeListContract.View rootView, OrderRecordAdapter adapter) {
    super(model, rootView);
    this.mAdapter = adapter;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;
    this.mAppManager = null;
    this.mImageLoader = null;
    this.mApplication = null;
  }

  public void refresh(boolean isRefresh) {
    if (isRefresh) {
      currentPage = 1;
      mRootView.showLoading();
      mAdapter.setEnableLoadMore(false);

    }

    mModel.getFeeOrders(currentPage, 5).subscribeOn(Schedulers.io()).doOnSubscribe(disposable -> {

    })
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


        });


  }


  public void call(String passengerTel) {
    PermissionUtil.callPhone(new PermissionUtil.RequestPermission() {
      @Override
      public void onRequestPermissionSuccess() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + passengerTel);
        intent.setData(data);
        mRootView.launchActivity(intent);

      }

      @Override
      public void onRequestPermissionFailure(List<String> permissions) {
        ArmsUtils.makeText(mRootView.getActivity(),
            ArmsUtils.getString(mRootView.getActivity(), R.string.no_permission));
      }

      @Override
      public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
        ArmsUtils.makeText(mRootView.getActivity(),
            ArmsUtils.getString(mRootView.getActivity(), R.string.no_permission));
      }
    }, mRootView.getRxpermissions(), mErrorHandler);

  }
}
