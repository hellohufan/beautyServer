package com.irontower.gcbuser.mvp.model.entity;

import android.os.Parcel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by jianghaifeng on 2017/12/16.
 */

public class Order implements android.os.Parcelable {

  private String beginTime, endTime, passenger, passengerTel, beginAddr, endAddr, orderNo, orgName, deptName, createTime, remark, beginGps, endGps, tripType, orderReason, useName, accompany;
  private Integer state, distanceType, passengerNum, carUse;
  private Map<String, Integer> map;
  private List<Attach> attachList;


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

  public Integer getDistanceType() {
    return distanceType;
  }

  public void setDistanceType(Integer distanceType) {
    this.distanceType = distanceType;
  }

  public String getOrderNo() {
    return orderNo;
  }

  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
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

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Integer getPassengerNum() {
    return passengerNum;
  }

  public void setPassengerNum(Integer passengerNum) {
    this.passengerNum = passengerNum;
  }

  public Integer getCarUse() {
    return carUse;
  }

  public void setCarUse(Integer carUse) {
    this.carUse = carUse;
  }


  public Order() {
  }


  public Map<String, Integer> getMap() {
    return map;
  }

  public void setMap(Map<String, Integer> map) {
    this.map = map;
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

  public String getTripType() {
    return tripType;
  }

  public void setTripType(String tripType) {
    this.tripType = tripType;
  }

  public String getOrderReason() {
    return orderReason;
  }

  public void setOrderReason(String orderReason) {
    this.orderReason = orderReason;
  }

  public List<Attach> getAttachList() {
    return attachList;
  }

  public void setAttachList(
      List<Attach> attachList) {
    this.attachList = attachList;
  }

  public String getUseName() {
    return useName;
  }

  public void setUseName(String useName) {
    this.useName = useName;
  }


  public String getAccompany() {
    return accompany;
  }

  public void setAccompany(String accompany) {
    this.accompany = accompany;
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.beginTime);
    dest.writeString(this.endTime);
    dest.writeString(this.passenger);
    dest.writeString(this.passengerTel);
    dest.writeString(this.beginAddr);
    dest.writeString(this.endAddr);
    dest.writeString(this.orderNo);
    dest.writeString(this.orgName);
    dest.writeString(this.deptName);
    dest.writeString(this.createTime);
    dest.writeString(this.remark);
    dest.writeString(this.beginGps);
    dest.writeString(this.endGps);
    dest.writeString(this.tripType);
    dest.writeString(this.orderReason);
    dest.writeString(this.useName);
    dest.writeString(this.accompany);
    dest.writeValue(this.state);
    dest.writeValue(this.distanceType);
    dest.writeValue(this.passengerNum);
    dest.writeValue(this.carUse);
    dest.writeInt(this.map.size());
    for (Entry<String, Integer> entry : this.map.entrySet()) {
      dest.writeString(entry.getKey());
      dest.writeValue(entry.getValue());
    }
    dest.writeTypedList(this.attachList);
  }

  protected Order(Parcel in) {
    this.beginTime = in.readString();
    this.endTime = in.readString();
    this.passenger = in.readString();
    this.passengerTel = in.readString();
    this.beginAddr = in.readString();
    this.endAddr = in.readString();
    this.orderNo = in.readString();
    this.orgName = in.readString();
    this.deptName = in.readString();
    this.createTime = in.readString();
    this.remark = in.readString();
    this.beginGps = in.readString();
    this.endGps = in.readString();
    this.tripType = in.readString();
    this.orderReason = in.readString();
    this.useName = in.readString();
    this.accompany = in.readString();
    this.state = (Integer) in.readValue(Integer.class.getClassLoader());
    this.distanceType = (Integer) in.readValue(Integer.class.getClassLoader());
    this.passengerNum = (Integer) in.readValue(Integer.class.getClassLoader());
    this.carUse = (Integer) in.readValue(Integer.class.getClassLoader());
    int mapSize = in.readInt();
    this.map = new HashMap<String, Integer>(mapSize);
    for (int i = 0; i < mapSize; i++) {
      String key = in.readString();
      Integer value = (Integer) in.readValue(Integer.class.getClassLoader());
      this.map.put(key, value);
    }
    this.attachList = in.createTypedArrayList(Attach.CREATOR);
  }

  public static final Creator<Order> CREATOR = new Creator<Order>() {
    @Override
    public Order createFromParcel(Parcel source) {
      return new Order(source);
    }

    @Override
    public Order[] newArray(int size) {
      return new Order[size];
    }
  };
}
