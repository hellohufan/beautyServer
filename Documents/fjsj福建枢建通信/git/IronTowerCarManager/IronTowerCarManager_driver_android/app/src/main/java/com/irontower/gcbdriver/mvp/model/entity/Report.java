package com.irontower.gcbdriver.mvp.model.entity;

import android.os.Parcel;

public class Report implements android.os.Parcelable {

  private String mile, minutes, toll, park, rest, gasCost, otherCost, orderCarNo;

  public String getMile() {
    return mile;
  }

  public void setMile(String mile) {
    this.mile = mile;
  }

  public String getMinutes() {
    return minutes;
  }

  public void setMinutes(String minutes) {
    this.minutes = minutes;
  }

  public String getToll() {
    return toll;
  }

  public void setToll(String toll) {
    this.toll = toll;
  }

  public String getPark() {
    return park;
  }

  public void setPark(String park) {
    this.park = park;
  }

  public String getRest() {
    return rest;
  }

  public void setRest(String rest) {
    this.rest = rest;
  }

  public String getGasCost() {
    return gasCost;
  }

  public void setGasCost(String gasCost) {
    this.gasCost = gasCost;
  }

  public String getOtherCost() {
    return otherCost;
  }

  public void setOtherCost(String otherCost) {
    this.otherCost = otherCost;
  }

  public String getOrderCarNo() {
    return orderCarNo;
  }

  public void setOrderCarNo(String orderCarNo) {
    this.orderCarNo = orderCarNo;
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.mile);
    dest.writeString(this.minutes);
    dest.writeString(this.toll);
    dest.writeString(this.park);
    dest.writeString(this.rest);
    dest.writeString(this.gasCost);
    dest.writeString(this.otherCost);
    dest.writeString(this.orderCarNo);
  }

  public Report() {
  }

  protected Report(Parcel in) {
    this.mile = in.readString();
    this.minutes = in.readString();
    this.toll = in.readString();
    this.park = in.readString();
    this.rest = in.readString();
    this.gasCost = in.readString();
    this.otherCost = in.readString();
    this.orderCarNo = in.readString();
  }

  public static final Creator<Report> CREATOR = new Creator<Report>() {
    @Override
    public Report createFromParcel(Parcel source) {
      return new Report(source);
    }

    @Override
    public Report[] newArray(int size) {
      return new Report[size];
    }
  };
}
