package com.irontower.gcbuser.mvp.contract;

import android.app.Activity;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.CarCenter;
import com.irontower.gcbuser.mvp.model.entity.FeeType;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import io.reactivex.Observable;
import java.util.List;


public interface RentVehicleContract {

  //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
  interface View extends IView {

    Activity getActivity();

    void init(List<CarCenter> rows);

    void initFeeTypes(List<FeeType> data);
  }

  //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
  interface Model extends IModel {

    Observable<BaseRowJson<FeeType>> getFeetTypes(String orgId);

    Observable<BaseJson<List<CarCenter>>> getCarCenters(boolean rent);

    Observable<BaseJson<Object>> submitRentCar(String todoId, String carType, String orderCarNo,
        String orgId, String feeType);

  }
}
