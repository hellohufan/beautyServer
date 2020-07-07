package com.irontower.gcbuser.mvp.model.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.support.annotation.NonNull;

@Entity(tableName = "car_search", indices = {
    @Index(value = {"carId", "beginTime", "endTime", "userId"},
        unique = true)})
public class CarSearch implements android.os.Parcelable {

  @PrimaryKey(autoGenerate = true)
  @NonNull
  private int id;
  private String carId;
  private String carNo;
  private String beginTime;
  private String endTime;
  private String userId;


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

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.id);
    dest.writeString(this.carId);
    dest.writeString(this.carNo);
    dest.writeString(this.beginTime);
    dest.writeString(this.endTime);
    dest.writeString(this.userId);
  }

  public CarSearch() {
  }

  protected CarSearch(Parcel in) {
    this.id = in.readInt();
    this.carId = in.readString();
    this.carNo = in.readString();
    this.beginTime = in.readString();
    this.endTime = in.readString();
    this.userId = in.readString();
  }

  public static final Creator<CarSearch> CREATOR = new Creator<CarSearch>() {
    @Override
    public CarSearch createFromParcel(Parcel source) {
      return new CarSearch(source);
    }

    @Override
    public CarSearch[] newArray(int size) {
      return new CarSearch[size];
    }
  };
}
