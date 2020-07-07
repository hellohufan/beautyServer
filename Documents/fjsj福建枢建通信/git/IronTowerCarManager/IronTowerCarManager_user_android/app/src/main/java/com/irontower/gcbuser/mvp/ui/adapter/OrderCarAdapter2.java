package com.irontower.gcbuser.mvp.ui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.mvp.model.entity.Dict;
import com.irontower.gcbuser.mvp.model.entity.OrderCar;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/16.
 */

public class OrderCarAdapter2 extends BaseQuickAdapter<OrderCar, BaseViewHolder> {

  private List<Dict> carTypes = AppUtil.getCarTypes();
  private List<Dict> orderStates = AppUtil.getOrderStates();

  @Override
  protected void convert(BaseViewHolder helper, OrderCar item) {
    helper.setText(R.id.tvCarNo, item.getCarNo());
    helper.setText(R.id.tvDriverName, item.getDriverName());
    if (StringUtils.isEmpty(item.getDriverName())) {
      helper.setGone(R.id.tvDN, false);
    } else {
      helper.setGone(R.id.tvDN, true);
    }
    if (item.getSeatNum() == null) {
      helper.setGone(R.id.tvSeatNum, false);
    } else {
      helper.setGone(R.id.tvSeatNum, true);
      helper.setText(R.id.tvSeatNum, item.getSeatNum().toString());
    }
    if (item.getFeeType() == null) {
      helper.setText(R.id.tvFeeType, "单位派车");
    } else {
      helper.setText(R.id.tvFeeType, "平台租车");
    }
    if (item.getCarType() == null) {
      helper.setGone(R.id.tvCarType, false);
    } else {
      helper.setGone(R.id.tvCarType, true);
      helper.setText(R.id.tvCarType,
          AppUtil.getDictName(String.valueOf(item.getCarType()), carTypes));
    }
    helper.setText(R.id.tvState, AppUtil.getDictName(String.valueOf(item.getState()), orderStates));
    if (item.getState() < 4) {
      helper.setTextColor(R.id.tvState, Color.parseColor("#747474"));
    } else if (item.getState() == 4) {
      helper.setTextColor(R.id.tvState, Color.parseColor("#ff6565"));
    } else if (item.getState() == 5) {
      helper.setTextColor(R.id.tvState, Color.parseColor("#f6683c"));
    } else {
      helper.setTextColor(R.id.tvState, Color.parseColor("#747474"));
    }
    if (item.getOrgId() != null) {
      if (item.getOrgId().equals(AppUtil.getUser().getOrgId()) && item.getState() < 6) {
        helper.setVisible(R.id.tvUnit, true);
      } else {
        helper.setVisible(R.id.tvUnit, false);
      }
    } else {
      helper.setVisible(R.id.tvUnit, false);
    }
    helper.addOnClickListener(R.id.tvUnit);

  }

  public OrderCarAdapter2(int layoutResId, @Nullable List<OrderCar> data) {
    super(layoutResId, data);
  }
}
