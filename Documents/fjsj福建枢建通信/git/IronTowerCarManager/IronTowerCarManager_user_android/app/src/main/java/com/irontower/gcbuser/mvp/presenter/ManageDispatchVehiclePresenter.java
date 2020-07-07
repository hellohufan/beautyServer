package com.irontower.gcbuser.mvp.presenter;

import com.irontower.gcbuser.mvp.contract.ManageDispatchVehicleContract;
import com.irontower.gcbuser.mvp.model.entity.ApproveOrder;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.ui.adapter.DispatchVehicleAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class ManageDispatchVehiclePresenter extends
    BasePresenter<ManageDispatchVehicleContract.Model, ManageDispatchVehicleContract.View> {

  private RxErrorHandler mErrorHandler;

  private DispatchVehicleAdapter mAdapter;
  private int currentPage = 1;
  private int totalNum = 0;

  @Inject
  public ManageDispatchVehiclePresenter(ManageDispatchVehicleContract.Model model,
      ManageDispatchVehicleContract.View rootView
      , RxErrorHandler handler, DispatchVehicleAdapter adapter) {
    super(model, rootView);
    this.mErrorHandler = handler;

    this.mAdapter = adapter;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;

  }


  public void getData(String s, boolean isRefresh) {

    if (isRefresh) {
      currentPage = 1;
      mRootView.showLoading();
      mAdapter.setEnableLoadMore(false);

    }

    mModel.getApproveOrder(s, currentPage, 5).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      if (isRefresh) {
        mAdapter.setEnableLoadMore(true);
        mRootView.hideLoading();
      }


    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseRowJson<ApproveOrder>>(mErrorHandler) {
          @Override
          public void onNext(BaseRowJson<ApproveOrder> stringBaseJson) {

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


        });


  }
}
