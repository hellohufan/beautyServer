package com.irontower.gcbdriver.mvp.contract;

import android.app.Activity;
import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import io.reactivex.Observable;
import java.util.Map;


public interface ForgetPasswordContract {

  //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
  interface View extends IView {

    Activity getActivity();

    void disable();

    void able();


  }

  //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
  interface Model extends IModel {

    Observable<BaseJson<Map<String, Object>>> getSMS(String phone);

    Observable<BaseJson<Integer>> resetPwd(String userNo, String vcode, String newPwd);

  }
}
