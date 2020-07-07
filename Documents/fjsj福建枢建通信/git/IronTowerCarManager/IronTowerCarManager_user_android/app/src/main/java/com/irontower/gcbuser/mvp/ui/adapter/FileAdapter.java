package com.irontower.gcbuser.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.utils.DateUtil;
import java.io.File;
import java.util.Date;
import java.util.List;

public class FileAdapter extends BaseQuickAdapter<File, BaseViewHolder> {

  @Override
  protected void convert(BaseViewHolder helper, File item) {
    helper.setText(R.id.tvFileName, item.getName());
    helper.setText(R.id.tvFileTime,
        DateUtil.format(DateUtil.DEFAULT_FMT, new Date(item.lastModified())));
    if (item.isDirectory()) {
      helper.setImageResource(R.id.ivFile, R.mipmap.folder);
    } else if (item.getName().endsWith(".docx") || item.getName().endsWith(".doc")) {
      helper.setImageResource(R.id.ivFile, R.mipmap.word);
    } else if (item.getName().endsWith(".xls") || item.getName().endsWith(".xlsx")) {
      helper.setImageResource(R.id.ivFile, R.mipmap.excel);
    } else if (item.getName().endsWith(".jpg") || item.getName().endsWith(".png") || item.getName()
        .endsWith(".jpeg")) {
      helper.setImageResource(R.id.ivFile, R.mipmap.picture);
    } else {
      helper.setImageResource(R.id.ivFile, R.mipmap.other_file);
    }
  }

  public FileAdapter(int layoutResId,
      @Nullable List<File> data) {
    super(layoutResId, data);
  }
}
