package com.irontower.gcbuser.mvp.model.entity;

/**
 * Created by jianghaifeng on 2017/12/22.
 */

public class SubmitOrder {

  private String passenger, passengerTel, passengerNum, beginTime, endTime, beginAddr, endAddr, beginGps, endGps, remark, tripType, orderReason, accompany;
  private Integer useId, distanceType;
  private String json;

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

  public String getPassengerNum() {
    return passengerNum;
  }

  public void setPassengerNum(String passengerNum) {
    this.passengerNum = passengerNum;
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

  public String getBeginGps() {
    return beginGps;
  }

  public void setBeginGps(String beginGps) {
    this.beginGps = beginGps;
  }

  public String getEndGps() {
    return endGps;
  }

  public void setEndGps(String endGps) {
    this.endGps = endGps;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Integer getUseId() {
    return useId;
  }

  public void setUseId(Integer useId) {
    this.useId = useId;
  }

  public Integer getDistanceType() {
    return distanceType;
  }

  public void setDistanceType(Integer distanceType) {
    this.distanceType = distanceType;
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  public String getOrderReason() {
    return orderReason;
  }

  public void setOrderReason(String orderReason) {
    this.orderReason = orderReason;
  }

  @Override
  public String toString() {
    return "SubmitOrder{" +
        "passenger='" + passenger + '\'' +
        ", passengerTel='" + passengerTel + '\'' +
        ", passengerNum='" + passengerNum + '\'' +
        ", beginTime='" + beginTime + '\'' +
        ", endTime='" + endTime + '\'' +
        ", beginAddr='" + beginAddr + '\'' +
        ", endAddr='" + endAddr + '\'' +
        ", beginGps='" + beginGps + '\'' +
        ", endGps='" + endGps + '\'' +
        ", remark='" + remark + '\'' +
        ", tripType='" + tripType + '\'' +
        ", orderReason='" + orderReason + '\'' +
        ", useId=" + useId +
        ", distanceType=" + distanceType +
        ", json='" + json + '\'' +
        '}';
  }

  public String getTripType() {
    return tripType;
  }

  public void setTripType(String tripType) {
    this.tripType = tripType;
  }

  public String getAccompany() {
    return accompany;
  }

  public void setAccompany(String accompany) {
    this.accompany = accompany;
  }
}
