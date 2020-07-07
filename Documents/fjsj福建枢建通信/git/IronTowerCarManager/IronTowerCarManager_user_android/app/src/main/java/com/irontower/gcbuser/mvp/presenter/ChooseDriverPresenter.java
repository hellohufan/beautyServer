package com.irontower.gcbuser.mvp.presenter;

import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.mvp.contract.ChooseDriverContract;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.Driver;
import com.irontower.gcbuser.mvp.ui.adapter.ChooseDriverAdapter;
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
public class ChooseDriverPresenter extends
    BasePresenter<ChooseDriverContract.Model, ChooseDriverContract.View> {

  private RxErrorHandler mErrorHandler;

  private ChooseDriverAdapter mAdapter;
  private int currentPage = 1;
  private int totalNum = 0;

  @Inject
  public ChooseDriverPresenter(ChooseDriverContract.Model model, ChooseDriverContract.View rootView
      , RxErrorHandler handler, ChooseDriverAdapter adapter) {
    super(model, rootView);
    this.mErrorHandler = handler;

    this.mAdapter = adapter;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;

  }


  public void getData(String s, boolean isRefresh, String orderBeginTime,
      String orderEndTime,
      String useOrgId, String selectedDriverId) {
    if (isRefresh) {

      currentPage = 1;
      mRootView.showLoading();
      mAdapter.setEnableLoadMore(false);

    }

    mModel
        .getDrivers(s, currentPage, 5, 2, orderBeginTime, orderEndTime, useOrgId, selectedDriverId)
        .subscribeOn(
            Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      if (isRefresh) {
        mAdapter.setEnableLoadMore(true);
        mRootView.hideLoading();
      }


    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(

            new ErrorHandleSubscriber<BaseRowJson<Driver>>(mErrorHandler) {
              @Override
              public void onNext(BaseRowJson<Driver> stringBaseJson) {

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

  public void submit(String todoId, String orderCarNo, String carId, Integer driverId,
      String carType, String feeType) {
    mModel.submit(todoId, orderCarNo, carId, String.valueOf(driverId), carType, feeType)
        .subscribeOn(Schedulers.io()).doOnSubscribe(disposable -> mRootView.showLoading1())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading1();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Object>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Object> stringBaseJson) {
            if (stringBaseJson.isSuccess()) {
              EventBus.getDefault().post("1", EventBusTags.DISPATCHCARSUCCESS);
              ArmsUtils
                  .makeText(mRootView.getActivity(), "派车成功");

            } else {

              ArmsUtils
                  .makeText(mRootView.getActivity(), stringBaseJson.getMessage());
            }
          }


        });
  }
}
