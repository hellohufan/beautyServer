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
package com.irontower.gcbdriver.mvp.model.api.service;

import com.irontower.gcbdriver.mvp.model.entity.BaseJson;
import com.irontower.gcbdriver.mvp.model.entity.BaseRowJson;
import com.irontower.gcbdriver.mvp.model.entity.BegGoOut;
import com.irontower.gcbdriver.mvp.model.entity.BegOff;
import com.irontower.gcbdriver.mvp.model.entity.Car;
import com.irontower.gcbdriver.mvp.model.entity.Message;
import com.irontower.gcbdriver.mvp.model.entity.Order;
import com.irontower.gcbdriver.mvp.model.entity.Track;
import com.irontower.gcbdriver.mvp.model.entity.Version;
import io.reactivex.Observable;
import java.util.List;
import java.util.Map;
import okhttp3.FormBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * ================================================
 * 存放通用的一些 API
 * <p>
 * Created by JessYan on 08/05/2016 12:05
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface CommonService {

  String HEADER_API_VERSION = "Content-Type:application/x-www-form-urlencoded";

  String ACCESS_TOKEN = "ACCESS_TOKEN";

//  --------------------------------公共API开始--------------------------------

  //  @Headers({HEADER_API_VERSION})
//  @POST("main/login.do")
//  Observable<BaseJson<Map<String, Object>>> login(@Body FormBody formBody,
//      @Query("uuid") String uuid);
  @Headers({HEADER_API_VERSION})
  @POST("sys/mainex/loginex.do")
  Observable<BaseJson<Map<String, Object>>> login(@Body FormBody formBody,
      @Query("uuid") String uuid);

  @Headers({HEADER_API_VERSION})
  @POST("main/updatePwd.do")
  Observable<BaseJson<Map<String, Object>>> modifyPwd(@Header(ACCESS_TOKEN) String token,
      @Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @POST("losefind/findUserPwd.do")
  Observable<BaseJson<Map<String, Object>>> getSMS(@Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @POST("losefind/resetPassword.do")
  Observable<BaseJson<Integer>> resetPwd(@Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @GET("user/get.do")
  Observable<BaseJson<Map<String, Object>>> getBasicInfo(@Header(ACCESS_TOKEN) String token,
      @Query("userNo") String userNo, @Query("userName") String userName,
      @Query("phoneNo") String phoneNo);


  @Headers({HEADER_API_VERSION})
  @GET("main/logout.do")
  Observable<BaseJson<Map<String, Object>>> logout(@Header(ACCESS_TOKEN) String string);

  @Headers({HEADER_API_VERSION})
  @POST("mobile/userex/updateHead.do")
  Observable<BaseJson<Map<String, Object>>> uploadImage(@Body FormBody formBody,
      @Header(ACCESS_TOKEN) String token);


  @Headers({HEADER_API_VERSION})
  @POST("dict/findByKey.do")
  Observable<BaseJson<Map<String, Object>>> getDictList(@Header(ACCESS_TOKEN) String string,
      @Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @POST("mobile/userex/queryAppVersion.do")
  Observable<BaseJson<Version>> getAppVersionInfo(@Header(ACCESS_TOKEN) String string);

//  --------------------------------公共API结束--------------------------------


  @Headers({HEADER_API_VERSION})
  @POST("carrental/order/queryDriverPage.do")
  Observable<BaseRowJson<Order>> getOrders(@Header(ACCESS_TOKEN) String token, @Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @GET("carrental/order/getOrderDetail.do")
  Observable<BaseJson<Map<String, Object>>> getOrderDetail(@Header(ACCESS_TOKEN) String string,
      @Query("orderCarNo") String orderCarNo);


  @Headers({HEADER_API_VERSION})
  @POST("fleet/driverleave/queryPage.do")
  Observable<BaseRowJson<BegOff>> getBegOff(@Body FormBody body,
      @Header(ACCESS_TOKEN) String string);

  @Headers({HEADER_API_VERSION})
  @POST("fleet/driverleave/add.do")
  Observable<BaseJson<Map<String, Object>>> applyBegOff(@Body FormBody body,
      @Header(ACCESS_TOKEN) String string);

  @Headers({HEADER_API_VERSION})
  @GET("carrental/order/driverOrderState.do")
  Observable<BaseJson<Object>> takeOrder(@Header(ACCESS_TOKEN) String string,
      @Query("state") String state, @Query("orderCarNo") String orderCarNo);

  @Headers({HEADER_API_VERSION})
  @GET("carrental/order/queryHisTrack.do")
  Observable<BaseJson<List<Track>>> getOrderTrack(@Header(ACCESS_TOKEN) String string,
      @Query("orderCarNo") String orderCarNo);

  @Headers({HEADER_API_VERSION})
  @POST("appservice/carentry/queryPage.do")
  Observable<BaseRowJson<BegGoOut>> getGoOut(@Header(ACCESS_TOKEN) String string,
      @Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @POST("report/order/countDriverOrderHalfYear.do")
  Observable<BaseJson<Map<String, Object>>> getChartData(@Header(ACCESS_TOKEN) String string);

  @Headers({HEADER_API_VERSION})
  @POST("report/order/sumDriverMileHalfYear.do")
  Observable<BaseJson<Map<String, Object>>> getSumDriverMileHalfYear(
      @Header(ACCESS_TOKEN) String string);

  @Headers({HEADER_API_VERSION})
  @POST("fleet/info/queryPageCarMgr.do")
  Observable<BaseRowJson<Car>> getCars(@Header(ACCESS_TOKEN) String string,
      @Body FormBody formBody);


  @Headers({HEADER_API_VERSION})
  @POST("appservice/carentry/add.do")
  Observable<BaseJson<Object>> submitGoOut(@Header(ACCESS_TOKEN) String string,
      @Body FormBody formBody);

  @Headers({HEADER_API_VERSION})
  @POST("sys/newsex/queryPage.do")
  Observable<BaseRowJson<Message>> getMessages(@Header(ACCESS_TOKEN) String string,
      @Body FormBody formBody);

  @Headers({HEADER_API_VERSION})
  @POST("sys/newsex/load.do")
  Observable<BaseJson<Message>> getMessageDetail(@Header(ACCESS_TOKEN) String string,
      @Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @POST("appservice/carentry/doCarEntry.do")
  Observable<BaseJson<Map<String, Object>>> changeBegGoOut(@Header(ACCESS_TOKEN) String string,
      @Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @POST("carrental/order/queryDriverReportPage.do")
  Observable<BaseRowJson<Order>> getFeeOrders(@Header(ACCESS_TOKEN) String token,
      @Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @POST("carrental/order/submitReport.do")
  Observable<BaseJson<Object>> submitReport(@Header(ACCESS_TOKEN) String token,
      @Body FormBody body);
}

