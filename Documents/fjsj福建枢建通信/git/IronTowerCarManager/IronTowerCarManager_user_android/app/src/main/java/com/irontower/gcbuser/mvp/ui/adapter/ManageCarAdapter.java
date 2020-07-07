package com.irontower.gcbuser.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.mvp.model.entity.Car;
import com.irontower.gcbuser.mvp.model.entity.Dict;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/17.
 */

public class ManageCarAdapter extends BaseQuickAdapter<Car, BaseViewHolder> {

  private List<Dict> carTypes = AppUtil.getCarTypes();
  private List<Dict> carUseStates = AppUtil.getCarUseStates();

  @Override
  protected void convert(BaseViewHolder helper, Car item) {
    helper.setText(R.id.tvCarNo, item.getCarNo());
    if (!StringUtils.isEmpty(item.getSeatNum())) {
      helper.setText(R.id.tvSeatNum, item.getSeatNum() + "座");
    } else {
      helper.setText(R.id.tvSeatNum, null);
    }
    if (item.getCarUseState() == null) {
      helper.setText(R.id.tvState, null);
      helper.setGone(R.id.tvState, false);
    } else {
      helper.setGone(R.id.tvState, true);
      helper.setText(R.id.tvState,
          AppUtil.getDictName(String.valueOf(item.getCarUseState()), carUseStates));
      if (item.getCarUseState() == 1) {
        helper.setBackgroundRes(R.id.tvState, R.mipmap.status_blue);
      } else {
        helper.setBackgroundRes(R.id.tvState, R.mipmap.status_red);
      }

    }

    if (!StringUtils.isEmpty(item.getOutputVolume())) {
      helper.setText(R.id.tvOutputVolume, item.getOutputVolume() + "L");
    } else {
      helper.setText(R.id.tvOutputVolume, null);

    }
    if (StringUtils.isEmpty(item.getCarType())) {
      helper.setText(R.id.tvCarType, null);
    } else {
      helper.setText(R.id.tvCarType, AppUtil.getDictName(item.getCarType(), carTypes));
    }
    helper.setText(R.id.tvName, item.getCnName() + item.getModelName());
  }

  public ManageCarAdapter(int layoutResId,
      @Nullable List<Car> data) {
    super(layoutResId, data);
  }
}
