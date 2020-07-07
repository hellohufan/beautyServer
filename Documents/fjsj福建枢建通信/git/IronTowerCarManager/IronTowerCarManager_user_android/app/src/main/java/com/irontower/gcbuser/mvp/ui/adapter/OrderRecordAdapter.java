package com.irontower.gcbuser.mvp.ui.adapter;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.mvp.model.entity.Dict;
import com.irontower.gcbuser.mvp.model.entity.MultipleOrder;
import java.util.List;


/**
 * Created by jianghaifeng on 2017/12/7.
 */

public class OrderRecordAdapter extends BaseMultiItemQuickAdapter<MultipleOrder, BaseViewHolder> {

  private List<Dict> dicts = AppUtil.getOrderStates();


  public OrderRecordAdapter(
      List<MultipleOrder> data) {
    super(data);
    addItemType(MultipleOrder.FINISH, R.layout.item_order_record_finish);
    addItemType(MultipleOrder.CANCEL, R.layout.item_order_record_waiting);
  }


  @Override
  protected void convert(BaseViewHolder helper, MultipleOrder item) {
    switch (helper.getItemViewType()) {
      case MultipleOrder.FINISH:
        helper.setText(R.id.tvOrderNo, item.getOrderNo());

        helper.setText(R.id.tvBeginAddr, item.getBeginAddr());
        helper.setText(R.id.tvEndAddr, item.getEndAddr());
        helper.setText(R.id.tvBeginTime, item.getBeginTime());
        if (!StringUtils.isEmpty(item.getPassenger())) {
          helper.setText(R.id.tvPassenger, item.getPassenger());
        } else {
          helper.setText(R.id.tvPassenger, null);

        }

        if (item.getState() > 8 && item.getState() < 13) {
          helper.setGone(R.id.ivOver, true);
        } else {
          helper.setGone(R.id.ivOver, false);
        }
        break;
      case MultipleOrder.CANCEL:
        helper.setText(R.id.tvOrderNo, item.getOrderNo());
        if (!StringUtils.isEmpty(item.getPassenger())) {
          helper.setText(R.id.tvPassenger, item.getPassenger());
        } else {
          helper.setText(R.id.tvPassenger, null);

        }

        helper.setText(R.id.tvBeginAddr, item.getBeginAddr());
        helper.setText(R.id.tvEndAddr, item.getEndAddr());
        helper.setText(R.id.tvBeginTime, item.getBeginTime());
        helper.setText(R.id.tvState, AppUtil.getDictName(item.getState().toString(), dicts));

        break;
      default:
    }


  }
}
