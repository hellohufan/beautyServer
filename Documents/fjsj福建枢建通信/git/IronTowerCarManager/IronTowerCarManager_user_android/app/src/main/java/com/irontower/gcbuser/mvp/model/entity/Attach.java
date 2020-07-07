package com.irontower.gcbuser.mvp.model.entity;

import android.os.Parcel;

public class Attach implements android.os.Parcelable {

  private String attachId;
  private String attachName;
  private String rawName;
  private String path;
  private String url;
  private boolean added;

  public boolean isAdded() {
    return added;
  }

  public void setAdded(boolean added) {
    this.added = added;
  }

  public String getAttachId() {
    return attachId;
  }

  public void setAttachId(String attachId) {
    this.attachId = attachId;
  }

  public String getAttachName() {
    return attachName;
  }

  public void setAttachName(String attachName) {
    this.attachName = attachName;
  }

  public String getRawName() {
    return rawName;
  }

  public void setRawName(String rawName) {
    this.rawName = rawName;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public Attach(boolean added) {
    this.added = true;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.attachId);
    dest.writeString(this.attachName);
    dest.writeString(this.rawName);
    dest.writeString(this.path);
    dest.writeString(this.url);
    dest.writeByte(this.added ? (byte) 1 : (byte) 0);
  }

  protected Attach(Parcel in) {
    this.attachId = in.readString();
    this.attachName = in.readString();
    this.rawName = in.readString();
    this.path = in.readString();
    this.url = in.readString();
    this.added = in.readByte() != 0;
  }

  public static final Creator<Attach> CREATOR = new Creator<Attach>() {
    @Override
    public Attach createFromParcel(Parcel source) {
      return new Attach(source);
    }

    @Override
    public Attach[] newArray(int size) {
      return new Attach[size];
    }
  };
}
