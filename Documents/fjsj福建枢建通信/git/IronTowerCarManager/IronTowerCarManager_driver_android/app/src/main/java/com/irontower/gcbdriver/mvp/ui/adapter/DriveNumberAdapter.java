package com.irontower.gcbdriver.mvp.ui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.model.entity.ChartData;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/9.
 */

public class DriveNumberAdapter extends BaseQuickAdapter<ChartData, BaseViewHolder> {

  @Override
  protected void convert(BaseViewHolder helper, ChartData item) {
    if (getData().indexOf(item) % 2 == 0) {
      helper.setBackgroundColor(R.id.layItem, Color.parseColor("#f7f7f7"));
    } else {
      helper.setBackgroundColor(R.id.layItem, Color.parseColor("#ffffff"));
    }
    helper.setText(R.id.tvShort, String.valueOf(item.getNum().intValue()));
    helper.setText(R.id.tvLong, String.valueOf(item.getNum1().intValue()));

    helper.setText(R.id.tvMonth, item.getMonth());

  }

  public DriveNumberAdapter(int layoutResId,
      @Nullable List<ChartData> data) {
    super(layoutResId, data);
  }
}
