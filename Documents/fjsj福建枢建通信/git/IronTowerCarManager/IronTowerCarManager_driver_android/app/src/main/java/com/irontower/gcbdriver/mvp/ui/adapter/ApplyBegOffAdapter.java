package com.irontower.gcbdriver.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.model.entity.BegOff;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/21.
 */

public class ApplyBegOffAdapter extends BaseQuickAdapter<BegOff, BaseViewHolder> {

  @Override
  protected void convert(BaseViewHolder helper, BegOff item) {

    helper.setText(R.id.tvType, item.getType());
    helper.setText(R.id.tvBeginTime, item.getBeginTime() + " 开始");
    helper.setText(R.id.tvEndTime, item.getEndTime() + " 结束");
    if (StringUtils.isEmpty(item.getRemark())) {
      helper.setText(R.id.tvReason, "暂无");
    } else {
      helper.setText(R.id.tvReason, item.getRemark());
    }

    if (item.getState() == 0) {
      helper.setBackgroundRes(R.id.tvState, R.mipmap.status_blue);
      helper.setText(R.id.tvState, "待提交");
    } else if (item.getState() == 1) {
      helper.setBackgroundRes(R.id.tvState, R.mipmap.status_blue);
      helper.setText(R.id.tvState, "待审批");

    } else if (item.getState() == 2) {
      helper.setBackgroundRes(R.id.tvState, R.mipmap.status_green);
      helper.setText(R.id.tvState, "审核通过");

    } else if (item.getState() == 3) {
      helper.setBackgroundRes(R.id.tvState, R.mipmap.status_red);
      helper.setText(R.id.tvState, "驳回");

    }

  }

  public ApplyBegOffAdapter(int layoutResId,
      @Nullable List<BegOff> data) {
    super(layoutResId, data);
  }
}
