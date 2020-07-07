package com.irontower.gcbuser.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.model.entity.OrderTrack;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/8.
 */

public class TimeLineAdapter extends BaseQuickAdapter<OrderTrack, BaseViewHolder> {

  public TimeLineAdapter(int layoutResId,
      @Nullable List<OrderTrack> data) {
    super(layoutResId, data);
  }

  @Override
  protected void convert(BaseViewHolder helper, OrderTrack item) {
    helper.setText(R.id.tvTime, item.getDealTime());
    helper.setText(R.id.tvContent, item.getLevelName());

    if (getData().indexOf(item) == getData().size() - 1) {
      helper.setImageResource(R.id.ivSchedule, R.mipmap.schedule_icon1);
      helper.setGone(R.id.ivHorizontalLine, false);
      helper.setGone(R.id.ivVerticalLine, false);

    } else {
      helper.setImageResource(R.id.ivSchedule, R.mipmap.schedule_icon1);
      helper.setGone(R.id.ivHorizontalLine, true);
      helper.setGone(R.id.ivVerticalLine, true);

    }

  }
}
