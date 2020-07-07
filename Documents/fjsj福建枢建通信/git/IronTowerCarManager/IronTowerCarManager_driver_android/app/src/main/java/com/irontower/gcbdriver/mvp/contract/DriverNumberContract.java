package com.irontower.gcbdriver.mvp.contract;

import android.support.v4.app.Fragment;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.irontower.gcbdriver.mvp.model.entity.ChartData;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import io.reactivex.Observable;
import java.util.List;
import java.util.Map;


public interface DriverNumberContract {

  //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
  interface View extends IView {

    Fragment getFragment();

    void toTop();


    void initChartData(List<ChartData> shortData, List<ChartData> longData);
  }

  //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
  interface Model extends IModel {

    Observable<BaseJson<Map<String, Object>>> getCountDriverOrderHalfYear();

  }
}
