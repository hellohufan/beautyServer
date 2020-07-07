package com.irontower.gcbuser.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.mvp.model.entity.Dict;
import com.irontower.gcbuser.mvp.model.entity.Order;
import java.util.List;

/**
 * Created by jianghaifeng on 2018/1/13.
 */

public class GoingOrderAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {

  private List<Dict> dicts = AppUtil.getOrderStates();

  @Override
  protected void convert(BaseViewHolder helper, Order item) {
    helper.setText(R.id.tvOrderNo, item.getOrderNo());
    if (!StringUtils.isEmpty(item.getPassenger())) {
      helper.setText(R.id.tvPassenger, item.getPassenger());
    } else {
      helper.setText(R.id.tvPassenger, null);

    }

    helper.setText(R.id.tvBeginAddr, item.getBeginAddr());
    helper.setText(R.id.tvEndAddr, item.getEndAddr());
    helper.setText(R.id.tvBeginTime, item.getBeginTime());
    helper.setText(R.id.tvState, AppUtil.getDictName(item.getState().toString(), dicts));

  }

  public GoingOrderAdapter(int layoutResId,
      @Nullable List<Order> data) {
    super(layoutResId, data);
  }
}
