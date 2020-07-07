package com.irontower.gcbuser.mvp.contract;

import android.app.Activity;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.Driver;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import io.reactivex.Observable;


public interface ChooseDriverContract {

  //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
  interface View extends IView {

    Activity getActivity();

    void showLoading1();

    void hideLoading1();

  }

  //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
  interface Model extends IModel {

    Observable<BaseRowJson<Driver>> getDrivers(String s, int page, int rows, int type,
        String orderBeginTime,
        String orderEndTime, String orgId, String selectedDriverId);

    Observable<BaseJson<Object>> submit(String todoId, String orderCarNo, String carId,
        String driverId, String carType, String feeType);


  }
}
