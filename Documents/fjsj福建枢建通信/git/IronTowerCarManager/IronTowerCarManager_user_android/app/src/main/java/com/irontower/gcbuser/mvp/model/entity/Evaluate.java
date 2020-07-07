package com.irontower.gcbuser.mvp.model.entity;

import android.os.Parcel;

/**
 * Created by jianghaifeng on 2017/12/24.
 */

public class Evaluate implements android.os.Parcelable {

  private Integer grade1, grade2, grade3;
  private String idea, refVal;

  public Integer getGrade1() {
    return grade1;
  }

  public void setGrade1(Integer grade1) {
    this.grade1 = grade1;
  }

  public Integer getGrade2() {
    return grade2;
  }

  public void setGrade2(Integer grade2) {
    this.grade2 = grade2;
  }

  public String getIdea() {
    return idea;
  }

  public void setIdea(String idea) {
    this.idea = idea;
  }

  public String getRefVal() {
    return refVal;
  }

  public void setRefVal(String refVal) {
    this.refVal = refVal;
  }

  public Evaluate() {
  }

  public Integer getGrade3() {
    return grade3;
  }

  public void setGrade3(Integer grade3) {
    this.grade3 = grade3;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeValue(this.grade1);
    dest.writeValue(this.grade2);
    dest.writeValue(this.grade3);
    dest.writeString(this.idea);
    dest.writeString(this.refVal);
  }

  protected Evaluate(Parcel in) {
    this.grade1 = (Integer) in.readValue(Integer.class.getClassLoader());
    this.grade2 = (Integer) in.readValue(Integer.class.getClassLoader());
    this.grade3 = (Integer) in.readValue(Integer.class.getClassLoader());
    this.idea = in.readString();
    this.refVal = in.readString();
  }

  public static final Creator<Evaluate> CREATOR = new Creator<Evaluate>() {
    @Override
    public Evaluate createFromParcel(Parcel source) {
      return new Evaluate(source);
    }

    @Override
    public Evaluate[] newArray(int size) {
      return new Evaluate[size];
    }
  };
}
