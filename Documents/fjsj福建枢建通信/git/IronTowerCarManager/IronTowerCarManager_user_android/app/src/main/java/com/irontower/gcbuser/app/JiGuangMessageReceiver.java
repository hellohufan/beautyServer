package com.irontower.gcbuser.app;

import android.content.Context;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;
import com.blankj.utilcode.util.SPUtils;

public class JiGuangMessageReceiver extends JPushMessageReceiver {
  @Override
  public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
    super.onTagOperatorResult(context, jPushMessage);

    if (jPushMessage.getErrorCode() != 0) {
      if (SPUtils.getInstance().contains("isLogin")) {
        JPushInterface.setAlias(context, (int) System.currentTimeMillis(),
            JPushInterface.getUdid(context));

      } else {

        JPushInterface.deleteAlias(context, (int) System.currentTimeMillis());


      }

    }
  }
}
