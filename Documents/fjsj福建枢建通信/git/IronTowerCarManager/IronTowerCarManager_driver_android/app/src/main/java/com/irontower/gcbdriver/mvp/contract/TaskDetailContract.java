package com.irontower.gcbdriver.mvp.contract;

import android.app.Activity;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.irontower.gcbdriver.mvp.model.entity.Order;
import com.irontower.gcbdriver.mvp.model.entity.OrderCar;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.Observable;
import java.util.List;
import java.util.Map;


public interface TaskDetailContract {

  //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
  interface View extends IView {

    RxPermissions getRxPermissions();

    Activity getActivity();

    void init(Order order,
        List<OrderCar> orderCars);

    void initState(int finalNewState);
  }

  //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
  interface Model extends IModel {

    Observable<BaseJson<Map<String, Object>>> getOrderDetail(String orderCarNo);


    Observable<BaseJson<Object>> takeOrder(String orderCarNo, int state, String feeType);
  }
}
