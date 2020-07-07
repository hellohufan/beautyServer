package com.irontower.gcbdriver.app.utils;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import com.baidu.mapapi.model.LatLng;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.irontower.gcbdriver.mvp.model.entity.Dict;
import com.irontower.gcbdriver.mvp.model.entity.User;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import sun.misc.BASE64Encoder;

/**
 * Created by jianghaifeng on 2017/12/8.
 */

public class AppUtil {

  private static final String MIME_TYPE_APK = "application/vnd.android.package-archive";

  public static void installApk(File apkFile, Application application) {
    Intent installApkIntent = new Intent();
    installApkIntent.setAction(Intent.ACTION_VIEW);
    installApkIntent.addCategory(Intent.CATEGORY_DEFAULT);
    installApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
      installApkIntent.setDataAndType(
          FileProvider.getUriForFile(application, getFileProviderAuthority(application), apkFile),
          MIME_TYPE_APK);
      installApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    } else {
      installApkIntent.setDataAndType(Uri.fromFile(apkFile), MIME_TYPE_APK);
    }

    if (application.getPackageManager().queryIntentActivities(installApkIntent, 0).size() > 0) {
      application.startActivity(installApkIntent);
    }
  }

  /**
   * 获取FileProvider的auth
   */
  public static String getFileProviderAuthority(Application application) {
    try {
      for (ProviderInfo provider : application.getPackageManager()
          .getPackageInfo(application.getPackageName(), PackageManager.GET_PROVIDERS).providers) {
        if (FileProvider.class.getName().equals(provider.name) && provider.authority
            .endsWith(".FileProvider")) {
          return provider.authority;
        }
      }
    } catch (PackageManager.NameNotFoundException ignore) {
    }
    return null;
  }


  public static User getUser() {
    String str = SPUtils.getInstance().getString("user");
    return new Gson().fromJson(str, User.class);
  }

  public static void saveUser(User user) {
    String str = new Gson().toJson(user);
    SPUtils.getInstance().put("user", str);
  }

  public static String encodeImage(String imgUrl) {
    BASE64Encoder base64Encoder = new BASE64Encoder();
    byte[] rs = null;
    try {
      FileInputStream fis = new FileInputStream(imgUrl);
      rs = new byte[fis.available()];
      fis.read(rs);
      fis.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return base64Encoder.encode(rs);


  }


  public static List<Dict> getCarTypes() {
    List<Dict> dictList = new ArrayList<>();
    Gson gson = new Gson();
    if (SPUtils.getInstance().contains("carTypes")) {
      for (JsonElement jsonElement : new JsonParser()
          .parse(SPUtils.getInstance().getString("carTypes")).getAsJsonArray()) {
        dictList.add(gson.fromJson(jsonElement, Dict.class));
      }
    }

    return dictList;

  }

  public static List<String> getCarTypes1() {
    List<String> dictList = new ArrayList<>();
    Gson gson = new Gson();
    if (SPUtils.getInstance().contains("carTypes")) {
      for (JsonElement jsonElement : new JsonParser()
          .parse(SPUtils.getInstance().getString("carTypes")).getAsJsonArray()) {
        dictList.add(gson.fromJson(jsonElement, Dict.class).getName());
      }
    }

    return dictList;

  }


  public static List<String> getCarState1() {
    List<String> dictList = new ArrayList<>();
    Gson gson = new Gson();
    if (SPUtils.getInstance().contains("carStates")) {
      for (JsonElement jsonElement : new JsonParser()
          .parse(SPUtils.getInstance().getString("carStates")).getAsJsonArray()) {
        dictList.add(gson.fromJson(jsonElement, Dict.class).getName());
      }
    }

    return dictList;

  }

  public static List<String> getCarOutType1() {
    List<String> dictList = new ArrayList<>();
    Gson gson = new Gson();
    if (SPUtils.getInstance().contains("carOutTypes")) {
      for (JsonElement jsonElement : new JsonParser()
          .parse(SPUtils.getInstance().getString("carOutTypes")).getAsJsonArray()) {
        dictList.add(gson.fromJson(jsonElement, Dict.class).getName());
      }
    }

    return dictList;

  }

  public static List<String> getCarOutStates1() {
    List<String> dictList = new ArrayList<>();
    Gson gson = new Gson();
    if (SPUtils.getInstance().contains("carOutStates")) {
      for (JsonElement jsonElement : new JsonParser()
          .parse(SPUtils.getInstance().getString("carOutStates")).getAsJsonArray()) {
        dictList.add(gson.fromJson(jsonElement, Dict.class).getName());
      }
    }

    return dictList;

  }

  public static List<Dict> getCarOutType() {
    List<Dict> dictList = new ArrayList<>();
    Gson gson = new Gson();
    if (SPUtils.getInstance().contains("carOutTypes")) {
      for (JsonElement jsonElement : new JsonParser()
          .parse(SPUtils.getInstance().getString("carOutTypes")).getAsJsonArray()) {
        dictList.add(gson.fromJson(jsonElement, Dict.class));
      }
    }

    return dictList;

  }

  public static List<Dict> getCarState() {
    List<Dict> dictList = new ArrayList<>();
    Gson gson = new Gson();
    if (SPUtils.getInstance().contains("carStates")) {
      for (JsonElement jsonElement : new JsonParser()
          .parse(SPUtils.getInstance().getString("carStates")).getAsJsonArray()) {
        dictList.add(gson.fromJson(jsonElement, Dict.class));
      }
    }

    return dictList;

  }

  public static List<Dict> getCarOutState() {
    List<Dict> dictList = new ArrayList<>();
    Gson gson = new Gson();
    if (SPUtils.getInstance().contains("carOutStates")) {
      for (JsonElement jsonElement : new JsonParser()
          .parse(SPUtils.getInstance().getString("carOutStates")).getAsJsonArray()) {
        dictList.add(gson.fromJson(jsonElement, Dict.class));
      }
    }

    return dictList;

  }


  public static String getDictType(String name, List<Dict> dicts) {
    for (Dict dict : dicts) {
      if (StringUtils.equals(dict.getName(), name)) {
        return dict.getVal();
      }
    }
    return null;

  }

  public static String getDictName(String type, List<Dict> dicts) {
    for (Dict dict : dicts) {
      if (StringUtils.equals(dict.getVal(), type)) {
        return dict.getName();
      }
    }
    return null;

  }


  public static List<Dict> getDistanceTypes() {
    List<Dict> dictList = new ArrayList<>();
    Dict dict1 = new Dict();
    dict1.setName("短途");
    dict1.setVal("2");
    Dict dict2 = new Dict();
    dict2.setName("长途");
    dict2.setName("3");
    dictList.add(dict1);
    dictList.add(dict2);
    return dictList;
  }

  public static List<String> getDistanceTypes1() {
    List<String> distanceTypes = new ArrayList<>();
    distanceTypes.add("短途");
    distanceTypes.add("长途");
    return distanceTypes;

  }

  public static LatLng getLatLng(String str) {
    if (StringUtils.isEmpty(str)) {
      return null;
    }
    String[] strings = str.split(",");
    return new LatLng(Double.parseDouble(strings[1]), Double.parseDouble(strings[0]));
  }

  public static int getMonthIndex(String month) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(DateUtil.getDate(DateUtil.YYYY_MM, month));
    return calendar.get(Calendar.MONTH) + 1;
  }


}
