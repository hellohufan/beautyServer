package com.irontower.gcbuser.mvp.presenter;

import android.app.Application;
import com.blankj.utilcode.util.FileUtils;
import com.irontower.gcbuser.app.utils.IntentUtil;
import com.irontower.gcbuser.mvp.contract.ManyAttachmentContract;
import com.irontower.gcbuser.mvp.model.api.Api;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.inject.Inject;
import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.ResponseBody;


@ActivityScope
public class ManyAttachmentPresenter extends
    BasePresenter<ManyAttachmentContract.Model, ManyAttachmentContract.View> {

  @Inject
  RxErrorHandler mErrorHandler;
  @Inject
  Application mApplication;
  @Inject
  ImageLoader mImageLoader;
  @Inject
  AppManager mAppManager;

  @Inject
  public ManyAttachmentPresenter(ManyAttachmentContract.Model model,
      ManyAttachmentContract.View rootView) {
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

  public void getFile(String url, String name) {

    mModel.getFile(url).subscribeOn(Schedulers.io())

        .doOnSubscribe(new Consumer<Disposable>() {
          @Override
          public void accept(Disposable disposable) throws Exception {
            mRootView.showLoading();
            ProgressManager.getInstance().addResponseListener(url, new ProgressListener() {
              @Override
              public void onProgress(ProgressInfo progressInfo) {
                if (progressInfo.isFinish()) {
                  if (mRootView != null) {
                    mRootView.hideLoading();
                    IntentUtil.viewFile(mRootView.getActivity(), Api.FILEPATH + name);
                  }

                }
              }

              @Override
              public void onError(long id, Exception e) {
                if (mRootView != null) {
                  mRootView.hideLoading();
                  ArmsUtils.makeText(mRootView.getActivity(), "下载异常");
                }
              }

            });
          }
        })
        .observeOn(Schedulers.io()).doOnNext(new Consumer<ResponseBody>() {
      @Override
      public void accept(ResponseBody responseBody) throws Exception {
        if (!writeResponseBodyToDisk(responseBody, name)) {
          if (mRootView != null) {
            mRootView.hideLoading();
            ArmsUtils.makeText(mRootView.getActivity(), "下载异常");
          }

        }

      }
    }).observeOn(AndroidSchedulers.mainThread())
        .compose(RxLifecycleUtils.bindToLifecycle(mRootView)).subscribe(
        new ErrorHandleSubscriber<ResponseBody>(mErrorHandler) {
          @Override
          public void onNext(ResponseBody responseBody) {

          }
        });

  }

  private boolean writeResponseBodyToDisk(ResponseBody body, String name) {
    try {

      File futureStudioIconFile = new File(
          Api.FILEPATH, name);
      FileUtils.createFileByDeleteOldFile(futureStudioIconFile);
      InputStream inputStream = null;
      OutputStream outputStream = null;

      try {
        byte[] fileReader = new byte[4096];

        inputStream = body.byteStream();
        outputStream = new FileOutputStream(futureStudioIconFile);

        while (true) {
          int read = inputStream.read(fileReader);

          if (read == -1) {
            break;
          }

          outputStream.write(fileReader, 0, read);


        }

        outputStream.flush();

        return true;
      } catch (IOException e) {
        return false;
      } finally {
        if (inputStream != null) {
          inputStream.close();
        }

        if (outputStream != null) {
          outputStream.close();
        }
      }
    } catch (IOException e) {
      return false;
    }
  }
}
