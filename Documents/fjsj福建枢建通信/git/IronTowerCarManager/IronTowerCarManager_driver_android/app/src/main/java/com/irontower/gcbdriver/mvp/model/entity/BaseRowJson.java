package com.irontower.gcbdriver.mvp.model.entity;

import com.irontower.gcbdriver.mvp.model.api.Api;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/20.
 */

public class BaseRowJson<T> implements Serializable {

  private Integer total;
  private Integer code;
  private String message;
  private List<T> rows;

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public List<T> getRows() {
    return rows;
  }

  public void setRows(List<T> rows) {
    this.rows = rows;
  }

  public boolean isSuccess() {
    return code.equals(Api.REQUEST_SUCCESS);
  }

  @Override
  public String toString() {
    return "BaseRowJson{" +
        "total=" + total +
        ", code=" + code +
        ", message='" + message + '\'' +
        ", rows=" + rows +
        '}';
  }
}
