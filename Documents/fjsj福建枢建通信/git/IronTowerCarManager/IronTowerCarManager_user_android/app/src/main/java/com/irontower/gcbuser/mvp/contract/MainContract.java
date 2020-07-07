package com.irontower.gcbuser.mvp.contract;

import android.app.Activity;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.BaseSimpleJson;
import com.irontower.gcbuser.mvp.model.entity.CarUse;
import com.irontower.gcbuser.mvp.model.entity.Version;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.Observable;
import java.io.File;
import java.util.Map;


public interface MainContract {

  //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
  interface View extends IView {

    Activity getActivity();

    RxPermissions getRxPermissions();

  }

  //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
  interface Model extends IModel {

    Observable<BaseJson<Map<String, Object>>> logout();


    Observable<BaseJson<Map<String, Object>>> getDictList();

    Observable<BaseJson<Map<String, Object>>> uploadImage(File file);


    Observable<BaseJson<Version>> getAppVersionInfo();

    Observable<BaseSimpleJson<Map<String, Object>>> isUrgentUser();

    Observable<BaseRowJson<CarUse>> getCarUses();
  }
}
