package com.irontower.gcbdriver.app.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * Copyright: (c) 2012,　福建福诺移动通信技术有限公司 All rights reserved。
 * </p>
 * <p>
 * 文件名称：DateUtil.java
 * </p>
 * <p>
 * 描 述： 日期工具包
 * </p>
 *
 * @author chenph
 * @version 1.0 *******************************************************************************
 * <p>
 * 修改记录
 * </p>
 * <p>
 * 修改时间：
 * </p>
 * <p>
 * 修 改 人：
 * </p>
 * <p>
 * 修改内容：
 * </p>
 * @date 2012-11-14
 */
public class DateUtil {

  public static final String DEFAULT_FMT = "yyyy-MM-dd HH:mm:ss";

  public static final String YYYYMMDDHHMISS = "yyyyMMddHHmmss";

  public static final String YYYYMMDD = "yyyyMMdd";

  public static final String YYYY_MM_DD_HH_MI_SS = "yyyy-MM-dd HH:mm:ss";

  public static final String YYYY_MM_DD_HH_MI = "yyyy-MM-dd HH:mm";

  public static final String YYYY_MM_DD = "yyyy-MM-dd";

  public static final String YYYY_MM_DD_E = "yyyy-MM-dd E";

  public static final String E = "E";

  public static final String YYYY_MM = "yyyy-MM";

  public static final String HH_MI = "HH:mm";
  public static final String STRHH_MI = "HH时mm分";
  public static final String STRMM_DD_HH_MI = "MM月dd日HH时mm分";

  public static final String HH_MI_SS = "HH:mm:ss";

  public static final String MM_DD = "MM-dd";

  public static final String STR_YYYY_MM_DD = "yyyy年MM月dd日";

  public static final String STR_YYYY_MM = "yyyy年MM月";

  public static final String HHMISS = "HHmmss";

  public static final String HHMI = "HHmm";

  public static final String STR_YYYY = "yyyy年";

  /**
   * 默认秒差
   */
  public static final int DIFF_DEFAULT = 3;

  public static final int DIFF_HOUR = 1;

  public static final int DIFF_MINUTE = 2;

  public static final int DIFF_SECOND = 3;

  public static final int DIFF_MILLISECOND = 4;

  /**
   * 获取当前时间
   */
  public static Date getNow() {
    return new Date();
  }

  /**
   * 解析日期为数组 格式：{年,月,日,时,分,秒}
   *
   * @param dDate
   * @return
   */

