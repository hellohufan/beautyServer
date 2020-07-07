package com.irontower.gcbuser.mvp.contract;

import android.app.Activity;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.Order;
import com.irontower.gcbuser.mvp.model.entity.OrderCar;
import com.irontower.gcbuser.mvp.model.entity.OrderTrack;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import io.reactivex.Observable;
import java.util.List;
import java.util.Map;
import okhttp3.ResponseBody;


public interface ApproveHistoryDetailContract {

  //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
  interface View extends IView {

    Activity getActivity();

    void init(Order order, List<OrderCar> orderCars, String node, List<OrderTrack> orderTracks,
        List<Attach> attaches);

    RxPermissions getRxpermission();
  }

  //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
  interface Model extends IModel {

    Observable<BaseJson<Map<String, Object>>> getOrderRecordDetail(String orderNo);

    Observable<ResponseBody> getFile(String url);
  }
}
