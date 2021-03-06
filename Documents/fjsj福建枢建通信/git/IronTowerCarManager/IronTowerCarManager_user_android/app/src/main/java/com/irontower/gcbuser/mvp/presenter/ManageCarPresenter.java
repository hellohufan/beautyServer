package com.irontower.gcbuser.mvp.presenter;

import com.irontower.gcbuser.mvp.contract.ManageCarContract;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.Car;
import com.irontower.gcbuser.mvp.ui.adapter.ManageCarAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class ManageCarPresenter extends
    BasePresenter<ManageCarContract.Model, ManageCarContract.View> {

  private RxErrorHandler mErrorHandler;
  private ManageCarAdapter mAdapter;
  private int currentPage = 1;
  private int totalNum = 0;


  @Inject
  public ManageCarPresenter(ManageCarContract.Model model, ManageCarContract.View rootView
      , RxErrorHandler handler, ManageCarAdapter adapter) {
    super(model, rootView);
    this.mErrorHandler = handler;
    this.mAdapter = adapter;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;

  }


  public void refresh(boolean isRefresh, String b) {
    if (isRefresh) {
      currentPage = 1;
      mRootView.showLoading();
      mAdapter.setEnableLoadMore(false);

    }

    mModel.getCars(currentPage, 5, b).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      if (isRefresh) {
        mAdapter.setEnableLoadMore(true);
        mRootView.hideLoading();
      }


    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseRowJson<Car>>(mErrorHandler) {
          @Override
          public void onNext(BaseRowJson<Car> stringBaseJson) {

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

}
