package com.irontower.gcbuser.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.mvp.model.entity.Dict;
import com.irontower.gcbuser.mvp.model.entity.Driver;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/17.
 */

public class ManageDriverAdapter extends BaseQuickAdapter<Driver, BaseViewHolder> {

  private List<Dict> dicts = AppUtil.getDriverStates();

  @Override
  protected void convert(BaseViewHolder helper, Driver item) {
    helper.setText(R.id.tvUserName, item.getUserName());
    helper.setText(R.id.tvSex, item.getSex());
    helper.setText(R.id.tvLicenseType, item.getLicenseType());
    helper.setText(R.id.tvPhoneNo, item.getPhoneNo());
    if (item.getDriverState() == null) {
      helper.setText(R.id.tvState, null);
      helper.setGone(R.id.tvState, false);
    } else {
      helper
          .setText(R.id.tvState, AppUtil.getDictName(String.valueOf(item.getDriverState()), dicts));

      if (item.getDriverState() == 1) {
        helper.setBackgroundRes(R.id.tvState, R.mipmap.status_blue);
      } else {
        helper.setBackgroundRes(R.id.tvState, R.mipmap.status_red);
      }

    }
  }

  public ManageDriverAdapter(int layoutResId,
      @Nullable List<Driver> data) {
    super(layoutResId, data);
  }
}
