package com.irontower.gcbuser.mvp.model.entity;

/**
 * Created by jianghaifeng on 2017/12/17.
 */

public class Vehicle {

  private String modelName, useOrgName, cnName, carNo;
  private boolean select;
  private Double outputVolume;
  private Integer carId, carType, seatNum;

  public String getModelName() {
    return modelName;
  }

  public void setModelName(String modelName) {
    this.modelName = modelName;
  }

  public String getUseOrgName() {
    return useOrgName;
  }

  public void setUseOrgName(String useOrgName) {
    this.useOrgName = useOrgName;
  }

  public String getCnName() {
    return cnName;
  }

  public void setCnName(String cnName) {
    this.cnName = cnName;
  }

  public String getCarNo() {
    return carNo;
  }

  public void setCarNo(String carNo) {
    this.carNo = carNo;
  }

  public boolean isSelect() {
    return select;
  }

  public void setSelect(boolean select) {
    this.select = select;
  }

  public Double getOutputVolume() {
    return outputVolume;
  }

  public void setOutputVolume(Double outputVolume) {
    this.outputVolume = outputVolume;
  }

  public Integer getCarId() {
    return carId;
  }

  public void setCarId(Integer carId) {
    this.carId = carId;
  }

  public Integer getCarType() {
    return carType;
  }

  public void setCarType(Integer carType) {
    this.carType = carType;
  }

  public Integer getSeatNum() {
    return seatNum;
  }

  public void setSeatNum(Integer seatNum) {
    this.seatNum = seatNum;
  }
}
