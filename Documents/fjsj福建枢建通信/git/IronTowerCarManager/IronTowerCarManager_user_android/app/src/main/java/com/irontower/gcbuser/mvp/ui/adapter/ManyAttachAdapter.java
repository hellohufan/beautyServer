package com.irontower.gcbuser.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import java.util.List;

public class ManyAttachAdapter extends BaseQuickAdapter<Attach, BaseViewHolder> {

  @Override
  protected void convert(BaseViewHolder helper, Attach item) {
    if (item.getAttachName().endsWith(".docx") || item.getAttachName().endsWith(".doc")) {
      helper.setImageResource(R.id.ivAttach, R.mipmap.word);
    } else if (item.getAttachName().endsWith(".xls") || item.getAttachName().endsWith(".xlsx")) {
      helper.setImageResource(R.id.ivAttach, R.mipmap.excel);
    } else if (item.getAttachName().endsWith(".jpg") || item.getAttachName().endsWith(".png")
        || item.getAttachName()
        .endsWith(".jpeg")) {
      helper.setImageResource(R.id.ivAttach, R.mipmap.picture);
    } else {
      helper.setImageResource(R.id.ivAttach, R.mipmap.other_file);
    }
    helper.setText(R.id.tvAttach, item.getAttachName());

  }

  public ManyAttachAdapter(int layoutResId,
      @Nullable List<Attach> data) {
    super(layoutResId, data);
  }
}
