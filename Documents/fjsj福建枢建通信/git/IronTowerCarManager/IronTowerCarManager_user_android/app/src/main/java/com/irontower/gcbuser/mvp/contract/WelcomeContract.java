package com.irontower.gcbuser.mvp.contract;

import android.app.Activity;
import com.jess.arms.mvp.IView;


public interface WelcomeContract {

  //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
  interface View extends IView {

    Activity getActivity();
  }


}
