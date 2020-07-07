package com.irontower.gcbuser.mvp.presenter;

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
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.mvp.contract.MainContract;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.BaseSimpleJson;
import com.irontower.gcbuser.mvp.model.entity.CarUse;
import com.irontower.gcbuser.mvp.model.entity.Dict;
import com.irontower.gcbuser.mvp.model.entity.User;
import com.irontower.gcbuser.mvp.model.entity.Version;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
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
  }


  public void init() {
    PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
      @Override
      public void onRequestPermissionSuccess() {
        //request permission success, do something.
      }

      @Override
      public void onRequestPermissionFailure(List<String> permissions) {
        ArmsUtils.makeText(mRootView.getActivity(),
            ArmsUtils.getString(mRootView.getActivity(), R.string.no_permission));

      }

      @Override
      public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
        ArmsUtils.makeText(mRootView.getActivity(),
            ArmsUtils.getString(mRootView.getActivity(), R.string.no_permission_forerver));
      }
    }, mRootView.getRxPermissions(), mErrorHandler);

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
//            List<Dict> carUses = new ArrayList<>();
            List<Dict> orderStates = new ArrayList<>();
            List<Dict> driverStates = new ArrayList<>();
            List<Dict> carUseStates = new ArrayList<>();
            List<Dict> carOutTypes = new ArrayList<>();

            for (JsonElement jsonElement : new JsonParser()
                .parse(mGson.toJson(mapBaseJson.getData().get("DICT_CAR_TYPE"))).getAsJsonArray()) {
              carTypes.add(mGson.fromJson(jsonElement, Dict.class));
            }
//            for (JsonElement jsonElement : new JsonParser()
//                .parse(mGson.toJson(mapBaseJson.getData().get("DICT_CAR_USE"))).getAsJsonArray()) {
//              carUses.add(mGson.fromJson(jsonElement, Dict.class));
//            }

            for (JsonElement jsonElement : new JsonParser()
                .parse(mGson.toJson(mapBaseJson.getData().get("DICT_ORDER_STATE")))
                .getAsJsonArray()) {
              orderStates.add(mGson.fromJson(jsonElement, Dict.class));
            }
            for (JsonElement jsonElement : new JsonParser()
                .parse(mGson.toJson(mapBaseJson.getData().get("DICT_DRIVER_STATE")))
                .getAsJsonArray()) {
              driverStates.add(mGson.fromJson(jsonElement, Dict.class));
            }
            for (JsonElement jsonElement : new JsonParser()
                .parse(mGson.toJson(mapBaseJson.getData().get("DICT_CAR_USE_STATE")))
                .getAsJsonArray()) {
              carUseStates.add(mGson.fromJson(jsonElement, Dict.class));
            }
            for (JsonElement jsonElement : new JsonParser()
                .parse(mGson.toJson(mapBaseJson.getData().get("DICT_CAR_OUT_TYPE")))
                .getAsJsonArray()) {
              carOutTypes.add(mGson.fromJson(jsonElement, Dict.class));
            }

            SPUtils.getInstance().put("carTypes", mGson.toJson(carTypes));

            SPUtils.getInstance().put("orderStates", mGson.toJson(orderStates));
            SPUtils.getInstance().put("driverStates", mGson.toJson(driverStates));
            SPUtils.getInstance().put("carUseStates", mGson.toJson(carUseStates));
            SPUtils.getInstance().put("carOutTypes", mGson.toJson(carOutTypes));

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
              if (Integer.parseInt(version.getCode()) > AppUtils
                  .getAppVersionCode()) {
                Builder builder = new Builder(mRootView.getActivity());
                builder.title("新版本V" + version.getNo())
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

  public void isUrgentUser() {
    mModel.isUrgentUser().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseSimpleJson<Map<String, Object>>>(mErrorHandler) {
          @Override
          public void onNext(BaseSimpleJson<Map<String, Object>> stringBaseJson) {
            User user = AppUtil.getUser();
            if (stringBaseJson.isSuccess()) {
              user.setUrgentUser(true);
            } else {
              user.setUrgentUser(false);
            }
            AppUtil.saveUser(user);

          }


        });//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁

  }

  public void getCarUses() {
    mModel.getCarUses().subscribeOn(Schedulers.io())
        .retryWhen(new RetryWithDelay(3, 2))
        .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseRowJson<CarUse>>(mErrorHandler) {
          @Override
          public void onNext(BaseRowJson<CarUse> baseRowJson) {
            List<Dict> dictList = new ArrayList<>();
            if (baseRowJson.getRows() != null) {
              for (CarUse carUse : baseRowJson.getRows()) {
                Dict dict = new Dict();
                dict.setName(carUse.getUseName());
                dict.setVal(carUse.getUseId());
                dictList.add(dict);
              }
            }

            SPUtils.getInstance().put("carUses", mGson.toJson(dictList));

//            }
          }
        });

  }
}
