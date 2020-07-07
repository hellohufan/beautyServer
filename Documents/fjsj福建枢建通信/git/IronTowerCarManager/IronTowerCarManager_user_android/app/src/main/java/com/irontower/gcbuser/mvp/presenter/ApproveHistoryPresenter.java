package com.irontower.gcbuser.mvp.presenter;

import com.irontower.gcbuser.mvp.contract.ApproveHistoryContract;
import com.irontower.gcbuser.mvp.model.entity.ApproveOrder;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.ui.adapter.ApproveHistoryAdapter;
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
public class ApproveHistoryPresenter extends
    BasePresenter<ApproveHistoryContract.Model, ApproveHistoryContract.View> {

  private RxErrorHandler mErrorHandler;

  private ApproveHistoryAdapter mAdapter;
  private int currentPage = 1;
  private int totalNum = 0;

  @Inject
  public ApproveHistoryPresenter(ApproveHistoryContract.Model model,
      ApproveHistoryContract.View rootView
      , RxErrorHandler handler, ApproveHistoryAdapter approveHistoryAdapter) {
    super(model, rootView);
    this.mErrorHandler = handler;

    this.mAdapter = approveHistoryAdapter;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;

  }


  public void getData(String passenger, String orderBy, String orderCol, String beginCreateTime,
      String endCreateTime, boolean isRefresh, String beginTime, String endTime) {
    if (isRefresh) {
      currentPage = 1;
      mRootView.showLoading();
      mAdapter.setEnableLoadMore(false);

    }

    mModel.getApproveHistoryOrder(passenger, currentPage, 5, orderBy, orderCol, beginCreateTime,
        endCreateTime, beginTime, endTime).subscribeOn(Schedulers.io())
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


              } else {

                ArmsUtils
                    .makeText(mRootView.getActivity(), stringBaseJson.getMessage());
              }
            }
          }


        });
  }
}
