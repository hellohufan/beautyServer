/**
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.irontower.gcbdriver.app;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.ui.activity.LoginActivity;
import com.jess.arms.http.GlobalHttpHandler;
import com.jess.arms.utils.ArmsUtils;
import java.util.Map;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * ================================================
 * 展示 {@link GlobalHttpHandler} 的用法
 * <p>
 * Created by JessYan on 04/09/2017 17:06
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class GlobalHttpHandlerImpl implements GlobalHttpHandler {

  private Context context;

  public GlobalHttpHandlerImpl(Context context) {
    this.context = context;
  }

  @Override
  public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain,
      Response response) {
    if (!TextUtils.isEmpty(httpResult) && httpResult.length() < 100) {
      Gson gson = ArmsUtils.obtainAppComponentFromContext(context).gson();
      try {
        Map<String, Object> map = gson.fromJson(httpResult, Map.class);
        if (map.containsKey("code")) {
          Double code = Double.parseDouble(String.valueOf(map.get("code")));
          if (Math.ceil(code) == 401) {

            new Thread() {

              @Override
              public void run() {
                Looper.prepare();
                new Handler().post(new Runnable() {
                  @Override
                  public void run() {
                    new MaterialDialog.Builder(
                        ArmsUtils.obtainAppComponentFromContext(context).appManager()
                            .getTopActivity())
                        .title("提示").content("尚未登录或登录超时,是否跳转到登录页面?")
                        .positiveText("确认")
                        .negativeText("取消")
                        .negativeColor(ArmsUtils.getColor(context, R.color.empty_color))
                        .onPositive(new SingleButtonCallback() {
                          @Override
                          public void onClick(@NonNull MaterialDialog dialog,
                              @NonNull DialogAction which) {
//
                            dialog.dismiss();
                            JPushInterface.resumePush(context);
                            JPushInterface.deleteAlias(
                                ArmsUtils.obtainAppComponentFromContext(context).appManager()
                                    .getCurrentActivity(), (int) System.currentTimeMillis());

                            SPUtils.getInstance().clear();
                            ArmsUtils.startActivity(
                                ArmsUtils.obtainAppComponentFromContext(context).appManager()
                                    .getCurrentActivity(), LoginActivity.class);
                            ArmsUtils.killAll();


                          }
                        })
                        .show()
                    ;

                  }
                });//在子线程中直接去new 一个handler
                Looper.loop();
              }
            }.start();


          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      }


    }

    return response;
  }

  // 这里可以在请求服务器之前可以拿到request,做一些操作比如给request统一添加token或者header以及参数加密等操作
  @Override
  public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
                    /* 如果需要再请求服务器之前做一些操作,则重新返回一个做过操作的的request如增加header,不做操作则直接返回request参数
                       return chain.request().newBuilder().header("token", tokenId)
                              .build(); */
    return request;
  }
}
