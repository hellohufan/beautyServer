package com.irontower.gcbuser.mvp.model.entity;

import android.os.Parcel;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/22.
 */

public class OrderCar implements android.os.Parcelable {

  private String driverName, orderCarNo, driverPhone, carNo;
  private Integer carType, state, carNum, driverId, feeType, seatNum, orgId, carId;
  private Evaluate evaluate;
  private List<OrderTrack> carTracks;

  public Integer getCarId() {
    return carId;
  }

  public void setCarId(Integer carId) {
    this.carId = carId;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getDriverName() {
    return driverName;
  }

  public void setDriverName(String driverName) {
    this.driverName = driverName;
  }


  public String getOrderCarNo() {
    return orderCarNo;
  }

  public void setOrderCarNo(String orderCarNo) {
    this.orderCarNo = orderCarNo;
  }

  public Integer getCarType() {
    return carType;
  }

  public void setCarType(Integer carType) {
    this.carType = carType;
  }

  public Integer getCarNum() {
    return carNum;
  }

  public void setCarNum(Integer carNum) {
    this.carNum = carNum;
  }

  public Integer getDriverId() {
    return driverId;
  }

  public void setDriverId(Integer driverId) {
    this.driverId = driverId;
  }

  public Integer getFeeType() {
    return feeType;
  }

  public void setFeeType(Integer feeType) {
    this.feeType = feeType;
  }

  public Integer getSeatNum() {
    return seatNum;
  }

  public void setSeatNum(Integer seatNum) {
    this.seatNum = seatNum;
  }


  public OrderCar() {
  }


  public Evaluate getEvaluate() {
    return evaluate;
  }

  public void setEvaluate(Evaluate evaluate) {
    this.evaluate = evaluate;
  }

  public List<OrderTrack> getCarTracks() {
    return carTracks;
  }

  public void setCarTracks(
      List<OrderTrack> carTracks) {
    this.carTracks = carTracks;
  }

  public String getDriverPhone() {
    return driverPhone;
  }

  public void setDriverPhone(String driverPhone) {
    this.driverPhone = driverPhone;
  }

  public String getCarNo() {
    return carNo;
  }

  public void setCarNo(String carNo) {
    this.carNo = carNo;
  }

  public Integer getOrgId() {
    return orgId;
  }

  public void setOrgId(Integer orgId) {
    this.orgId = orgId;
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.driverName);
    dest.writeString(this.orderCarNo);
    dest.writeString(this.driverPhone);
    dest.writeString(this.carNo);
    dest.writeValue(this.carType);
    dest.writeValue(this.state);
    dest.writeValue(this.carNum);
    dest.writeValue(this.driverId);
    dest.writeValue(this.feeType);
    dest.writeValue(this.seatNum);
    dest.writeValue(this.orgId);
    dest.writeValue(this.carId);
    dest.writeParcelable(this.evaluate, flags);
    dest.writeTypedList(this.carTracks);
  }

  protected OrderCar(Parcel in) {
    this.driverName = in.readString();
    this.orderCarNo = in.readString();
    this.driverPhone = in.readString();
    this.carNo = in.readString();
    this.carType = (Integer) in.readValue(Integer.class.getClassLoader());
    this.state = (Integer) in.readValue(Integer.class.getClassLoader());
    this.carNum = (Integer) in.readValue(Integer.class.getClassLoader());
    this.driverId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.feeType = (Integer) in.readValue(Integer.class.getClassLoader());
    this.seatNum = (Integer) in.readValue(Integer.class.getClassLoader());
    this.orgId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.carId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.evaluate = in.readParcelable(Evaluate.class.getClassLoader());
    this.carTracks = in.createTypedArrayList(OrderTrack.CREATOR);
  }

  public static final Creator<OrderCar> CREATOR = new Creator<OrderCar>() {
    @Override
    public OrderCar createFromParcel(Parcel source) {
      return new OrderCar(source);
    }

    @Override
    public OrderCar[] newArray(int size) {
      return new OrderCar[size];
    }
  };
}
