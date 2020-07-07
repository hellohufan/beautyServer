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
package com.irontower.gcbdriver.mvp.model.api;

/**
 * ================================================
 * 存放一些与 API 有关的东西,如请求地址,请求码等
 * <p>
 * Created by JessYan on 08/05/2016 11:25
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface Api {
    //正式
//    String APP_DOMAIN = "http://www.fjgcgl.org.cn:50001/";
    //铁塔车管家
    String APP_DOMAIN= "http://gcb.fjhtxl.net:81/";
//    测试
//    String APP_DOMAIN = "http://gc2.0.fjhtxl.cn/";

//    String APP_DOMAIN = "http://gccs.fjhtxl.net:81/";


    int REQUEST_SUCCESS = 0;
}
