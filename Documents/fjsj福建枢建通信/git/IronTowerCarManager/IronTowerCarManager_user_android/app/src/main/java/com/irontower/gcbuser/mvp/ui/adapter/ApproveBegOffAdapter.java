package com.irontower.gcbuser.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.model.entity.BegOff;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/17.
 */

public class ApproveBegOffAdapter extends BaseQuickAdapter<BegOff, BaseViewHolder> {

  @Override
  protected void convert(BaseViewHolder helper, BegOff item) {
    helper.setText(R.id.tvDriverName, item.getDriverName());
    if (StringUtils.isEmpty(item.getPhoneNo())) {
      helper.setText(R.id.tvPhoneNo, null);
    } else {
      helper.setText(R.id.tvPhoneNo, "(" + item.getPhoneNo() + ")");
    }

    helper.setText(R.id.tvBeginTime, item.getBeginTime());
    helper.setText(R.id.tvEndTime, item.getEndTime());
    helper.setText(R.id.tvReason, item.getReason());
    if (StringUtils.isEmpty(item.getType())) {
      helper.setGone(R.id.tvType, false);
    } else {
      helper.setGone(R.id.tvType, true);

      helper.setText(R.id.tvType, item.getType());
      helper.setBackgroundRes(R.id.tvType, R.mipmap.status_red);
    }

    helper.addOnClickListener(R.id.tvReject).addOnClickListener(R.id.tvAgree);
  }

  public ApproveBegOffAdapter(int layoutResId,
      @Nullable List<BegOff> data) {
    super(layoutResId, data);
  }
}
