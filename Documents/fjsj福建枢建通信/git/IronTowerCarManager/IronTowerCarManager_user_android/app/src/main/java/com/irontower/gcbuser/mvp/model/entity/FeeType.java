package com.irontower.gcbuser.mvp.model.entity;

/**
 * Created by jianghaifeng on 2017/12/27.
 */

public class FeeType {

  private String feeName;
  private Integer feeId;


  public Integer getFeeId() {
    return feeId;
  }

  public void setFeeId(Integer feeId) {
    this.feeId = feeId;
  }

  public String getFeeName() {
    return feeName;
  }

  public void setFeeName(String feeName) {
    this.feeName = feeName;
  }

  public FeeType(String feeName, Integer feeId) {
    this.feeName = feeName;
    this.feeId = feeId;
  }
}
