package com.irontower.gcbuser.mvp.presenter;

import com.irontower.gcbuser.mvp.contract.GoOutRecordContract;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.BegGoOut;
import com.irontower.gcbuser.mvp.ui.adapter.GoOutRecordAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class GoOutRecordPresenter extends
    BasePresenter<GoOutRecordContract.Model, GoOutRecordContract.View> {

  private RxErrorHandler mErrorHandler;
  private int currentPage = 1;
  private int totalNum = 0;
  private GoOutRecordAdapter mAdapter;

  @Inject
  public GoOutRecordPresenter(GoOutRecordContract.Model model, GoOutRecordContract.View rootView
      , RxErrorHandler handler, GoOutRecordAdapter goOutRecordAdapter) {
    super(model, rootView);
    this.mErrorHandler = handler;
    this.mAdapter = goOutRecordAdapter;

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;

  }


  public void getData(boolean isRefresh) {
    if (isRefresh) {
      currentPage = 1;
      mRootView.showLoading();
      mAdapter.setEnableLoadMore(false);

    }

    mModel.getGoOuts(currentPage, 5).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      if (isRefresh) {
        mAdapter.setEnableLoadMore(true);
        mRootView.hideLoading();
      }


    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseRowJson<BegGoOut>>(mErrorHandler) {
          @Override
          public void onNext(BaseRowJson<BegGoOut> stringBaseJson) {

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
