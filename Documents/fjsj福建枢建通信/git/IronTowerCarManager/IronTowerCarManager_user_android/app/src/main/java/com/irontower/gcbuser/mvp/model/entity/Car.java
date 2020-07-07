package com.irontower.gcbuser.mvp.model.entity;

import android.os.Parcel;

/**
 * Created by jianghaifeng on 2017/12/15.
 */

public class Car implements android.os.Parcelable {

  private String name, carId, carNo, carColor, registerDate, carUseDetail, modelName, outputVolume, cnName, carType, orgName, seatNum;
  private String num;
  private Integer carUseState;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNum() {
    return num;
  }

  public void setNum(String num) {
    this.num = num;
  }

  public Car(String name, String num) {
    this.name = name;
    this.num = num;
  }

  public Car() {
  }

  public String getCarId() {
    return carId;
  }

  public void setCarId(String carId) {
    this.carId = carId;
  }

  public String getCarNo() {
    return carNo;
  }

  public void setCarNo(String carNo) {
    this.carNo = carNo;
  }

  public String getCarColor() {
    return carColor;
  }

  public void setCarColor(String carColor) {
    this.carColor = carColor;
  }

  public String getRegisterDate() {
    return registerDate;
  }

  public void setRegisterDate(String registerDate) {
    this.registerDate = registerDate;
  }


  public String getModelName() {
    return modelName;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  public String getOutputVolume() {
    return outputVolume;
  }

  public void setOutputVolume(String outputVolume) {
    this.outputVolume = outputVolume;
  }

  public String getCnName() {
    return cnName;
  }

  public void setCnName(String cnName) {
    this.cnName = cnName;
  }

  public String getCarType() {
    return carType;
  }

  public void setCarType(String carType) {
    this.carType = carType;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  public Integer getCarUseState() {
    return carUseState;
  }

  public void setCarUseState(Integer carUseState) {
    this.carUseState = carUseState;
  }

  public String getSeatNum() {
    return seatNum;
  }

  public void setSeatNum(String seatNum) {
    this.seatNum = seatNum;
  }


  public String getCarUseDetail() {
    return carUseDetail;
  }

  public void setCarUseDetail(String carUseDetail) {
    this.carUseDetail = carUseDetail;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.name);
    dest.writeString(this.carId);
    dest.writeString(this.carNo);
    dest.writeString(this.carColor);
    dest.writeString(this.registerDate);
    dest.writeString(this.carUseDetail);
    dest.writeString(this.modelName);
    dest.writeString(this.outputVolume);
    dest.writeString(this.cnName);
    dest.writeString(this.carType);
    dest.writeString(this.orgName);
    dest.writeString(this.seatNum);
    dest.writeString(this.num);
    dest.writeValue(this.carUseState);
  }

  protected Car(Parcel in) {
    this.name = in.readString();
    this.carId = in.readString();
    this.carNo = in.readString();
    this.carColor = in.readString();
    this.registerDate = in.readString();
    this.carUseDetail = in.readString();
    this.modelName = in.readString();
    this.outputVolume = in.readString();
    this.cnName = in.readString();
    this.carType = in.readString();
    this.orgName = in.readString();
    this.seatNum = in.readString();
    this.num = in.readString();
    this.carUseState = (Integer) in.readValue(Integer.class.getClassLoader());
  }

  public static final Creator<Car> CREATOR = new Creator<Car>() {
    @Override
    public Car createFromParcel(Parcel source) {
      return new Car(source);
    }

    @Override
    public Car[] newArray(int size) {
      return new Car[size];
    }
  };
}
