package com.irontower.gcbuser.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.model.entity.ApproveOrder;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/16.
 */

public class ApproveHistoryAdapter extends BaseQuickAdapter<ApproveOrder, BaseViewHolder> {

  @Override
  protected void convert(BaseViewHolder helper, ApproveOrder item) {
    helper.setText(R.id.tvOrderNo, item.getOrderNo());

    helper.setText(R.id.tvBeginAddr, item.getBeginAddr());
    helper.setText(R.id.tvEndAddr, item.getEndAddr());
    helper.setText(R.id.tvPassenger, item.getPassenger());
    helper.setText(R.id.tvDealTime, item.getDealTime());
    if (item.getDealState() != null && item.getDealState() == 1) {
      helper.setText(R.id.tvDealState, "已同意");
      helper.setBackgroundRes(R.id.tvDealState, R.mipmap.status_blue);


    } else {
      helper.setText(R.id.tvDealState, "已驳回");
      helper.setBackgroundRes(R.id.tvDealState, R.mipmap.status_red);


    }


  }

  public ApproveHistoryAdapter(int layoutResId,
      @Nullable List<ApproveOrder> data) {
    super(layoutResId, data);
  }
}
