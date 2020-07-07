package com.irontower.gcbuser.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.mvp.model.entity.Dict;
import com.irontower.gcbuser.mvp.model.entity.Vehicle;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/18.
 */

public class ChooseVehicleAdapter extends BaseQuickAdapter<Vehicle, BaseViewHolder> {

  private List<Dict> dicts = AppUtil.getCarTypes();

  @Override
  protected void convert(BaseViewHolder helper, Vehicle item) {
    helper.setText(R.id.tvCarNo, item.getCarNo());
    helper.setText(R.id.tvCnName, item.getCnName());
    if (item.getOutputVolume() == null) {
      helper.setText(R.id.tvGas, null);
    } else {
      helper.setText(R.id.tvGas, item.getOutputVolume() + "L");
    }

    if (item.getSeatNum() == null) {
      helper.setText(R.id.tvSeatNum, null);
    } else {
      helper.setText(R.id.tvSeatNum, item.getSeatNum() + "座");
    }
    helper.setText(R.id.tvCarType, AppUtil.getDictName(String.valueOf(item.getCarType()), dicts));
    if (item.isSelect()) {
      helper.setGone(R.id.ivSelect, true);
      helper.setBackgroundRes(R.id.layItem, R.mipmap.choice_car_bg_focus);
    } else {
      helper.setBackgroundRes(R.id.layItem, R.mipmap.manage_car_listbg);
      helper.setGone(R.id.ivSelect, false);

    }


  }

  public ChooseVehicleAdapter(int layoutResId,
      @Nullable List<Vehicle> data) {
    super(layoutResId, data);
  }
}
