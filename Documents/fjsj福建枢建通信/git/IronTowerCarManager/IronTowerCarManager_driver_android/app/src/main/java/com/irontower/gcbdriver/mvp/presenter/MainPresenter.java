package com.irontower.gcbdriver.mvp.presenter;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.MainContract;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.irontower.gcbdriver.mvp.model.entity.Dict;
import com.irontower.gcbdriver.mvp.model.entity.Version;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;


@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> {

  private RxErrorHandler mErrorHandler;
  private Gson mGson;

  @Inject
  public MainPresenter(MainContract.Model model, MainContract.View rootView
      , RxErrorHandler handler
      , Gson gson) {
    super(model, rootView);
    this.mErrorHandler = handler;
    this.mGson = gson;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;
    this.mGson = null;
  }


  public void logout() {
    mModel.logout().subscribeOn(Schedulers.io())

        .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Map<String, Object>>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Map<String, Object>> mapBaseJson) {

          }
        });

  }

  public void getDicts() {

    mModel.getDictList().subscribeOn(Schedulers.io())
        .retryWhen(new RetryWithDelay(3, 2))
        .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Map<String, Object>>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Map<String, Object>> mapBaseJson) {
            List<Dict> carTypes = new ArrayList<>();
            List<Dict> orderStates = new ArrayList<>();
            List<Dict> carOutTypes = new ArrayList<>();
            List<Dict> carOutStates = new ArrayList<>();

            for (JsonElement jsonElement : new JsonParser()
                .parse(mGson.toJson(mapBaseJson.getData().get("DICT_CAR_TYPE"))).getAsJsonArray()) {
              carTypes.add(mGson.fromJson(jsonElement, Dict.class));
            }

            for (JsonElement jsonElement : new JsonParser()
                .parse(mGson.toJson(mapBaseJson.getData().get("DICT_ORDER_STATE")))
                .getAsJsonArray()) {
              orderStates.add(mGson.fromJson(jsonElement, Dict.class));
            }

            for (JsonElement jsonElement : new JsonParser()
                .parse(mGson.toJson(mapBaseJson.getData().get("DICT_CAR_OUT_TYPE")))
                .getAsJsonArray()) {
              carOutTypes.add(mGson.fromJson(jsonElement, Dict.class));
            }
            for (JsonElement jsonElement : new JsonParser()
                .parse(mGson.toJson(mapBaseJson.getData().get("CAR_ENTRY_ARR"))).getAsJsonArray()) {
              carOutStates.add(mGson.fromJson(jsonElement, Dict.class));
            }

            SPUtils.getInstance().put("carTypes", mGson.toJson(carTypes));

            SPUtils.getInstance().put("carStates", mGson.toJson(orderStates));
            SPUtils.getInstance().put("carOutTypes", mGson.toJson(carOutTypes));
            SPUtils.getInstance().put("carOutStates", mGson.toJson(carOutStates));

          }


        });
  }


  public void checkAppUpdate() {
    mModel.getAppVersionInfo().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Version>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Version> stringBaseJson) {
            if (stringBaseJson.isSuccess()) {
              Version version = stringBaseJson.getData();
              if (Integer.parseInt(stringBaseJson.getData().getCode()) > AppUtils
                  .getAppVersionCode()) {
                Builder builder = new Builder(mRootView.getActivity());
                builder.title("新版本V" + stringBaseJson.getData().getNo())
                    .content(version.getRemark())
                    .positiveText("确认");
                if (!StringUtils.isEmpty(version.getForceCode())
                    && AppUtils.getAppVersionCode() <= Integer
                    .parseInt(version.getForceCode())) {
                  builder.cancelable(false).autoDismiss(false);

                } else {
                  builder.cancelable(true).autoDismiss(true).negativeText("取消").negativeColor(
                      ArmsUtils.getColor(mRootView.getActivity(), R.color.empty_color));

                }
                builder.onPositive(new SingleButtonCallback() {
                  @Override
                  public void onClick(@NonNull MaterialDialog dialog,
                      @NonNull DialogAction which) {

                    Uri uri = Uri.parse(version.getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    mRootView.launchActivity(intent);
                  }
                })
                ;
                builder.build().show();
              }

            }


          }


        });//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁

  }

}
