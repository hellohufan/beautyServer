package com.irontower.gcbdriver.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.model.entity.Order;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/6.
 */

public class MyTaskAdapter extends BaseQuickAdapter<Order, BaseViewHolder> {

  public MyTaskAdapter(int layoutResId,
      @Nullable List<Order> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, Order item) {
    helper.setText(R.id.tvCarNo, item.getCarNo());
    helper.setText(R.id.tvDistanceType, item.getDistanceType() == 2 ? "短途" : "长途");
    helper.setText(R.id.tvPassenger, item.getPassenger());

    helper.setText(R.id.tvBeginAddr, item.getBeginAddr());
    helper.setText(R.id.tvEndAddr, item.getEndAddr());
    helper.setText(R.id.tvBeginTime, item.getBeginTime());

    helper.addOnClickListener(R.id.layNavigation)
        .addOnClickListener(R.id.layCall);

  }


}
