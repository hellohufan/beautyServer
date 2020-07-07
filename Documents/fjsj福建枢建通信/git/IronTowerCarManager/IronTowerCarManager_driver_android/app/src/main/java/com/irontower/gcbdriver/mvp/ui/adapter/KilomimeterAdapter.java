package com.irontower.gcbdriver.mvp.ui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.model.entity.ChartData;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/9.
 */

public class KilomimeterAdapter extends BaseQuickAdapter<ChartData, BaseViewHolder> {

  private DecimalFormat df = new DecimalFormat("0.00");

  @Override
  protected void convert(BaseViewHolder helper, ChartData item) {
    if (getData().indexOf(item) % 2 == 0) {
      helper.setBackgroundColor(R.id.layItem, Color.parseColor("#f7f7f7"));
    } else {
      helper.setBackgroundColor(R.id.layItem, Color.parseColor("#ffffff"));
    }
    helper.setText(R.id.tvShort,
        item.getNum().floatValue() > 0 ? df.format(item.getNum().doubleValue()) : "0");
    helper.setText(R.id.tvLong,
        item.getNum1().floatValue() > 0 ? df.format(item.getNum1().doubleValue()) : "0");

    helper.setText(R.id.tvMonth, item.getMonth());

  }

  public KilomimeterAdapter(int layoutResId,
      @Nullable List<ChartData> data) {
    super(layoutResId, data);
  }
}
