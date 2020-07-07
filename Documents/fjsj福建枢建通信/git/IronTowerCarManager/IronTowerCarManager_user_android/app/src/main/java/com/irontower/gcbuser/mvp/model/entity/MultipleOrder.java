package com.irontower.gcbuser.mvp.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by jianghaifeng on 2017/12/13.
 */

public class MultipleOrder implements MultiItemEntity {

  public static final int FINISH = 1;
  public static final int CANCEL = 2;

  @Override
  public int getItemType() {
    return (state > 8 && state < 13) ? 1 : 2;
  }


  private String orderNo, beginTime, endTime, passenger, passengerTel, beginAddr, endAddr;
  private Integer state;


  public static int getFINISH() {
    return FINISH;
  }

  public static int getCANCEL() {
    return CANCEL;
  }

  public String getOrderNo() {
    return orderNo;
  }

  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
  }

  public String getBeginTime() {
    return beginTime;
  }

  public void setBeginTime(String beginTime) {
    this.beginTime = beginTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public String getPassenger() {
    return passenger;
  }

  public void setPassenger(String passenger) {
    this.passenger = passenger;
  }

  public String getPassengerTel() {
    return passengerTel;
  }

  public void setPassengerTel(String passengerTel) {
    this.passengerTel = passengerTel;
  }

  public String getBeginAddr() {
    return beginAddr;
  }

  public void setBeginAddr(String beginAddr) {
    this.beginAddr = beginAddr;
  }

  public String getEndAddr() {
    return endAddr;
  }

  public void setEndAddr(String endAddr) {
    this.endAddr = endAddr;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  @Override
  public String toString() {
    return "MultipleOrder{" +
        "orderNo='" + orderNo + '\'' +
        ", beginTime='" + beginTime + '\'' +
        ", endTime='" + endTime + '\'' +
        ", passenger='" + passenger + '\'' +
        ", passengerTel='" + passengerTel + '\'' +
        ", beginAddr='" + beginAddr + '\'' +
        ", endAddr='" + endAddr + '\'' +
        ", state=" + state +
        '}';
  }
}
