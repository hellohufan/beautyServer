package com.irontower.gcbuser.mvp.contract;

import android.support.v4.app.Fragment;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.ChartData;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import io.reactivex.Observable;
import java.util.List;
import java.util.Map;


public interface UserCarTimeContract {

  interface View extends IView {

    Fragment getFragment();

    void toTop();


    void initChartData(List<ChartData> shortData, List<ChartData> longData);
  }

  //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
  interface Model extends IModel {

    Observable<BaseJson<Map<String, Object>>> countApplyOrderHalfYear();

  }
}
