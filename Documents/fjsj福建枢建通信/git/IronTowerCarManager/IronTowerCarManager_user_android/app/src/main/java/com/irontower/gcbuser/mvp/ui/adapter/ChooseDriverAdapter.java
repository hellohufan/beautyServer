package com.irontower.gcbuser.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.model.entity.Driver;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/18.
 */

public class ChooseDriverAdapter extends BaseQuickAdapter<Driver, BaseViewHolder> {

  @Override
  protected void convert(BaseViewHolder helper, Driver item) {
    helper.setText(R.id.tvUserName, item.getUserName());
    helper.setText(R.id.tvSex, item.getSex());
    helper.setText(R.id.tvPhoneNo, item.getPhoneNo());
    helper.setText(R.id.tvLicense, item.getLicenseType());
    if (item.isSelect()) {
      helper.setGone(R.id.ivSelect, true);
      helper.setBackgroundRes(R.id.layItem, R.mipmap.driver_bg_focus);
    } else {
      helper.setBackgroundRes(R.id.layItem, R.mipmap.driver_bg1);
      helper.setGone(R.id.ivSelect, false);

    }

  }

  public ChooseDriverAdapter(int layoutResId,
      @Nullable List<Driver> data) {
    super(layoutResId, data);
  }

}
