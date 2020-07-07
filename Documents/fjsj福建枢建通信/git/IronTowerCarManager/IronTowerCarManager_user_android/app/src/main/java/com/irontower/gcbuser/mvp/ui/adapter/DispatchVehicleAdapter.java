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

public class DispatchVehicleAdapter extends BaseQuickAdapter<ApproveOrder, BaseViewHolder> {

  @Override
  protected void convert(BaseViewHolder helper, ApproveOrder item) {
    helper.setText(R.id.tvPassenger, item.getPassenger());
    helper.setText(R.id.tvBeginAddr, item.getBeginAddr());
    helper.setText(R.id.tvEndAddr, item.getEndAddr());
    helper.setText(R.id.tvCommitTime, item.getBeginTime() + " 使用");
    if (item.getDistanceType() == 2) {
      helper.setText(R.id.tvDistanceType, "短途");


    } else {
      helper.setText(R.id.tvDistanceType, "长途");
    }
    if (item.getFlowId() != null && "4".equals(item.getFlowId())) {
      helper.setText(R.id.tvOrgName, item.getDeptName() == null ? "" : item.getDeptName());
    } else {
      helper.setText(R.id.tvOrgName, item.getOrgName() == null ? "" : item.getOrgName());

    }
    helper.setBackgroundRes(R.id.tvDistanceType, R.mipmap.status_red);
  }

  public DispatchVehicleAdapter(int layoutResId,
      @Nullable List<ApproveOrder> data) {
    super(layoutResId, data);
  }
}
