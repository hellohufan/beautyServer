package com.irontower.gcbdriver.mvp.presenter;

import com.irontower.gcbdriver.mvp.contract.ApplyBegOffContract;
import com.irontower.gcbdriver.mvp.model.entity.BaseRowJson;
import com.irontower.gcbdriver.mvp.model.entity.BegOff;
import com.irontower.gcbdriver.mvp.ui.adapter.ApplyBegOffAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class ApplyBegOffPresenter extends
    BasePresenter<ApplyBegOffContract.Model, ApplyBegOffContract.View> {

  private RxErrorHandler mErrorHandler;
  private ApplyBegOffAdapter mAdapter;
  private int currentPage = 1;
  private int totalNum = 0;


  @Inject
  public ApplyBegOffPresenter(ApplyBegOffContract.Model model, ApplyBegOffContract.View rootView
      , RxErrorHandler handler, ApplyBegOffAdapter adapter) {
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
    mModel.getBegOff(currentPage, 5).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      if (isRefresh) {
        mAdapter.setEnableLoadMore(true);
        mRootView.hideLoading();
      }


    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseRowJson<BegOff>>(mErrorHandler) {
          @Override
          public void onNext(BaseRowJson<BegOff> stringBaseJson) {

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
            if (!isRefresh) {
              mAdapter.loadMoreFail();
            }
          }
        });
  }
}
