package com.irontower.gcbdriver.mvp.model.entity;

import android.os.Parcel;

/**
 * Created by jianghaifeng on 2017/12/30.
 */

public class BegGoOut implements android.os.Parcelable {

  private String carNo, lastTime, applyDesc, inTime, outTime, applyName, hid, phoneNo, fleetName, realOutTime, realInTime, remark;
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


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.carNo);
    dest.writeString(this.lastTime);
    dest.writeString(this.applyDesc);
    dest.writeString(this.inTime);
    dest.writeString(this.outTime);
    dest.writeString(this.applyName);
    dest.writeString(this.hid);
    dest.writeString(this.phoneNo);
    dest.writeString(this.fleetName);
    dest.writeString(this.realOutTime);
    dest.writeString(this.realInTime);
    dest.writeString(this.remark);
    dest.writeValue(this.state);
    dest.writeValue(this.outType);
  }

  protected BegGoOut(Parcel in) {
    this.carNo = in.readString();
    this.lastTime = in.readString();
    this.applyDesc = in.readString();
    this.inTime = in.readString();
    this.outTime = in.readString();
    this.applyName = in.readString();
    this.hid = in.readString();
    this.phoneNo = in.readString();
    this.fleetName = in.readString();
    this.realOutTime = in.readString();
    this.realInTime = in.readString();
    this.remark = in.readString();
    this.state = (Integer) in.readValue(Integer.class.getClassLoader());
    this.outType = (Integer) in.readValue(Integer.class.getClassLoader());
  }

  public static final Creator<BegGoOut> CREATOR = new Creator<BegGoOut>() {
    @Override
    public BegGoOut createFromParcel(Parcel source) {
      return new BegGoOut(source);
    }

    @Override
    public BegGoOut[] newArray(int size) {
      return new BegGoOut[size];
    }
  };
}
