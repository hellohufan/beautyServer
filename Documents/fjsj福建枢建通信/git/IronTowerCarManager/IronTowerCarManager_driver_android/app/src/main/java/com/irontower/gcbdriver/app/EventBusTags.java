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

  String CHANGEUSERNAME = "CHANGEUSERNAME";
  String CHANGEHEADURL = "CHANGEHEADURL";
  String REFRESHBEGOFF = "REFRESHBEGOFF";
  String TAKE_ORDER_SUCCESS = "TAKE_ORDER_SUCCESS";
  String END_ORDER = "END_ORDER";
  String JUMP_TO_ORDER = "JUMP_TO_ORDER";
  String CHOOSE_CAR_SUCCESS = "CHOOSE_CAR_SUCCESS";
  String APPLY_GO_OUT_SUCCESS = "APPLY_GO_OUT_SUCCESS";
  String CHANGE_GO_OUT_SUCCESS = "CHANGE_GO_OUT_SUCCESS";
  String SUBMITREPORT = "SUBMITREPORT";
}
