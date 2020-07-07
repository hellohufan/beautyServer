package com.irontower.gcbuser.mvp.model.entity;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import java.util.Objects;

/**
 * Created by jianghaifeng on 2017/12/13.
 */

public class MultipleOrg extends AbstractExpandableItem<MultiItemEntity> implements
    MultiItemEntity {

  @Override
  public int getItemType() {
    return MultipleCar.ORG;
  }

  private String id;
  private boolean isParent;

  private int orgLevel;
  private String text;
  private boolean requesting = false;
  private int onlineCount;
  private int totalCount;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public boolean isParent() {
    return isParent;
  }

  public void setParent(boolean parent) {
    isParent = parent;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public int getLevel() {
    return 0;
  }

  public int getOrgLevel() {
    return orgLevel;
  }

  public void setOrgLevel(int orgLevel) {
    this.orgLevel = orgLevel;
  }

  public boolean isRequesting() {
    return requesting;
  }

  public void setRequesting(boolean requesting) {
    this.requesting = requesting;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MultipleOrg that = (MultipleOrg) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  public int getOnlineCount() {
    return onlineCount;
  }

  public void setOnlineCount(int onlineCount) {
    this.onlineCount = onlineCount;
  }

  public int getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(int totalCount) {
    this.totalCount = totalCount;
  }
}
