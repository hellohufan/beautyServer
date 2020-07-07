package com.irontower.gcbuser.mvp.contract;

import android.app.Activity;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.Vehicle;
import com.jess.arms.mvp.IView;
import com.jess.arms.mvp.IModel;
import io.reactivex.Observable;


public interface ChooseVehicleContract {

  //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
  interface View extends IView {

    Activity getActivity();

    void initView(int num, String carType);

  }

  //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
  interface Model extends IModel {

    Observable<BaseRowJson<Vehicle>> getVehicles(String s, int page, int rows,
        String orderBeginTime, String orderEndTime, String useOrgId, String carType,
        String selectedCarId, String feeType, String orderNo);

  }
}
