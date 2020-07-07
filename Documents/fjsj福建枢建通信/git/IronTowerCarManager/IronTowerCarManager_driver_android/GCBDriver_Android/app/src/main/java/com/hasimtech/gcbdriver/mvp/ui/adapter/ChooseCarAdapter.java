package com.irontower.gcbdriver.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.app.utils.AppUtil;
import com.irontower.gcbdriver.mvp.model.entity.Car;
import com.irontower.gcbdriver.mvp.model.entity.Dict;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/26.
 */

public class ChooseCarAdapter extends BaseQuickAdapter<Car, BaseViewHolder> {

  private List<Dict> carTypes = AppUtil.getCarTypes();

  @Override
  protected void convert(BaseViewHolder helper, Car item) {
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

//    helper.setText(R.id.tvSeatNum,item.getSeatNum());
    helper
        .setText(R.id.tvCarType, AppUtil.getDictName(String.valueOf(item.getCarType()), carTypes));
    if (item.isSelect()) {
      helper.setGone(R.id.ivSelect, true);
      helper.setBackgroundRes(R.id.layItem, R.mipmap.choice_car_bg_focus);
    } else {
      helper.setBackgroundRes(R.id.layItem, R.mipmap.choice_car_bg);
      helper.setGone(R.id.ivSelect, false);

    }


  }

  public ChooseCarAdapter(int layoutResId,
      @Nullable List<Car> data) {
    super(layoutResId, data);
  }
}