  /**
   * 获取日期
   *
   * @param fmt 日期格式
   * @param dateStr 日期字符
   */
  public static Date getDate(String fmt, String dateStr) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat(fmt);
      return sdf.parse(dateStr);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 获取日期
   *
   * @param dateStr 日期字符
   */
  public static Date getDate(String dateStr) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FMT);
      return sdf.parse(dateStr);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 格式化日期
   *
   * @param fmt 日期格式
   * @param date 日期
   */
  public static String format(String fmt, Date date) {
    if (date == null) {
      return null;
    }
    SimpleDateFormat sdf = new SimpleDateFormat(fmt);
    return sdf.format(date);
  }

  /**
   * 格式化日期
   */
  public static String format(Date date) {
    try {
      return format(DEFAULT_FMT, date);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 格式化日期
   *
   * @param fmt 日期格式
   * @param dateStr 日期字符
   * @param outFmt 输出日期格式
   */
  public static String formatDate(String fmt, String dateStr, String outFmt) {
    try {
      Date date = getDate(fmt, dateStr);
      return format(outFmt, date);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 获取Timestamp
   *
   * @param date 日期
   */
  public static Timestamp getTimestamp(Date date) {
    if (date == null) {
      return null;
    }
    try {
      return new Timestamp(date.getTime());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 获取Timestamp
   *
   * @param fmt 日期格式
   * @param dateStr 日期字符
   */
  public static Timestamp getTimestamp(String fmt, String dateStr) {
    try {
      return new Timestamp(getDate(fmt, dateStr).getTime());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 获取Timestamp
   *
   * @param dateStr 日期字符
   */
  public static Timestamp getTimestamp(String dateStr) {
    try {
      return new Timestamp(getDate(dateStr).getTime());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 获取日期差
   *
   * @param date1 日期1
   * @param date2 日期2
   * @param flag 差值类型标识
   */
  public static long diffDate(Date date1, Date date2, int flag) {
    try {
      long diff = date1.getTime() - date2.getTime();
      long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
      long day = diff / nd;// 计算差多少天
      if (flag == DIFF_HOUR) {
        return diff / 3600000L;
      }
      if (flag == DIFF_MINUTE) {
        return diff / 60000L;
      }
      if (flag == DIFF_SECOND) {
        return diff / 1000L;
      }
      if (flag == DIFF_MILLISECOND) {
        return diff;
      }
      if (flag == 5) {
        return day;
      }
      return 0L;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0L;
  }

  public static long diffDate(Date date1, Date date2) {
    return diffDate(date1, date2, DIFF_DEFAULT);
  }

  public static boolean geDate(Date date1, Date date2, int flag) {
    if (date1 == null) {
      return false;
    } else if (date2 == null) {
      return true;
    }
    return diffDate(date1, date2, flag) > 0;
  }

  public static boolean gtDate(Date date1, Date date2, int flag) {
    if (date1 == null) {
      return false;
    } else if (date2 == null) {
      return true;
    }
    return diffDate(date1, date2, flag) > 0;
  }

  public static boolean geDate(Date date1, Date date2) {
    return geDate(date1, date2, DIFF_DEFAULT);
  }

  public static boolean gtDate(Date date1, Date date2) {
    return gtDate(date1, date2, DIFF_DEFAULT);
  }

  public static Date getMaxDate(Date date1, Date date2) {
    return geDate(date1, date2, DIFF_DEFAULT) ? date1 : date2;
  }

  public static Date getMinDate(Date date1, Date date2) {
    return geDate(date1, date2, DIFF_DEFAULT) ? date2 : date1;
  }

  public static long diffDate(String dateStr1, String dateStr2, int flag) {
    return diffDate(getDate(dateStr1), getDate(dateStr2), flag);
  }

  public static long diffDate(String dateStr1, String dateStr2) {
    return diffDate(getDate(dateStr1), getDate(dateStr2), DIFF_DEFAULT);
  }

  public static boolean geDate(String dateStr1, String dateStr2, int flag) {
    return diffDate(dateStr1, dateStr2, flag) >= 0;
  }

  public static boolean gtDate(String dateStr1, String dateStr2, int flag) {
    return diffDate(dateStr1, dateStr2, flag) > 0;
  }

  public static boolean geDate(String dateStr1, String dateStr2) {
    return geDate(dateStr1, dateStr2, DIFF_DEFAULT);
  }

  public static boolean gtDate(String dateStr1, String dateStr2) {
    return gtDate(dateStr1, dateStr2, DIFF_DEFAULT);
  }

  /**
   * 获取日期部分
   */
  public static int getDate(Date date, int field) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(field);
  }

  /**
   * 根据日期差值获取指定日期
   *
   * @param date 传入日期
   * @param diffYear 年差值
   * @param diffMonth 月差值
   * @param diffDate 天差值
   * @param diffHour 小时差值
   * @param diffMinute 分钟差值
   * @param diffSecond 秒差值
   * @param diffMillisecond 毫秒差值
   */
  public static Date addDate(Date date, int diffYear, int diffMonth, int diffDate, int diffHour,
      int diffMinute, int diffSecond, int diffMillisecond) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.YEAR, diffYear);
    calendar.add(Calendar.MONTH, diffMonth);
    calendar.add(Calendar.DATE, diffDate);
    calendar.add(Calendar.HOUR, diffHour);
    calendar.add(Calendar.MINUTE, diffMinute);
    calendar.add(Calendar.SECOND, diffSecond);
    calendar.add(Calendar.MILLISECOND, diffMillisecond);
    return calendar.getTime();
  }

  public static Date addDateOfYear(Date date, int diffYear) {
    return addDate(date, diffYear, 0, 0, 0, 0, 0, 0);
  }

  public static Date addDateOfMonth(Date date, int diffMonth) {
    return addDate(date, 0, diffMonth, 0, 0, 0, 0, 0);
  }

  public static Date addDateOfDate(Date date, int diffDate) {
    return addDate(date, 0, 0, diffDate, 0, 0, 0, 0);
  }

  public static Date addDateOfHour(Date date, int diffHour) {
    return addDate(date, 0, 0, 0, diffHour, 0, 0, 0);
  }

  public static Date addDateOfMinute(Date date, int diffMinute) {
    return addDate(date, 0, 0, 0, 0, diffMinute, 0, 0);
  }

  public static Date addDateOfSecond(Date date, int diffSecond) {
    return addDate(date, 0, 0, 0, 0, 0, diffSecond, 0);
  }

  public static Date addDateOfMillisecond(Date date, int diffMillisecond) {
    return addDate(date, 0, 0, 0, 0, 0, 0, diffMillisecond);
  }
}
