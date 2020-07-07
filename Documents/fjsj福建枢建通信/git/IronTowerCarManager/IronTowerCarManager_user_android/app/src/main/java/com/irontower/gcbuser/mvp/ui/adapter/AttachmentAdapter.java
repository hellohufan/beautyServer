package com.irontower.gcbuser.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import java.util.List;

public class AttachmentAdapter extends BaseQuickAdapter<Attach, BaseViewHolder> {

  @Override
  protected void convert(BaseViewHolder helper, Attach item) {
    helper.setText(R.id.tvAttachment, item.getAttachName());
    if (item.isAdded()) {
      helper.setImageResource(R.id.ivReduceAttachment, R.mipmap.add);
    } else {
      helper.setImageResource(R.id.ivReduceAttachment, R.mipmap.reduce);
    }
    helper.addOnClickListener(R.id.ivReduceAttachment).addOnClickListener(R.id.layChooseAttachment);
    if (mData.indexOf(item) == 0) {
      helper.setGone(R.id.viewLine, false);
    } else {
      helper.setGone(R.id.viewLine, true);

    }
  }

  public AttachmentAdapter(int layoutResId,
      @Nullable List<Attach> data) {
    super(layoutResId, data);
  }
}
