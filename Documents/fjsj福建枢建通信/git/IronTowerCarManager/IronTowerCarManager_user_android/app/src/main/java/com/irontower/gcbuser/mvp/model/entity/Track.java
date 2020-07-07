package com.irontower.gcbuser.mvp.model.entity;

/**
 * Created by jianghaifeng on 2017/12/25.
 */

public class Track {

  private String upTime, lat, lng, ht, dAttr, cMile, sAttr, aAttr, gMile;
  private double gSpeed;

  public String getUpTime() {
    return upTime;
  }

  public void setUpTime(String upTime) {
    this.upTime = upTime;
  }

  public String getLat() {
    return lat;
  }

  public void setLat(String lat) {
    this.lat = lat;
  }

  public String getLng() {
    return lng;
  }

  public void setLng(String lng) {
    this.lng = lng;
  }

  public String getHt() {
    return ht;
  }

  public void setHt(String ht) {
    this.ht = ht;
  }

  public String getdAttr() {
    return dAttr;
  }

  public void setdAttr(String dAttr) {
    this.dAttr = dAttr;
  }

  public double getgSpeed() {
    return gSpeed;
  }

  public void setgSpeed(double gSpeed) {
    this.gSpeed = gSpeed;
  }

  public String getcMile() {
    return cMile;
  }

  public void setcMile(String cMile) {
    this.cMile = cMile;
  }

  public String getsAttr() {
    return sAttr;
  }

  public void setsAttr(String sAttr) {
    this.sAttr = sAttr;
  }

  public String getaAttr() {
    return aAttr;
  }

  public void setaAttr(String aAttr) {
    this.aAttr = aAttr;
  }

  public String getgMile() {
    return gMile;
  }

  public void setgMile(String gMile) {
    this.gMile = gMile;
  }
}
