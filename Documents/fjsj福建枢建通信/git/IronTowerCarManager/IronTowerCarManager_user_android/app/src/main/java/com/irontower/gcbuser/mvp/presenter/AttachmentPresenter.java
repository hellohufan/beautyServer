package com.irontower.gcbuser.mvp.presenter;

import android.app.Application;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.mvp.contract.AttachmentContract;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import org.simple.eventbus.EventBus;


@ActivityScope
public class AttachmentPresenter extends
    BasePresenter<AttachmentContract.Model, AttachmentContract.View> {

  @Inject
  RxErrorHandler mErrorHandler;
  @Inject
  Application mApplication;
  @Inject
  ImageLoader mImageLoader;
  @Inject
  AppManager mAppManager;

  @Inject
  public AttachmentPresenter(AttachmentContract.Model model, AttachmentContract.View rootView) {
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


  public void uploadFile(Map<String, Object> fileMap,
      List<Attach> realAttachList) {
    mModel.uploadFile(fileMap).subscribeOn(Schedulers.io())
        .doOnSubscribe(disposable -> mRootView.showLoading())
        .observeOn(AndroidSchedulers.mainThread()).doFinally(() -> {
      mRootView.hideLoading();
    }).compose(RxLifecycleUtils.bindToLifecycle(mRootView))
        .subscribe(new ErrorHandleSubscriber<BaseJson<List<Attach>>>(mErrorHandler) {
          @Override
          public void onNext(BaseJson<List<Attach>> stringBaseJson) {
            if (stringBaseJson.isSuccess()) {

              ArmsUtils
                  .makeText(mRootView.getActivity(), "附件上传成功.");
              Map<String, Object> map = new HashMap<>();
              map.put("attachMap", fileMap);
              map.put("attachList", stringBaseJson.getData());
              map.put("realAttachList", realAttachList);
              EventBus.getDefault().post(map, EventBusTags.UPLOAD_FILE_SUCCESS);
              mRootView.killMyself();

            } else {

              ArmsUtils
                  .makeText(mRootView.getActivity(), "附件上传失败.");
            }
          }

        });


  }
}
