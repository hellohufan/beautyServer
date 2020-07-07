package com.irontower.gcbuser.mvp.contract;

import android.app.Activity;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.User;
import com.irontower.gcbuser.mvp.model.entity.Version;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.Observable;
import java.io.File;
import java.util.Map;


public interface BasicInfoContract {

  interface View extends IView {

    Activity getActivity();

    RxPermissions getRxPermissions();

    void init(User newUser);
  }

  //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
  interface Model extends IModel {

    Observable<BaseJson<Map<String, Object>>> getBasicInfo(String userNo, String userName,
        String phoneNo);


    Observable<BaseJson<Map<String, Object>>> uploadImage(File file);


    Observable<BaseJson<Version>> getAppVersionInfo();

  }
}
