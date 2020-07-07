package com.irontower.gcbdriver.mvp.presenter;

import com.irontower.gcbdriver.mvp.contract.ChooseCarContract;
import com.irontower.gcbdriver.mvp.model.entity.BaseRowJson;
import com.irontower.gcbdriver.mvp.model.entity.Car;
import com.irontower.gcbdriver.mvp.ui.adapter.ChooseCarAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import timber.log.Timber;


@ActivityScope
public class ChooseCarPresenter extends
    BasePresenter<ChooseCarContract.Model, ChooseCarContract.View> {

  private RxErrorHandler mErrorHandler;

  private ChooseCarAdapter mAdapter;
  private int currentPage = 1;
  private int totalNum = 0;

  @Inject
  public ChooseCarPresenter(ChooseCarContract.Model model, ChooseCarContract.View rootView
      , RxErrorHandler handler, ChooseCarAdapter adapter) {
    super(model, rootView);
    this.mErrorHandler = handler;
    mAdapter = adapter;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;

  }

  public void getData(String beginTime, String endTime, String s, boolean isRefresh) {
    if (isRefresh) {
      currentPage = 1;
      mRootView.showLoading();
      mAdapter.setEnableLoadMore(false);

    }

    mModel.getVehicles(s, currentPage, 5, beginTime, endTime).subscribeOn(
        Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      if (isRefresh) {
        mAdapter.setEnableLoadMore(true);
        mRootView.hideLoading();
      }


    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseRowJson<Car>>(mErrorHandler) {
          @Override
          public void onNext(BaseRowJson<Car> stringBaseJson) {
            Timber.w(stringBaseJson.toString());
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
