package com.irontower.gcbuser.mvp.contract;

import android.app.Activity;
import android.content.Intent;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.FeeType;
import com.irontower.gcbuser.mvp.model.entity.Order;
import com.irontower.gcbuser.mvp.model.entity.OrderCar;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.Observable;
import java.util.List;
import java.util.Map;
import okhttp3.ResponseBody;


public interface DispatchVehicleDetailContract {

  //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
  interface View extends IView {

    Activity getActivity();

    RxPermissions getRxPermissions();

    void init(Order order, List<OrderCar> orderCars, String node,
        List<Attach> attaches);

    void initFeeTypes(List<FeeType> rows, Integer feeType, Intent intent);
  }

  //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
  interface Model extends IModel {

    Observable<BaseJson<Map<String, Object>>> getTodoOrderDetail(String todoId);


    Observable<BaseJson<Object>> rejectUseCar(String s, String todoId);

    Observable<BaseJson<Object>> finishDispatchCar(String todoId);

    Observable<BaseJson<Object>> delDispatchCar(String orderCarNo);

    Observable<BaseJson<Map<String, Object>>> cancelUseCar(String orderCarNo, String remark);

    Observable<ResponseBody> getFile(String url);

    Observable<BaseRowJson<FeeType>> getFeetTypes(String orgId);
  }
}
