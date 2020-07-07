package com.irontower.gcbuser.mvp.model.entity;

/**
 * Created by jianghaifeng on 2017/12/18.
 */

public class ChartData {

  private String month;
  private Number distanceType, num, num1;

  public String getMonth() {
    return month;
  }

  public void setMonth(String month) {
    this.month = month;
  }


  public void setDistanceType(int distanceType) {
    this.distanceType = distanceType;
  }


  public void setNum(int num) {
    this.num = num;
  }


  public void setNum1(int num1) {
    this.num1 = num1;
  }

  public Number getDistanceType() {
    return distanceType;
  }

  public void setDistanceType(Number distanceType) {
    this.distanceType = distanceType;
  }

  public Number getNum() {
    return num;
  }

  public void setNum(Number num) {
    this.num = num;
  }

  public Number getNum1() {
    return num1;
  }

  public void setNum1(Number num1) {
    this.num1 = num1;
  }
}