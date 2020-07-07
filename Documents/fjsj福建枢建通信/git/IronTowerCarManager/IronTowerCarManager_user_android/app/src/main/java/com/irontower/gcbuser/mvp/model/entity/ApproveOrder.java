package com.irontower.gcbuser.mvp.model.entity;

/**
 * Created by jianghaifeng on 2017/12/22.
 */

public class ApproveOrder {


  private String todoId, orderNo, beginTime, endTime, passenger, passengerTel, beginAddr, endAddr, dealTime, commitTime, deptName, orgName, flowId;
  private Integer state, dealState, distanceType;


  public String getTodoId() {
    return todoId;
  }

  public void setTodoId(String todoId) {
    this.todoId = todoId;
  }

  public String getOrderNo() {
    return orderNo;
  }

  public void setOrderNo(String orderNo) {
    this.orderNo = orderNo;
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

  public String getPassenger() {
    return passenger;
  }

  public void setPassenger(String passenger) {
    this.passenger = passenger;
  }

  public String getPassengerTel() {
    return passengerTel;
  }

  public void setPassengerTel(String passengerTel) {
    this.passengerTel = passengerTel;
  }

  public String getBeginAddr() {
    return beginAddr;
  }

  public void setBeginAddr(String beginAddr) {
    this.beginAddr = beginAddr;
  }

  public String getEndAddr() {
    return endAddr;
  }

  public void setEndAddr(String endAddr) {
    this.endAddr = endAddr;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getDealTime() {
    return dealTime;
  }

  public void setDealTime(String dealTime) {
    this.dealTime = dealTime;
  }

  public String getCommitTime() {
    return commitTime;
  }

  public void setCommitTime(String commitTime) {
    this.commitTime = commitTime;
  }

  public Integer getDealState() {
    return dealState;
  }

  public void setDealState(Integer dealState) {
    this.dealState = dealState;
  }

  public Integer getDistanceType() {
    return distanceType;
  }

  public void setDistanceType(Integer distanceType) {
    this.distanceType = distanceType;
  }

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  public String getOrgName() {
    return orgName;
  }

  public void setOrgName(String orgName) {
    this.orgName = orgName;
  }

  public String getFlowId() {
    return flowId;
  }

  public void setFlowId(String flowId) {
    this.flowId = flowId;
  }


}
