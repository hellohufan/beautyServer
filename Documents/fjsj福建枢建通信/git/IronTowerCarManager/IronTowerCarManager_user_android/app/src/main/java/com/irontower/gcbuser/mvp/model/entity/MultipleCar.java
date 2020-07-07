package com.irontower.gcbuser.mvp.model.entity;

import android.os.Parcel;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by jianghaifeng on 2017/12/13.
 */

public class MultipleCar implements MultiItemEntity, android.os.Parcelable {

  public static final int ORG = 1;
  public static final int CAR = 2;

  @Override
  public int getItemType() {
    return CAR;
  }

  private String id;
  private String carId;
  private String carNo;
  private String dataId;
  private String text;
  private int orgLevel;
  private int dAttr;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
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


  public String getDataId() {
    return dataId;
  }

  public void setDataId(String dataId) {
    this.dataId = dataId;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public int getOrgLevel() {
    return orgLevel;
  }

  public void setOrgLevel(int orgLevel) {
    this.orgLevel = orgLevel;
  }


  public MultipleCar() {
  }

  public int getdAttr() {
    return dAttr;
  }

  public void setdAttr(int dAttr) {
    this.dAttr = dAttr;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.carId);
    dest.writeString(this.carNo);
    dest.writeString(this.dataId);
    dest.writeString(this.text);
    dest.writeInt(this.orgLevel);
    dest.writeInt(this.dAttr);
  }

  protected MultipleCar(Parcel in) {
    this.id = in.readString();
    this.carId = in.readString();
    this.carNo = in.readString();
    this.dataId = in.readString();
    this.text = in.readString();
    this.orgLevel = in.readInt();
    this.dAttr = in.readInt();
  }

  public static final Creator<MultipleCar> CREATOR = new Creator<MultipleCar>() {
    @Override
    public MultipleCar createFromParcel(Parcel source) {
      return new MultipleCar(source);
    }

    @Override
    public MultipleCar[] newArray(int size) {
      return new MultipleCar[size];
    }
  };
}
