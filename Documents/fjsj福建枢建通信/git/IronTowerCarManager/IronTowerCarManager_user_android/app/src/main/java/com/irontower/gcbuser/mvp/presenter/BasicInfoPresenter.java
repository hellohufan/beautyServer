package com.irontower.gcbuser.mvp.presenter;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.app.utils.ConstantUtils;
import com.irontower.gcbuser.mvp.contract.BasicInfoContract;
import com.irontower.gcbuser.mvp.model.api.Api;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.User;
import com.irontower.gcbuser.mvp.model.entity.Version;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.RxLifecycleUtils;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import org.simple.eventbus.EventBus;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


@ActivityScope
public class BasicInfoPresenter extends
    BasePresenter<BasicInfoContract.Model, BasicInfoContract.View> {

  private RxErrorHandler mErrorHandler;
  private Application mApplication;
  private Gson mGson;


  @Inject
  public BasicInfoPresenter(BasicInfoContract.Model model, BasicInfoContract.View rootView
      , RxErrorHandler handler, Application application
      , Gson gson) {
    super(model, rootView);
    this.mErrorHandler = handler;
    this.mApplication = application;
    this.mGson = gson;

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;
    this.mApplication = null;
    this.mGson = null;
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
                new MaterialDialog.Builder(mRootView.getActivity())
                    .title("新版本V" + stringBaseJson.getData().getNo())
                    .content(version.getRemark())
                    .positiveText("确认")
                    .negativeText("取消")
                    .negativeColor(ArmsUtils.getColor(mRootView.getActivity(), R.color.empty_color))
                    .onPositive(new SingleButtonCallback() {
                      @Override
                      public void onClick(@NonNull MaterialDialog dialog,
                          @NonNull DialogAction which) {

                        Uri uri = Uri.parse(version.getUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        mRootView.launchActivity(intent);
                      }
                    })
                    .show()
                ;
              } else {
                ArmsUtils.makeText(mRootView.getActivity(), "已经是最新版本了");
              }


            }


          }


        });//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁

  }

  public void chooseImage(int which, CircleImageView imageView) {
    switch (which) {
      case 0:
        RxPaparazzo.single(mRootView.getActivity())
            .usingCamera()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(response -> {

              // See response.resultCode() doc
              if (response.resultCode() == Activity.RESULT_OK) {
                if (!ImageUtils.isImage(response.data().getFile().getAbsolutePath())) {
                  ArmsUtils.makeText(mRootView.getActivity(),
                      mApplication.getString(R.string.not_img));
                  return;
                }

                zipFileAndUpload(response.data().getFile(), imageView);
                //启动压缩

              } else if (response.resultCode() == Activity.RESULT_CANCELED) {

              } else {
                ArmsUtils.makeText(mRootView.getActivity(),
                    mApplication.getString(R.string.unknown_error));
              }
            });

        break;
      case 1:
        RxPaparazzo.single(mRootView.getActivity())
            .usingGallery()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(response -> {
              // See response.resultCode() doc

              if (response.resultCode() == Activity.RESULT_OK) {
                if (!ImageUtils.isImage(response.data().getFile().getAbsolutePath())) {
                  ArmsUtils.makeText(mRootView.getActivity(),
                      mApplication.getString(R.string.not_img));
                  return;
                }
                zipFileAndUpload(response.data().getFile(), imageView);

              } else if (response.resultCode() == Activity.RESULT_CANCELED) {
              } else {
                ArmsUtils.makeText(mRootView.getActivity(),
                    mApplication.getString(R.string.unknown_error));
              }
            });
        break;
      default:
        break;

    }

  }


  public void getData(String userNo, String userName, String phoneNo) {
    mModel.getBasicInfo(userNo, userName, phoneNo).subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Map<String, Object>>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Map<String, Object>> stringBaseJson) {
            if (stringBaseJson.isSuccess()) {
              User user = mGson.fromJson(mGson.toJson(stringBaseJson.getData()), User.class);
              User newUser = AppUtil.getUser();
              newUser.setHeadUrl(user.getHeadUrl());
              newUser.setUserNo(user.getUserNo());
              newUser.setPhoneNo(user.getPhoneNo());
              newUser.setSex(user.getSex());
              newUser.setUserName(user.getUserName());
              AppUtil.saveUser(newUser);
              mRootView.init(newUser);


            } else {

              ArmsUtils
                  .makeText(mRootView.getActivity(), stringBaseJson.getMessage());
            }
          }

          @Override
          public void onError(Throwable t) {
            super.onError(t);

          }
        });//
  }

  private void uploadImage(File file, CircleImageView imageView) {
    mModel.uploadImage(file).subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<Map<String, Object>>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<Map<String, Object>> stringBaseJson) {

            if (stringBaseJson.isSuccess()) {
              GlideArms.with(mRootView.getActivity())
                  .load(Api.APP_DOMAIN + stringBaseJson.getData().get("url"))
                  .placeholder(R.mipmap.passenger_photo)
                  .error(R.mipmap.passenger_photo).into(imageView);
              EventBus.getDefault().post(stringBaseJson.getData().get("url"),
                  EventBusTags.CHANGEHEADURL);

            } else {

            }
          }

          @Override
          public void onError(Throwable t) {
            super.onError(t);

          }
        });//
  }

  private void zipFileAndUpload(File file, CircleImageView circleImageView) {
    FileUtils.createOrExistsDir(ConstantUtils.IMAGE_FILE_PATH);
    Luban.with(mRootView.getActivity())
        .load(file)                                   // 传人要压缩的图片列表
        .ignoreBy(100)                                  // 忽略不压缩图片的大小
        .setTargetDir(ConstantUtils.IMAGE_FILE_PATH)                        // 设置压缩后文件存储位置
        .setCompressListener(new OnCompressListener() { //设置回调
          @Override
          public void onStart() {
          }

          @Override
          public void onSuccess(File file) {

            uploadImage(file, circleImageView);
          }

          @Override
          public void onError(Throwable e) {
            // TODO 当压缩过程出现问题时调用
          }
        }).launch();

  }

  public void callPhone() {
    PermissionUtil.callPhone(new PermissionUtil.RequestPermission() {
      @Override
      public void onRequestPermissionSuccess() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:059163509168");
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
    }, mRootView.getRxPermissions(), mErrorHandler);
  }
}