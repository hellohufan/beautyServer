package com.irontower.gcbuser.mvp.ui.adapter;

import android.support.annotation.Nullable;
import com.baidu.mapapi.search.core.PoiInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import java.util.List;

/**
 * Created by jianghaifeng on 2017/12/14.
 */

public class AddressAdapter extends BaseQuickAdapter<PoiInfo, BaseViewHolder> {

  @Override
  protected void convert(BaseViewHolder helper, PoiInfo item) {
    helper.setText(R.id.tvName, item.name);
    helper.setText(R.id.tvAddr, item.address);

  }

  public AddressAdapter(int layoutResId,
      @Nullable List<PoiInfo> data) {
    super(layoutResId, data);
  }
}
