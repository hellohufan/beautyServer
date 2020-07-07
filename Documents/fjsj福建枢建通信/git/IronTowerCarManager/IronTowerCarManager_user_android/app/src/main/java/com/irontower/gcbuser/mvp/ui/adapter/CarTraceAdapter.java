package com.irontower.gcbuser.mvp.ui.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.model.entity.MultipleCar;
import com.irontower.gcbuser.mvp.model.entity.MultipleOrg;
import java.util.List;


/**
 * Created by jianghaifeng on 2017/12/7.
 */

public class CarTraceAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

  private static final int MARGIN = 15;

  public interface OnLoadListener {

    void onLoad(MultipleOrg multipleOrg, int pos);

    void jump(MultipleCar multipleCar);


  }

  private OnLoadListener onLoadListener;

  public CarTraceAdapter(
      List<MultiItemEntity> data, OnLoadListener onLoadListener) {
    super(data);
    this.onLoadListener = onLoadListener;
    addItemType(MultipleCar.ORG, R.layout.item_car_trace_org);
    addItemType(MultipleCar.CAR, R.layout.item_car_trace_car);

  }


  @Override
  protected void convert(BaseViewHolder helper, MultiItemEntity item) {
    switch (item.getItemType()) {
      case MultipleCar.CAR:
        MultipleCar multipleCar = (MultipleCar) item;
        helper.itemView.setPadding(multipleCar.getOrgLevel() * MARGIN, 0, 0, 0);
        if ((multipleCar.getdAttr() & 1) == 1) {
          helper.setText(R.id.tvStatus, "在线");
          helper.setBackgroundRes(R.id.tvStatus, R.mipmap.onlinebg);
        } else {
          helper.setText(R.id.tvStatus, "离线");
          helper.setBackgroundRes(R.id.tvStatus, R.mipmap.offlinebg);
        }
        helper.setText(R.id.tvName, multipleCar.getText());
        helper.itemView.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {
            onLoadListener.jump(multipleCar);
          }
        });
        break;

      case MultipleCar.ORG:
        MultipleOrg multipleOrg = (MultipleOrg) item;
        helper.setText(R.id.tvName, multipleOrg.getText());
        helper.setText(R.id.tvOnlineCount, multipleOrg.getOnlineCount() + "");
        helper.setText(R.id.tvTotalCount, "/" + multipleOrg.getTotalCount() + ")");
        helper.itemView.setPadding(multipleOrg.getOrgLevel() * MARGIN, 0, 0, 0);
        if (multipleOrg.isExpanded()) {
          helper.setImageResource(R.id.ivArrow, R.mipmap.up);
        } else {
          helper.setImageResource(R.id.ivArrow, R.mipmap.down2);
        }
        helper.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            int pos = helper.getAdapterPosition();
            if (multipleOrg.isRequesting() && multipleOrg.getSubItems().size() != 0) {
              if (((MultipleOrg) item).isExpanded()) {
                collapse(pos);
              } else {
                expand(pos);
              }
            }

            if (!multipleOrg.isRequesting() && multipleOrg.isParent()) {
              multipleOrg.setRequesting(true);
              onLoadListener.onLoad(multipleOrg, pos);
            }
          }
        });

        break;

      default:
        break;

    }
  }
}
