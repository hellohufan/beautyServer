package com.irontower.gcbuser.mvp.model.entity;

import android.os.Parcel;

/**
 * Created by jianghaifeng on 2017/12/17.
 */

public class Driver implements android.os.Parcelable {

  private String userName, status, sex, phoneNo, licenseType, deptName, nativePlace, laborRelation, remark;
  private Integer userId, driverState;
  private boolean select;


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }


  public boolean isSelect() {
    return select;
  }

  public void setSelect(boolean select) {
    this.select = select;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPhoneNo() {
    return phoneNo;
  }

  public void setPhoneNo(String phoneNo) {
    this.phoneNo = phoneNo;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getLicenseType() {
    return licenseType;
  }

  public void setLicenseType(String licenseType) {
    this.licenseType = licenseType;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public String getNativePlace() {
    return nativePlace;
  }

  public void setNativePlace(String nativePlace) {
    this.nativePlace = nativePlace;
  }


  public Driver() {
  }

  public String getLaborRelation() {
    return laborRelation;
  }

  public void setLaborRelation(String laborRelation) {
    this.laborRelation = laborRelation;
  }


  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Integer getDriverState() {
    return driverState;
  }

  public void setDriverState(Integer driverState) {
    this.driverState = driverState;
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.userName);
    dest.writeString(this.status);
    dest.writeString(this.sex);
    dest.writeString(this.phoneNo);
    dest.writeString(this.licenseType);
    dest.writeString(this.deptName);
    dest.writeString(this.nativePlace);
    dest.writeString(this.laborRelation);
    dest.writeString(this.remark);
    dest.writeValue(this.userId);
    dest.writeValue(this.driverState);
    dest.writeByte(this.select ? (byte) 1 : (byte) 0);
  }

  protected Driver(Parcel in) {
    this.userName = in.readString();
    this.status = in.readString();
    this.sex = in.readString();
    this.phoneNo = in.readString();
    this.licenseType = in.readString();
    this.deptName = in.readString();
    this.nativePlace = in.readString();
    this.laborRelation = in.readString();
    this.remark = in.readString();
    this.userId = (Integer) in.readValue(Integer.class.getClassLoader());
    this.driverState = (Integer) in.readValue(Integer.class.getClassLoader());
    this.select = in.readByte() != 0;
  }

  public static final Creator<Driver> CREATOR = new Creator<Driver>() {
    @Override
    public Driver createFromParcel(Parcel source) {
      return new Driver(source);
    }

    @Override
    public Driver[] newArray(int size) {
      return new Driver[size];
    }
  };
}
