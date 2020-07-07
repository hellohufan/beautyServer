package com.irontower.gcbdriver.mvp.ui.widget;

import android.text.InputFilter;
import android.text.Spanned;

public class LengthFilter implements InputFilter {

  private static final int DECIMAL_DIGITS = 2;//小数的位数

  @Override
  public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
      int dend) {

    // 删除等特殊字符，直接返回
    if ("".equals(source.toString())) {
      return null;
    }
    if (source.length() > 1) {
      return "";
    }

    String dValue = dest.toString();
    String[] splitArray = dValue.split("\\.");
    if (splitArray.length > 1) {
      String dotValue = splitArray[1];
      //获取小数点“.”在字符串中的index值
      int dotIndex = dValue.indexOf(".");
      if (dotValue.length() >= DECIMAL_DIGITS && dstart > dotIndex) {
        return "";
      }
    }

    return null;
  }
}
