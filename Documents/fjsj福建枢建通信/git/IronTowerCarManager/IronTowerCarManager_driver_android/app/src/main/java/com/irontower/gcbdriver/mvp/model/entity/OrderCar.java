package com.irontower.gcbdriver.mvp.model.entity;

/**
 * Created by jianghaifeng on 2017/12/24.
 */

public class OrderCar {

  private Integer carType, state;
  private String orderCarNo, feeType;

  public Integer getCarType() {
    return carType;
  }

  public void setCarType(Integer carType) {
    this.carType = carType;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getOrderCarNo() {
    return orderCarNo;
  }

  public void setOrderCarNo(String orderCarNo) {
    this.orderCarNo = orderCarNo;
  }

  public String getFeeType() {
    return feeType;
  }

  public void setFeeType(String feeType) {
    this.feeType = feeType;
  }
}
