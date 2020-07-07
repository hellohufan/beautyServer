package com.irontower.gcbuser.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.model.entity.CarSearch;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/14.
 */

public class CarSearchAdapter extends BaseQuickAdapter<CarSearch, BaseViewHolder> {

  @Override
  protected void convert(BaseViewHolder helper, CarSearch item) {
    helper.setText(R.id.tvCarNo, item.getCarNo());
    helper.setText(R.id.tvTime, item.getBeginTime() + " ~ " + item.getEndTime());

  }

  public CarSearchAdapter(int layoutResId,
      @Nullable List<CarSearch> data) {
    super(layoutResId, data);
  }
}
