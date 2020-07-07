package com.irontower.gcbuser.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.model.entity.Message;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/18.
 */

public class MessageAdapter extends BaseQuickAdapter<Message, BaseViewHolder> {

  @Override
  protected void convert(BaseViewHolder helper, Message item) {
    helper.setText(R.id.tvTime, item.getPublishTime());
    helper.setText(R.id.tvContent, item.getTitle());


  }

  public MessageAdapter(int layoutResId,
      @Nullable List<Message> data) {
    super(layoutResId, data);
  }
}
