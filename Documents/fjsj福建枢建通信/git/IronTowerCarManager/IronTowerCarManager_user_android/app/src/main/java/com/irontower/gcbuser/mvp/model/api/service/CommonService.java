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
package com.irontower.gcbuser.mvp.model.api.service;

import com.irontower.gcbuser.mvp.model.entity.ApproveOrder;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import com.irontower.gcbuser.mvp.model.entity.BaseJson;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.BaseSimpleJson;
import com.irontower.gcbuser.mvp.model.entity.BegGoOut;
import com.irontower.gcbuser.mvp.model.entity.BegOff;
import com.irontower.gcbuser.mvp.model.entity.Car;
import com.irontower.gcbuser.mvp.model.entity.CarCenter;
import com.irontower.gcbuser.mvp.model.entity.CarUse;
import com.irontower.gcbuser.mvp.model.entity.Driver;
import com.irontower.gcbuser.mvp.model.entity.FeeType;
import com.irontower.gcbuser.mvp.model.entity.Message;
import com.irontower.gcbuser.mvp.model.entity.Module;
import com.irontower.gcbuser.mvp.model.entity.MultipleOrder;
import com.irontower.gcbuser.mvp.model.entity.Order;
import com.irontower.gcbuser.mvp.model.entity.Track;
import com.irontower.gcbuser.mvp.model.entity.Vehicle;
import com.irontower.gcbuser.mvp.model.entity.Version;
import io.reactivex.Observable;
import java.util.List;
import java.util.Map;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * ================================================ 存放通用的一些 API
 * <p>
 * Created by JessYan on 08/05/2016 12:05
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface CommonService {

  String HEADER_API_VERSION = "Content-Type:application/x-www-form-urlencoded";
  String ACCESS_TOKEN = "ACCESS_TOKEN";

  //   ------------------公共API开始------------------
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
  @POST("mobile/userex/updateHead.do")
  Observable<BaseJson<Map<String, Object>>> uploadImage(@Body FormBody formBody,
      @Header(ACCESS_TOKEN) String token);

  @Headers({HEADER_API_VERSION})
  @GET("main/logout.do")
  Observable<BaseJson<Map<String, Object>>> logout(@Header(ACCESS_TOKEN) String string);


  @Headers({HEADER_API_VERSION})
  @POST("mobile/userex/queryAppVersion.do")
  Observable<BaseJson<Version>> getAppVersionInfo(@Header(ACCESS_TOKEN) String string,
      @Query("verApp") String str);


  @Headers({HEADER_API_VERSION})
  @POST("dict/findByKey.do")
  Observable<BaseJson<Map<String, Object>>> getDictList(@Header(ACCESS_TOKEN) String string,
      @Body FormBody body);


  @Headers({HEADER_API_VERSION})
  @GET("carrental/order/queryHisTrack.do")
  Observable<BaseJson<List<Track>>> getOrderTrack(@Header(ACCESS_TOKEN) String string,
      @Query("orderCarNo") String orderCarNo);

  //   ------------------公共API结束------------------


  @Headers({HEADER_API_VERSION})
  @POST("mobile/morder/queryMyOrderPage.do")
  Observable<BaseRowJson<MultipleOrder>> getMultipleOrders(@Header(ACCESS_TOKEN) String string,
      @Body FormBody body);


  @Headers({HEADER_API_VERSION})
  @POST("mobile/morder/queryOrderPage.do")
  Observable<BaseRowJson<ApproveOrder>> getApproveOrder(@Header(ACCESS_TOKEN) String string,
      @Body FormBody formBody);


  @Headers({HEADER_API_VERSION})
  @POST("carrental/order/add.do")
  Observable<BaseJson<Map<String, Object>>> submitOrder(@Header(ACCESS_TOKEN) String string,
      @Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @POST("mobile/morder/getOrderDetail.do")
  Observable<BaseJson<Map<String, Object>>> getOrderRecordDetail(
      @Header(ACCESS_TOKEN) String string, @Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @GET("mobile/morder/getTodoOrderDetail.do")
  Observable<BaseJson<Map<String, Object>>> getTodoOrderDetail(@Header(ACCESS_TOKEN) String string,
      @Query("todoId") String todoId);

  @Headers({HEADER_API_VERSION})
  @POST("mywork/dispatch/approval.do")
  Observable<BaseJson<Map<String, Object>>> approveOrder(@Header(ACCESS_TOKEN) String string,
      @Body FormBody formBody);

  @Headers({HEADER_API_VERSION})
  @POST("baseinfo/carinfo/queryComboPage.do")
  Observable<BaseRowJson<Vehicle>> getVehicles(@Header(ACCESS_TOKEN) String string,
      @Body FormBody formBody);


  @Headers({HEADER_API_VERSION})
  @POST("sys/userextend/queryComboPage.do")
  Observable<BaseRowJson<Driver>> getDrivers(@Header(ACCESS_TOKEN) String string,
      @Body FormBody formBody);


  @Headers({HEADER_API_VERSION})
  @POST("mywork/dispatch/dispatchCar.do")
  Observable<BaseJson<Object>> submitDispatchCar(@Header(ACCESS_TOKEN) String string,
      @Body FormBody formBody);

  @Headers({HEADER_API_VERSION})
  @POST("mywork/dispatch/cancelOrderCar.do")
  Observable<BaseJson<Map<String, Object>>> cancelOrderCar(@Header(ACCESS_TOKEN) String string,
      @Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @POST("carrental/order/update.do")
  Observable<BaseJson<Map<String, Object>>> updateOrder(@Header(ACCESS_TOKEN) String string,
      @Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @POST("appservice/evaluate/driverScore.do")
  Observable<BaseJson<Object>> evaluate(@Header(ACCESS_TOKEN) String string,
      @Body FormBody formBody);

  @Headers({HEADER_API_VERSION})
  @POST("fleet/info/queryPageCarMgr.do")
  Observable<BaseRowJson<Car>> getCars(@Header(ACCESS_TOKEN) String string, @Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @POST("mobile/userex/queryPageDriverMgr.do")
  Observable<BaseRowJson<Driver>> getManageDrivers(@Header(ACCESS_TOKEN) String string,
      @Body FormBody body);


  @Headers({HEADER_API_VERSION})
  @POST("carrental/feetype/queryPage.do")
  Observable<BaseRowJson<FeeType>> getFeetTypes(@Header(ACCESS_TOKEN) String string,
      @Query("orgId") String orgId);

  @Headers({HEADER_API_VERSION})
  @POST("baseinfo/department/queryCarCenter.do")
  Observable<BaseJson<List<CarCenter>>> getCarCenters(@Header(ACCESS_TOKEN) String string,
      @Body FormBody formBody);

  @Headers({HEADER_API_VERSION})
  @POST("mywork/dispatch/rentCar.do")
  Observable<BaseJson<Object>> submitRentCar(@Header(ACCESS_TOKEN) String string,
      @Body FormBody formBody);


  @Headers({HEADER_API_VERSION})
  @POST("mywork/dispatch/reject.do")
  Observable<BaseJson<Object>> rejectUseCar(@Header(ACCESS_TOKEN) String string,
      @Body FormBody formBody);

  @Headers({HEADER_API_VERSION})
  @POST("fleet/driverleave/queryPage.do")
  Observable<BaseRowJson<BegOff>> getBegOffs(@Header(ACCESS_TOKEN) String string,
      @Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @POST("fleet/driverleave/approval.do")
  Observable<BaseJson<Object>> approvalBegOff(@Header(ACCESS_TOKEN) String string,
      @Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @GET("mobile/userex/loadAppRight.do")
  Observable<BaseJson<List<Module>>> get(@Header(ACCESS_TOKEN) String string);

  @Headers({HEADER_API_VERSION})
  @POST("appservice/carentry/queryPage.do")
  Observable<BaseRowJson<BegGoOut>> getGoOuts(@Header(ACCESS_TOKEN) String string,
      @Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @POST("report/order/countApplyOrderHalfYear.do")
  Observable<BaseJson<Map<String, Object>>> countApplyOrderHalfYear(
      @Header(ACCESS_TOKEN) String string);

  @Headers({HEADER_API_VERSION})
  @POST("report/order/sumApplyPriceHalfYear.do")
  Observable<BaseJson<Map<String, Object>>> getSumDriverMileHalfYear(
      @Header(ACCESS_TOKEN) String string);

  @Headers({HEADER_API_VERSION})
  @POST("sys/newsex/queryPage.do")
  Observable<BaseRowJson<Message>> getMessages(@Header(ACCESS_TOKEN) String string,
      @Body FormBody formBody);

  @Headers({HEADER_API_VERSION})
  @POST("sys/newsex/load.do")
  Observable<BaseJson<Message>> getMessageDetail(@Header(ACCESS_TOKEN) String string,
      @Body FormBody body);


  @Headers({HEADER_API_VERSION})
  @POST("appservice/carentry/check.do")
  Observable<BaseJson<Object>> approvalBegGoOut(@Header(ACCESS_TOKEN) String string,
      @Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @POST("mobile/morder/queryMyOrderPage.do")
  Observable<BaseRowJson<Order>> getOrders(@Header(ACCESS_TOKEN) String string,
      @Body FormBody body);

  @Headers({HEADER_API_VERSION})
  @POST("mywork/dispatch/insideDispatchFinish.do")
  Observable<BaseJson<Object>> finishDispatchCar(@Header(ACCESS_TOKEN) String string,
      @Body FormBody formBody);

  @Headers({HEADER_API_VERSION})
  @POST("mywork/dispatch/delOrderCar.do")
  Observable<BaseJson<Object>> delDispatchCar(@Header(ACCESS_TOKEN) String string,
      @Body FormBody formBody);

  @Headers({HEADER_API_VERSION})
  @POST("carrental/order/withdraw.do")
  Observable<BaseJson<Map<String, Object>>> withDrawOrder(@Header(ACCESS_TOKEN) String string,
      @Body FormBody body);


  /**
   * 是否白名单用户
   *
   * @param string 请求头
   * @return result
   */
  @Headers({HEADER_API_VERSION})
  @GET("carrental/urgentuser/isUrgentUser.do")
  Observable<BaseSimpleJson<Map<String, Object>>> isUrgentUser(@Header(ACCESS_TOKEN) String string);


  @Multipart
  @POST("attachment/add.do")
  Observable<BaseJson<List<Attach>>> uploadFile(@Header(ACCESS_TOKEN) String string,
      @PartMap Map<String, RequestBody> params);

  @Headers({HEADER_API_VERSION})
  @POST("monitor/dev/queryRealPos.do")
  Observable<BaseJson<Map<String, Object>>> getDriverPosition(@Header(ACCESS_TOKEN) String string,
      @Body FormBody body);

  @GET
  @Streaming
  Observable<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);


  @Headers({HEADER_API_VERSION})
  @GET("baseinfo/caruse/queryPage.do")
  Observable<BaseRowJson<CarUse>> getCarUses(@Header(ACCESS_TOKEN) String string);


  @Headers({HEADER_API_VERSION})
  @GET("carrental/order/del.do")
  Observable<BaseJson<Object>> delOrder(@Header(ACCESS_TOKEN) String string,
      @Query("eids") String orderNo);

  @Headers({HEADER_API_VERSION})
  @GET("monitor/appmap/loadTreeData.do")
  Observable<List<Map<String, Object>>> getTreeData(@Header(ACCESS_TOKEN) String string);

  @Headers({HEADER_API_VERSION})
  @GET("monitor/appmap/loadTreeData.do")
  Observable<List<Map<String, Object>>> loadTreeChildren(@Header(ACCESS_TOKEN) String string,
      @Query("id") String uuid);

  @Headers({HEADER_API_VERSION})
  @GET("monitor/dev/queryRealPos.do")
  Observable<BaseJson<Map<String, Object>>> getCarPositionData(@Header(ACCESS_TOKEN) String string,
      @Query("refId") String refId, @Query("sendCmd") boolean sendCmd);

  @Headers({HEADER_API_VERSION})
  @GET("monitor/gatewayhis/queryPagePostion.do")
  Observable<BaseRowJson<Track>> getTrackData(@Header(ACCESS_TOKEN) String string,
      @Query("noRunCount") boolean noRunCount,
      @Query("rows") int rows,
      @Query("refId") String carId,
      @Query("carNo") String carNo,
      @Query("bTime") String bTime,
      @Query("eTime") String eTime);
}