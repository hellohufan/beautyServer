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
package com.irontower.gcbuser.app;

import org.simple.eventbus.EventBus;

/**
 * ================================================
 * 放置 {@link EventBus} 的 Tag ,便于检索
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#3.5">EventBusTags wiki 官方文档</a>
 * Created by JessYan on 8/30/2016 16:39
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface EventBusTags {

  String REMARK = "REMARK";
  String REASON = "REASON";
  String MOBILE = "MOBILE";
  String STARTCITY = "STARTCITY";
  String ENDCITY = "ENDCITY";
  String ENDCITY2 = "ENDCITY2";
  String ENDCITY3 = "ENDCITY3";
  String STARTCITY2 = "STARTCITY2";
  String STARTCITY3 = "STARTCITY3";
  String STARTCITY4 = "STARTCITY4";
  String CHANGEUSERNAME = "CHANGEUSERNAME";
  String CHANGEHEADURL = "CHANGEHEADURL";
  String APPROVE_ORDER = "APPROVE_ORDER";
  String DISPATCHCARSUCCESS = "DISPATCHCARSUCCESS";
  String WITH_DRAW_ORDER = "WITH_DRAW_ORDER";
  String CANCEL_USE_CAR = "CANCEL_USE_CAR";
  String UPDATE_ORDER = "UPDATE_ORDER";
  String EVALUATE_SUCCESS = "EVALUATE_SUCCESS";
  String RENT_CAR_SUCCESS = "RENT_CAR_SUCCESS";
  String REJECT_USE_CAR = "REJECT_USE_CAR";

  String FINISH_DISPATCH_CAR = "FINISH_DISPATCH_CAR";
  String DEL_DISPATCH_CAR = "DEL_DISPATCH_CAR";
  String TIMESELECT = "TIMESELECT";
  String CHOOSEATTACHMENT = "CHOOSEATTACHMENT";
  String UPLOAD_FILE_SUCCESS = "UPLOAD_FILE_SUCCESS";
  String ACCOMPANY = "ACCOMPANY";
  String DEL_ORDER = "DEL_ORDER";
}
