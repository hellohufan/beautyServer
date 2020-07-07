package com.irontower.gcbuser.mvp.presenter;

import com.irontower.gcbuser.mvp.contract.ApproveBegOffContract;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.BegOff;
import com.irontower.gcbuser.mvp.ui.adapter.ApproveBegOffAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


@ActivityScope
public class ApproveBegOffPresenter extends
    BasePresenter<ApproveBegOffContract.Model, ApproveBegOffContract.View> {

  private RxErrorHandler mErrorHandler;
  private ApproveBegOffAdapter mAdapter;
  private int currentPage = 1;
  private int totalNum = 0;

  @Inject
  public ApproveBegOffPresenter(ApproveBegOffContract.Model model,
      ApproveBegOffContract.View rootView
      , RxErrorHandler handler, ApproveBegOffAdapter adapter) {
    super(model, rootView);
    this.mErrorHandler = handler;
    this.mAdapter = adapter;
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

    mModel.getBegOffs(currentPage, 5).subscribeOn(Schedulers.io())
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

          }
        });


  }


  public void approvalBegOff(String s, Integer leaveId, String s1) {
    mModel.approvalBegOff(s, leaveId, s1).subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Object>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Object> stringBaseJson) {
            if (stringBaseJson.isSuccess()) {

              getData(true);
            } else {

              ArmsUtils
                  .makeText(mRootView.getActivity(), stringBaseJson.getMessage());
            }
          }


        });
  }
}
