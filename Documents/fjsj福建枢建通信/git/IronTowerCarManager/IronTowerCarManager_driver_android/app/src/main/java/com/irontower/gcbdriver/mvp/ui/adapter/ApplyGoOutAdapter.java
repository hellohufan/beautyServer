package com.irontower.gcbdriver.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.app.utils.AppUtil;
import com.irontower.gcbdriver.mvp.model.entity.BegGoOut;
import com.irontower.gcbdriver.mvp.model.entity.Dict;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/26.
 */

public class ApplyGoOutAdapter extends BaseQuickAdapter<BegGoOut, BaseViewHolder> {

  private List<Dict> carOutTypes = AppUtil.getCarOutType();
  private List<Dict> carOutStates = AppUtil.getCarOutState();

  @Override
  protected void convert(BaseViewHolder helper, BegGoOut item) {
    helper.setText(R.id.tvCarNo, item.getCarNo());
    if (StringUtils.isEmpty(item.getApplyDesc())) {
      helper.setText(R.id.tvReason, "暂无");
    } else {
      helper.setText(R.id.tvReason, item.getApplyDesc());
    }
    helper.setText(R.id.tvLastTime, item.getLastTime());
    helper
        .setText(R.id.tvType, AppUtil.getDictName(String.valueOf(item.getOutType()), carOutTypes));
    helper
        .setText(R.id.tvState, AppUtil.getDictName(String.valueOf(item.getState()), carOutStates));

    if (item.getState() == 2 || item.getState() == 5) {
      helper.setBackgroundRes(R.id.tvState, R.mipmap.status_red);
    } else if (item.getState() == 0 || item.getState() == 3 || item.getState() == 4) {
      helper.setBackgroundRes(R.id.tvState, R.mipmap.status_blue);

    } else {
      helper.setBackgroundRes(R.id.tvState, R.mipmap.status_green);

    }
  }

  public ApplyGoOutAdapter(int layoutResId,
      @Nullable List<BegGoOut> data) {
    super(layoutResId, data);
  }
}
