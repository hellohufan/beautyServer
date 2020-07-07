package com.irontower.gcbuser.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.model.entity.BegGoOut;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/17.
 */

public class ApproveGoOutHistoryAdapter extends BaseQuickAdapter<BegGoOut, BaseViewHolder> {

  @Override
  protected void convert(BaseViewHolder helper, BegGoOut item) {
    helper.setText(R.id.tvApplyName, item.getApplyName());
    helper.setText(R.id.tvApplyDesc, item.getApplyDesc());
    helper.setText(R.id.tvInTime, item.getInTime());
    helper.setText(R.id.tvOutTime, item.getOutTime());
    helper.setText(R.id.tvCarNo, item.getCarNo());
    if (StringUtils.isEmpty(item.getPhoneNo())) {
      helper.setText(R.id.tvPhoneNo, null);
    } else {
      helper.setText(R.id.tvPhoneNo, "(" + item.getPhoneNo() + ")");
    }

    if (item.getState() == 1) {
      helper.setImageResource(R.id.ivState, R.mipmap.agree);
    } else if (item.getState() == 2) {
      helper.setImageResource(R.id.ivState, R.mipmap.reject);

    } else if (item.getState() == 3) {
      helper.setImageResource(R.id.ivState, R.mipmap.out);

    } else {
      helper.setImageResource(R.id.ivState, R.mipmap.in);

    }
  }

  public ApproveGoOutHistoryAdapter(int layoutResId,
      @Nullable List<BegGoOut> data) {
    super(layoutResId, data);
  }
}
