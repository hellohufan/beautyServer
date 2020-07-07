package com.irontower.gcbdriver.mvp.model.entity;

/**
 * Created by jianghaifeng on 2017/12/20.
 */

public class Order {

  private String orderCarNo, beginTime, endTime, passenger, passengerTel, beginAddr, endAddr, carNo, remark, orgName, deptName, createTime, endGps, beginGps, realEndTime, realBeginTime, orderNo, useName, tripType;
  private Integer state, orderCarState, passengerNum, distanceType, carType, carUse;

  public String getOrderCarNo() {
    return orderCarNo;
  }

  public void setOrderCarNo(String orderCarNo) {
    this.orderCarNo = orderCarNo;
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

  public String getCarNo() {
    return carNo;
  }

  public void setCarNo(String carNo) {
    this.carNo = carNo;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Integer getCarUse() {
    return carUse;
  }

  public void setCarUse(Integer carUse) {
    this.carUse = carUse;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getOrderCarState() {
    return orderCarState;
  }

  public void setOrderCarState(Integer orderCarState) {
    this.orderCarState = orderCarState;
  }

  public Integer getPassengerNum() {
    return passengerNum;
  }

  public void setPassengerNum(Integer passengerNum) {
    this.passengerNum = passengerNum;
  }

  public Integer getDistanceType() {
    return distanceType;
  }

  public void setDistanceType(Integer distanceType) {
    this.distanceType = distanceType;
  }

  public Integer getCarType() {
    return carType;
  }

  public void setCarType(Integer carType) {
    this.carType = carType;
  }


  public String getEndGps() {
    return endGps;
  }

  public void setEndGps(String endGps) {
    this.endGps = endGps;
  }

  public String getBeginGps() {
    return beginGps;
  }

  public void setBeginGps(String beginGps) {
    this.beginGps = beginGps;
  }

  @Override
  public String toString() {
    return "Order{" +
        "orderCarNo='" + orderCarNo + '\'' +
        ", beginTime='" + beginTime + '\'' +
        ", endTime='" + endTime + '\'' +
        ", passenger='" + passenger + '\'' +
        ", passengerTel='" + passengerTel + '\'' +
        ", beginAddr='" + beginAddr + '\'' +
        ", endAddr='" + endAddr + '\'' +
        ", carNo='" + carNo + '\'' +
        ", remark='" + remark + '\'' +
        ", orgName='" + orgName + '\'' +
        ", deptName='" + deptName + '\'' +
        ", createTime='" + createTime + '\'' +
        ", endGps='" + endGps + '\'' +
        ", beginGps='" + beginGps + '\'' +
        ", state=" + state +
        ", orderCarState=" + orderCarState +
        ", passengerNum=" + passengerNum +
        ", distanceType=" + distanceType +
        ", carType=" + carType +
        ", carUse=" + carUse +
        '}';
  }

  public String getRealEndTime() {
    return realEndTime;
  }

  public void setRealEndTime(String realEndTime) {
    this.realEndTime = realEndTime;
  }

  public String getRealBeginTime() {
    return realBeginTime;
  }

  public void setRealBeginTime(String realBeginTime) {
    this.realBeginTime = realBeginTime;
  }

  public String getOrderNo() {
    return orderNo;
  }

  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
  }

  public String getUseName() {
    return useName;
  }

  public void setUseName(String useName) {
    this.useName = useName;
  }

  public String getTripType() {
    return tripType;
  }

  public void setTripType(String tripType) {
    this.tripType = tripType;
  }
}

