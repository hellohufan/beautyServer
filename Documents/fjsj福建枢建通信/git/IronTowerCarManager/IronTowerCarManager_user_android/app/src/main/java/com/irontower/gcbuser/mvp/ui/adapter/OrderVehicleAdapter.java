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
 * Created by jianghaifeng on 2017/12/19.
 */

public class OrderVehicleAdapter extends BaseQuickAdapter<OrderCar, BaseViewHolder> {

  private List<Dict> carTypes = AppUtil.getCarTypes();
  private List<Dict> orderStates = AppUtil.getOrderStates();

  public OrderVehicleAdapter(int layoutResId,
      @Nullable List<OrderCar> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, OrderCar item) {

    helper.setText(R.id.tvDriverName, item.getDriverName());
    helper.setText(R.id.tvCarNo, item.getCarNo());
    if (item.getCarType() != null) {
      helper.setText(R.id.tvCarType, AppUtil.getDictName(item.getCarType().toString(), carTypes));
    } else {
      helper.setText(R.id.tvCarType, null);

    }
    helper.setText(R.id.tvStatus, AppUtil.getDictName(item.getState().toString(), orderStates));
    if (!StringUtils.isEmpty(item.getDriverName())) {

      helper.setGone(R.id.tvLine, true);
    } else {
      helper.setGone(R.id.tvLine, false);
    }
    if (item.getState() == 2) {
      helper.setText(R.id.tvUnit, "派车");
      helper.setGone(R.id.tvUnit, true);
      helper.setTextColor(R.id.tvStatus, Color.parseColor("#747474"));
      helper.setGone(R.id.tvAgainDispatchCar, false);

    } else if (item.getState() == 3 && item.getOrgId() != null) {

      helper.setGone(R.id.tvAgainDispatchCar, false);
      if (AppUtil.getUser().getOrgId().equals(item.getOrgId())) {
        helper.setGone(R.id.tvUnit, true);
        helper.setText(R.id.tvUnit, "派车");
      } else {
        helper.setGone(R.id.tvUnit, false);
      }
      helper.setTextColor(R.id.tvStatus, Color.parseColor("#747474"));
    } else if (item.getState() == 4) {
      if (item.getOrgId() == null) {
        helper.setGone(R.id.tvAgainDispatchCar, true);
        helper.setText(R.id.tvAgainDispatchCar, "重新派车");

      } else {
        if (AppUtil.getUser().getOrgId().equals(item.getOrgId())) {
          helper.setGone(R.id.tvAgainDispatchCar, true);
          helper.setText(R.id.tvAgainDispatchCar, "重新派车");
        } else {
          helper.setGone(R.id.tvAgainDispatchCar, false);

        }
      }

      helper.setTextColor(R.id.tvStatus, Color.parseColor("#ff6565"));
      helper.setText(R.id.tvUnit, "联系司机");
      helper.setGone(R.id.tvUnit, true);
    } else if (item.getState() == 5) {
      if (item.getOrgId() == null) {
        helper.setVisible(R.id.tvAgainDispatchCar, true);
        helper.setText(R.id.tvAgainDispatchCar, "重新派车");

      } else {
        if (AppUtil.getUser().getOrgId().equals(item.getOrgId())) {
          helper.setVisible(R.id.tvAgainDispatchCar, true);
          helper.setText(R.id.tvAgainDispatchCar, "重新派车");
        } else {
          helper.setGone(R.id.tvAgainDispatchCar, false);

        }
      }

      helper.setTextColor(R.id.tvStatus, Color.parseColor("#f6683c"));
      helper.setText(R.id.tvUnit, "联系司机");
      helper.setGone(R.id.tvUnit, true);
    } else {
      helper.setVisible(R.id.tvAgainDispatchCar, false);
      helper.setTextColor(R.id.tvStatus, Color.parseColor("#f6683c"));
      helper.setText(R.id.tvUnit, "联系司机");
      helper.setGone(R.id.tvUnit, true);

    }
    if (item.getOrgId() == null) {
      if (item.getState() >= 4 && item.getState() < 8) {
        helper.setGone(R.id.tvCancelUseCar, true);//可见
      } else {
        helper.setGone(R.id.tvCancelUseCar, false);//不可见
      }
    } else {
      if (AppUtil.getUser().getOrgId().equals(item.getOrgId()) && item.getState() >= 3
          && item.getState() < 8) {

        helper.setGone(R.id.tvCancelUseCar, true);//可见

      } else {
        helper.setGone(R.id.tvCancelUseCar, false);//不可见


      }

    }
    if (item.getState() == 3) {
      helper.setText(R.id.tvCancelUseCar, "驳回");
    } else {
      helper.setText(R.id.tvCancelUseCar, "协调取消");
    }
    helper.addOnClickListener(R.id.tvUnit).addOnClickListener(R.id.tvAgainDispatchCar)
        .addOnClickListener(R.id.tvCancelUseCar);


  }
}
