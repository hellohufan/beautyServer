package com.irontower.gcbuser.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.model.entity.Order;
import java.util.List;

/**
 * Created by jianghaifeng on 2018/1/13.
 */

public class FinishOrderAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {

  @Override
  protected void convert(BaseViewHolder helper, Order item) {
    helper.setText(R.id.tvOrderNo, item.getOrderNo());

    helper.setText(R.id.tvBeginAddr, item.getBeginAddr());
    helper.setText(R.id.tvEndAddr, item.getEndAddr());
    helper.setText(R.id.tvBeginTime, item.getBeginTime());
    if (!StringUtils.isEmpty(item.getPassenger())) {
      helper.setText(R.id.tvPassenger, item.getPassenger());
    } else {
      helper.setText(R.id.tvPassenger, null);

    }

    if (item.getState() > 8 && item.getState() < 13) {
      helper.setGone(R.id.ivOver, true);
    } else {
      helper.setGone(R.id.ivOver, false);
    }
  }

  public FinishOrderAdapter(int layoutResId,
      @Nullable List<Order> data) {
    super(layoutResId, data);
  }
}
