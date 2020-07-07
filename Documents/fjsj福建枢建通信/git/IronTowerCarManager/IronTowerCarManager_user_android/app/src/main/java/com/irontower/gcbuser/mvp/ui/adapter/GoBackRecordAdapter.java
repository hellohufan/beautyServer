package com.irontower.gcbuser.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.mvp.model.entity.BegGoOut;
import com.irontower.gcbuser.mvp.model.entity.Dict;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/17.
 */

public class GoBackRecordAdapter extends BaseQuickAdapter<BegGoOut, BaseViewHolder> {

  private List<Dict> dicts = AppUtil.getCarOutType();

  @Override
  protected void convert(BaseViewHolder helper, BegGoOut item) {
    helper.setText(R.id.tvCarNo, item.getCarNo());
    helper.setText(R.id.tvType, AppUtil.getDictName(String.valueOf(item.getOutType()), dicts));
    helper.setText(R.id.tvApplyDesc, item.getApplyDesc());
    helper.setText(R.id.tvFleetName, item.getFleetName());
    helper.setText(R.id.tvRealOutTime, item.getRealOutTime() + " 回场");

    if (item.getOutType() == 1) {
      helper.setBackgroundRes(R.id.tvType, R.mipmap.status_blue);

    } else if (item.getOutType() == 2) {
      helper.setBackgroundRes(R.id.tvType, R.mipmap.status_green);

    } else {
      helper.setBackgroundRes(R.id.tvType, R.mipmap.status_red);

    }

  }

  public GoBackRecordAdapter(int layoutResId,
      @Nullable List<BegGoOut> data) {
    super(layoutResId, data);
  }
}
