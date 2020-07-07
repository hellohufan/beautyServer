package com.irontower.gcbuser.mvp.model.entity;

/**
 * Created by jianghaifeng on 2017/12/30.
 */

/**
 * Created by jianghaifeng on 2017/12/30.
 */

public class BegGoOut {

  private String carNo, lastTime, applyDesc, inTime, outTime, applyName, hid, phoneNo, fleetName, realOutTime, realInTime;
  private Integer state, outType;

  public String getCarNo() {
    return carNo;
  }

  public void setCarNo(String carNo) {
    this.carNo = carNo;
  }

  public String getLastTime() {
    return lastTime;
  }

  public void setLastTime(String lastTime) {
    this.lastTime = lastTime;
  }


  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getOutType() {
    return outType;
  }

  public void setOutType(Integer outType) {
    this.outType = outType;
  }


  public BegGoOut() {
  }

  public String getInTime() {
    return inTime;
  }

  public void setInTime(String inTime) {
    this.inTime = inTime;
  }

  public String getOutTime() {
    return outTime;
  }

  public void setOutTime(String outTime) {
    this.outTime = outTime;
  }


  public String getApplyName() {
    return applyName;
  }

  public void setApplyName(String applyName) {
    this.applyName = applyName;
  }

  public String getApplyDesc() {
    return applyDesc;
  }

  public void setApplyDesc(String applyDesc) {
    this.applyDesc = applyDesc;
  }

  public String getHid() {
    return hid;
  }

  public void setHid(String hid) {
    this.hid = hid;
  }

  public String getPhoneNo() {
    return phoneNo;
  }

  public void setPhoneNo(String phoneNo) {
    this.phoneNo = phoneNo;
  }

  public String getFleetName() {
    return fleetName;
  }

  public void setFleetName(String fleetName) {
    this.fleetName = fleetName;
  }

  public String getRealOutTime() {
    return realOutTime;
  }

  public void setRealOutTime(String realOutTime) {
    this.realOutTime = realOutTime;
  }

  public String getRealInTime() {
    return realInTime;
  }

  public void setRealInTime(String realInTime) {
    this.realInTime = realInTime;
  }
}

