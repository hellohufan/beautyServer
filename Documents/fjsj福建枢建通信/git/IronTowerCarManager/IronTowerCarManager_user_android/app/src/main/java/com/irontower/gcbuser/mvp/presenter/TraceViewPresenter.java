package com.irontower.gcbuser.mvp.presenter;

import android.app.Application;
import com.irontower.gcbuser.mvp.contract.TraceViewContract;
import com.irontower.gcbuser.mvp.model.dao.AppDatabase;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.CarSearch;
import com.irontower.gcbuser.mvp.model.entity.Track;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;


/**
 * ================================================ Description:
 * <p>
 * Created by MVPArmsTemplate on 06/12/2019 22:13
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
public class TraceViewPresenter extends
    BasePresenter<TraceViewContract.Model, TraceViewContract.View> {

  @Inject
  RxErrorHandler mErrorHandler;
  @Inject
  Application mApplication;
  @Inject
  ImageLoader mImageLoader;
  @Inject
  AppManager mAppManager;

  @Inject
  public TraceViewPresenter(TraceViewContract.Model model, TraceViewContract.View rootView) {
    super(model, rootView);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;
    this.mAppManager = null;
    this.mImageLoader = null;
    this.mApplication = null;
  }

  public void getData(String carId, String carNo, CharSequence bTime, CharSequence eTime) {
    mModel.getData(carId, carNo, bTime, eTime).subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {

      mRootView.hideLoading();
    })
        .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseRowJson<Track>>(mErrorHandler) {
          @Override
          public void onNext(BaseRowJson<Track> obj) {
            if (obj.isSuccess()) {
              mRootView.init(obj.getRows());
            } else {
              ArmsUtils.makeText(mRootView.getActivity(), obj.getMessage());
            }
          }
        });

  }

  public void loadHistory(String userId) {
    AppDatabase.getInstance(mRootView.getActivity()).carSearchDao().loadHistory(userId).
        subscribeOn(Schedulers.io()).
        observeOn(AndroidSchedulers.mainThread()).
        compose(RxLifecycleUtils.bindToLifecycle(mRootView)).

        subscribe(new SingleObserver<List<CarSearch>>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onSuccess(List<CarSearch> carSearches) {
            mRootView.refreshData(carSearches);
          }

          @Override
          public void onError(Throwable e) {

          }
        });

  }

}
