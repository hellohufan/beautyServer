package com.irontower.gcbuser.app.utils;

import android.os.Environment;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/15.
 */

public class ConstantUtils {
  public  static String APK_FILE_PATH = Environment.getExternalStorageDirectory() + "/GcbUser/Download";
  public  static String IMAGE_FILE_PATH = Environment.getExternalStorageDirectory() + "/GcbUser/Image/";
  public static List<String> getCarTypes(){
    List<String> res = new ArrayList<>();
    res.add("轿车");
    res.add("大型客车");
    res.add("中型客车");
    res.add("越野车");
    res.add("货车");
    res.add("专用车");
    res.add("纯电动客车");
    return res;
  }

  public static List<String> getCarNums() {
    List<String> res = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
      res.add(i + "");
    }
    return res;
  }

  public static List<String> getPassengerNums() {
    List<String> res = new ArrayList<>();
    for (int i = 1; i <= 1000; i++) {
      res.add(i + "");
    }
    return res;
  }

  public static List<String> getJourneryTypes() {
    List<String> res = new ArrayList<>();
    res.add("长途");
    res.add("短途");
    return res;
  }

  public static List<String> getReasons() {
    List<String> res = new ArrayList<>();
    res.add("应急用车");
    res.add("机要用车");
    res.add("集体公务出行");
    res.add("跨区域出差");
    res.add("公务接待");
    res.add("重要会议应急接送");
    res.add("参加中央在闽调研等");
    res.add("紧急就医");
    res.add("老干部服务用车");
    res.add("执法执勤用车");
    res.add("其他公务用车");
    return res;
  }

  public static List<String> getMoneys() {
    List<String> res = new ArrayList<>();
    res.add("常规结算");

    return res;
  }
}
