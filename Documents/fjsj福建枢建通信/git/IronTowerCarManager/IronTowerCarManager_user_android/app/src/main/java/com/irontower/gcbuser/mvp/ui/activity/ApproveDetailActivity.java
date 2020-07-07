package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.blankj.utilcode.util.StringUtils;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.di.component.DaggerOrderDetailComponent;
import com.irontower.gcbuser.di.module.ApproveDetailModule;
import com.irontower.gcbuser.mvp.contract.ApproveDetailContract.View;
import com.irontower.gcbuser.mvp.model.api.Api;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import com.irontower.gcbuser.mvp.model.entity.Dict;
import com.irontower.gcbuser.mvp.model.entity.Order;
import com.irontower.gcbuser.mvp.model.entity.OrderCar;
import com.irontower.gcbuser.mvp.presenter.ApproveDetailPresenter;
import com.irontower.gcbuser.mvp.ui.adapter.TimeLineAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;


public class ApproveDetailActivity extends BaseActivity<ApproveDetailPresenter> implements
    View {

  @BindView(R.id.rvTimeLine)
  RecyclerView rvTimeLine;
  @Inject
  MaterialDialog materialDialog;

  @BindView(R.id.tvAccompany)
  TextView tvAccompany;

  @BindView(R.id.tvOrgName)
  TextView tvOrgName;

  @Inject
  LayoutManager timeLineLayoutManager;

  @Inject
  TimeLineAdapter timeLineAdapter;
  @BindView(R.id.tvPassengerNum)
  TextView tvPassengerNum;
  @BindView(R.id.tvCarUse)
  TextView tvCarUse;
  @BindView(R.id.tvCarDetail)
  TextView tvCarDetail;
  @BindView(R.id.tvCreateTime)
  TextView tvCreateTime;
  @BindView(R.id.tvBeginTime)
  TextView tvBeginTime;
  @BindView(R.id.tvDistanceType)
  TextView tvDistanceType;
  @BindView(R.id.tvBeginAddr)
  TextView tvBeginAddr;
  @BindView(R.id.tvEndAddr)
  TextView tvEndAddr;
  @BindView(R.id.tvEndTime)
  TextView tvEndTime;
  @BindView(R.id.tvRemark)
  TextView tvRemark;
  @BindView(R.id.tvOrderReason)
  TextView tvOrderReason;
  @BindView(R.id.tvOrderNo)
  TextView tvOrderNo;
  @BindView(R.id.tvNodeName)
  TextView tvNodeName;
  @BindView(R.id.layContent)
  LinearLayout layContent;
  private MaterialDialog agreeDialog, rejectDialog;
  private List<Dict> carTypeDicts = new ArrayList<>();
  private String passengerTel;
  @Inject
  RxPermissions rxPermissions;
  @BindView(R.id.layOneAttachment)
  LinearLayout layOneAttachment;
  @BindView(R.id.layManyAttachment)
  LinearLayout layManyAttachment;
  @BindView(R.id.layNoAttachment)
  LinearLayout layNoAttachment;
  @BindView(R.id.ivOneAttachment)
  ImageView ivOneAttachment;
  @BindView(R.id.tvOneAttachment)
  TextView tvOneAttachment;
  private Attach attach;
  private List<Attach> attaches = new ArrayList<>();


  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerOrderDetailComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .approveDetailModule(new ApproveDetailModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_approve_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {

    rvTimeLine.setNestedScrollingEnabled(false);
    rvTimeLine.setLayoutManager(timeLineLayoutManager);
    rvTimeLine.setAdapter(timeLineAdapter);
    carTypeDicts.addAll(AppUtil.getCarTypes());
    mPresenter.getData(getIntent().getStringExtra("todoId"));


  }


  @Override
  public void showLoading() {
    if (materialDialog != null) {
      materialDialog.show();
    }

  }

  @Override
  public void hideLoading() {
    if (materialDialog != null) {
      materialDialog.dismiss();
    }
  }

  @Override
  public void showMessage(@NonNull String message) {
    checkNotNull(message);
    ArmsUtils.snackbarText(message);
  }

  @Override
  public void launchActivity(@NonNull Intent intent) {
    checkNotNull(intent);
    ArmsUtils.startActivity(intent);
  }

  @Override
  public void killMyself() {
    finish();
  }


  @OnClick({R.id.layAgree, R.id.layReject, R.id.ivPassengerCall})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {

      case R.id.layAgree:
        if (agreeDialog == null) {
          agreeDialog = new Builder(this)
              .customView(R.layout.dialog_approve_choose, false)
              .backgroundColor(AlertDialog.THEME_HOLO_LIGHT)
              .show();
          android.view.View customView = agreeDialog.getCustomView();
          TextView textView = customView.findViewById(R.id.tvTitle);
          AppCompatEditText appCompatEditText = customView.findViewById(R.id.editText);
          appCompatEditText.setText("");

//          appCompatEditText.setHint("请输入同意理由");
          textView.setText("审批同意");
          appCompatEditText.setText("同意派车");
          RelativeLayout layCancel = customView.findViewById(R.id.layCancel);
          RelativeLayout layConfirm = customView.findViewById(R.id.layConfirm);
          layCancel.setOnClickListener(view1 -> agreeDialog.dismiss());
          layConfirm.setOnClickListener(view1 -> {
            agreeDialog.dismiss();
            mPresenter.approveOrder(getIntent().getStringExtra("todoId"), "1",
                appCompatEditText.getText().toString());
          });

        } else {
          agreeDialog.show();
        }
        break;
      case R.id.layReject:
        if (rejectDialog == null) {
          rejectDialog = new Builder(this)
              .customView(R.layout.dialog_approve_choose, false)
              .backgroundColor(AlertDialog.THEME_HOLO_LIGHT)
              .show();
          android.view.View customView = rejectDialog.getCustomView();
          TextView textView = customView.findViewById(R.id.tvTitle);
          textView.setText("审批驳回");
          AppCompatEditText appCompatEditText = customView.findViewById(R.id.editText);
          appCompatEditText.setText("不符合派车条件");

          RelativeLayout layCancel = customView.findViewById(R.id.layCancel);
          RelativeLayout layConfirm = customView.findViewById(R.id.layConfirm);
          layCancel.setOnClickListener(view1 -> rejectDialog.dismiss());
          layConfirm.setOnClickListener(view1 -> {
            if (StringUtils.isEmpty(appCompatEditText.getText().toString())) {
              ArmsUtils.makeText(getActivity(), "请输入驳回理由");
              return;
            }
            rejectDialog.dismiss();
            mPresenter.approveOrder(getIntent().getStringExtra("todoId"), "2",
                appCompatEditText.getText().toString());

          });

        } else {
          rejectDialog.show();
        }

        break;
      case R.id.ivPassengerCall:
        mPresenter.call(passengerTel);
        break;
      default:
    }
  }

  @Override
  public Activity getActivity() {
    return this;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (materialDialog != null) {
      materialDialog.dismiss();
      materialDialog = null;
    }
    if (agreeDialog != null) {
      agreeDialog.dismiss();
      agreeDialog = null;
    }
    if (rejectDialog != null) {
      rejectDialog.dismiss();
      rejectDialog = null;
    }


  }

  @Override
  public void init(Order order, List<OrderCar> orderCars, String node,
      List<Attach> attacheList) {
    layContent.setVisibility(android.view.View.VISIBLE);
    Map<String, Integer> map = new HashMap<>();
    for (OrderCar orderCar : orderCars) {
      String name = AppUtil.getDictName(orderCar.getCarType().toString(), carTypeDicts);
      if (map.containsKey(name)) {
        map.put(name, map.get(name) + 1);
      } else {
        map.put(name, 1);
      }
    }
    tvCarDetail.setText("用车人:" + order.getPassenger());

    tvNodeName.setText(AppUtil.getDictName(order.getState() + "", AppUtil.getOrderStates()));

    tvOrderNo.setText(order.getOrderNo());
    tvPassengerNum.setText(order.getPassengerNum() + "人");
    tvBeginTime.setText(order.getBeginTime());
    tvEndTime.setText(order.getEndTime());
    tvBeginAddr.setText(order.getBeginAddr());
    tvEndAddr.setText(order.getEndAddr());
    tvCreateTime.setText(order.getCreateTime());
    if (StringUtils.isEmpty(order.getDeptName())) {
      tvOrgName.setText("用车单位:" + order.getOrgName());
    } else {
      tvOrgName.setText("用车单位:" + order.getOrgName() + "/" + order.getDeptName());
    }
    tvDistanceType.setText(
        order.getDistanceType() == 2 ? "短途/" + order.getTripType() : "长途/" + order.getTripType());
    tvCarUse.setText(order.getUseName());
    tvRemark.setText(StringUtils.isEmpty(order.getRemark()) ? "无" : order.getRemark());
    tvAccompany.setText(StringUtils.isEmpty(order.getAccompany()) ? "无" : order.getAccompany());
    tvOrderReason.setText(order.getOrderReason());
    passengerTel = order.getPassengerTel();

    if (attacheList.size() == 0) {
      layNoAttachment.setVisibility(android.view.View.VISIBLE);
      layOneAttachment.setVisibility(android.view.View.GONE);
      layManyAttachment.setVisibility(android.view.View.GONE);
    } else {
      attach = attacheList.get(0);
      attaches.addAll(attacheList);
      if (attaches.size() == 1) {
        layOneAttachment.setVisibility(android.view.View.VISIBLE);
        layNoAttachment.setVisibility(android.view.View.GONE);
        layManyAttachment.setVisibility(android.view.View.GONE);
      } else {
        layManyAttachment.setVisibility(android.view.View.VISIBLE);
        layOneAttachment.setVisibility(android.view.View.VISIBLE);
        layNoAttachment.setVisibility(android.view.View.GONE);
      }

      tvOneAttachment.setText(attach.getAttachName());

      if (attach.getAttachName().endsWith(".docx") || attach.getAttachName().endsWith(".doc")) {
        ivOneAttachment.setImageResource(R.mipmap.word);
      } else if (attach.getAttachName().endsWith(".xls") || attach.getAttachName()
          .endsWith(".xlsx")) {
        ivOneAttachment.setImageResource(R.mipmap.excel);
      } else if (attach.getAttachName().endsWith(".jpg") || attach.getAttachName().endsWith(".png")
          || attach.getAttachName()
          .endsWith(".jpeg")) {
        ivOneAttachment.setImageResource(R.mipmap.picture);
      } else {
        ivOneAttachment.setImageResource(R.mipmap.other_file);
      }

    }

  }

  @Override
  public RxPermissions getRxpermission() {
    return rxPermissions;
  }

  @OnClick({R.id.layOneAttachment, R.id.layManyAttachment})
  public void onViewClicked3(android.view.View view) {
    switch (view.getId()) {
      case R.id.layOneAttachment:
        mPresenter
            .getFile(Api.APP_DOMAIN.substring(0, Api.APP_DOMAIN.length() - 1) + attach.getPath(),
                attach.getAttachName());
        break;
      case R.id.layManyAttachment:
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("attachList",
            (ArrayList<? extends Parcelable>) attaches);
        intent.setClass(getActivity(), ManyAttachmentActivity.class);
        startActivity(intent);
        break;
      default:
        break;
    }
  }

}
