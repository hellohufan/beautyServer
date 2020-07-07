package com.irontower.gcbdriver.mvp.model.entity;

/**
 * Created by jianghaifeng on 2017/12/8.
 */

public class Version {

  private String code;
  private String forceCode;
  private String no;
  private String remark;
  private String url;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getNo() {
    return no;
  }

  public void setNo(String no) {
    this.no = no;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getForceCode() {
    return forceCode;
  }

  public void setForceCode(String forceCode) {
    this.forceCode = forceCode;
  }
}