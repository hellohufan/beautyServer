package com.irontower.gcbuser.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import com.blankj.utilcode.util.FileUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.utils.IntentUtil;
import com.irontower.gcbuser.mvp.contract.ApproveHistoryDetailContract;
import com.irontower.gcbuser.mvp.model.api.Api;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.Order;
import com.irontower.gcbuser.mvp.model.entity.OrderCar;
import com.irontower.gcbuser.mvp.model.entity.OrderTrack;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import okhttp3.ResponseBody;


@ActivityScope
public class ApproveHistoryDetailPresenter extends
    BasePresenter<ApproveHistoryDetailContract.Model, ApproveHistoryDetailContract.View> {

  private RxErrorHandler mErrorHandler;
  private Application mApplication;
  private Gson mGson;

  @Inject
  public ApproveHistoryDetailPresenter(ApproveHistoryDetailContract.Model model,
      ApproveHistoryDetailContract.View rootView
      , RxErrorHandler handler, Gson gson, Application application) {
    super(model, rootView);
    this.mErrorHandler = handler;
    this.mGson = gson;
    this.mApplication = application;

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;

  }

  public void getData(String orderNo) {
    mModel.getOrderRecordDetail(orderNo).subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Map<String, Object>>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Map<String, Object>> stringBaseJson) {
            if (stringBaseJson.isSuccess()) {
              List<OrderCar> orderCars = new ArrayList<>();
              List<OrderTrack> orderTracks = new ArrayList<>();
              List<Attach> attaches = new ArrayList<>();

              Order order = mGson
                  .fromJson(mGson.toJson(stringBaseJson.getData().get("order")), Order.class);

              for (JsonElement jsonElement : new JsonParser()
                  .parse(mGson.toJson(stringBaseJson.getData().get("orderCars")))
                  .getAsJsonArray()) {
                orderCars.add(mGson.fromJson(jsonElement, OrderCar.class));
              }
              for (JsonElement jsonElement : new JsonParser()
                  .parse(mGson.toJson(stringBaseJson.getData().get("orderTracks")))
                  .getAsJsonArray()) {
                orderTracks.add(mGson.fromJson(jsonElement, OrderTrack.class));
              }
              for (JsonElement jsonElement : new JsonParser()
                  .parse(mGson.toJson(stringBaseJson.getData().get("attachs")))
                  .getAsJsonArray()) {
                attaches.add(mGson.fromJson(jsonElement, Attach.class));
              }

              String node = null;
              if (orderTracks.size() > 0) {
                node = orderTracks.get(0).getLevelName();
              }
              mRootView.init(order, orderCars, node, orderTracks, attaches);


            } else {

              ArmsUtils
                  .makeText(mRootView.getActivity(), stringBaseJson.getMessage());
            }
          }


        });

  }

  public void call(String phone) {
    PermissionUtil.callPhone(new PermissionUtil.RequestPermission() {
      @Override
      public void onRequestPermissionSuccess() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phone);
        intent.setData(data);
        mRootView.launchActivity(intent);

      }

      @Override
      public void onRequestPermissionFailure(List<String> permissions) {
        ArmsUtils.makeText(mApplication, ArmsUtils.getString(mApplication, R.string.no_permission));
      }

      @Override
      public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
        ArmsUtils.makeText(mApplication,
            ArmsUtils.getString(mApplication, R.string.no_permission_forerver));
      }
    }, mRootView.getRxpermission(), mErrorHandler);
  }

  public void getFile(String url, String name) {
    PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
      @Override
      public void onRequestPermissionSuccess() {
        //request permission success, do something.
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

      @Override
      public void onRequestPermissionFailure(List<String> permissions) {
        ArmsUtils.makeText(mApplication, ArmsUtils.getString(mApplication, R.string.no_permission));

      }

      @Override
      public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
        ArmsUtils.makeText(mApplication,
            ArmsUtils.getString(mApplication, R.string.no_permission_forerver));
      }
    }, mRootView.getRxpermission(), mErrorHandler);


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
