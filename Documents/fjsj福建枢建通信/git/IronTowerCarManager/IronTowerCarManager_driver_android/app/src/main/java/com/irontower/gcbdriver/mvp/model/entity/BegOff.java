package com.irontower.gcbdriver.mvp.model.entity;

/**
 * Created by jianghaifeng on 2017/12/21.
 */

public class BegOff {

  private String leaveId, beginTime, endTime, reason, type, remark;
  private Integer state;
  private boolean submit;

  public String getLeaveId() {
    return leaveId;
  }

  public void setLeaveId(String leaveId) {
    this.leaveId = leaveId;
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

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public boolean isSubmit() {
    return submit;
  }

  public void setSubmit(boolean submit) {
    this.submit = submit;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }
}
