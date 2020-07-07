package com.irontower.gcbuser.mvp.model.entity;

import android.os.Parcel;

/**
 * Created by jianghaifeng on 2017/12/22.
 */

public class OrderTrack implements android.os.Parcelable {

  private String levelName, dealTime;

  public String getLevelName() {
    return levelName;
  }

  public void setLevelName(String levelName) {
    this.levelName = levelName;
  }

  public String getDealTime() {
    return dealTime;
  }

  public void setDealTime(String dealTime) {
    this.dealTime = dealTime;
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.levelName);
    dest.writeString(this.dealTime);
  }

  public OrderTrack() {
  }

  protected OrderTrack(Parcel in) {
    this.levelName = in.readString();
    this.dealTime = in.readString();
  }

  public static final Creator<OrderTrack> CREATOR = new Creator<OrderTrack>() {
    @Override
    public OrderTrack createFromParcel(Parcel source) {
      return new OrderTrack(source);
    }

    @Override
    public OrderTrack[] newArray(int size) {
      return new OrderTrack[size];
    }
  };
}
