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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemChildClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemLongClickListener;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.di.component.DaggerDispatchCarHistoryDetailComponent;
import com.irontower.gcbuser.di.module.DispatchCarHistoryDetailModule;
import com.irontower.gcbuser.mvp.contract.DispatchCarHistoryDetailContract;
import com.irontower.gcbuser.mvp.model.api.Api;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import com.irontower.gcbuser.mvp.model.entity.Dict;
import com.irontower.gcbuser.mvp.model.entity.FeeType;
import com.irontower.gcbuser.mvp.model.entity.Order;
import com.irontower.gcbuser.mvp.model.entity.OrderCar;
import com.irontower.gcbuser.mvp.model.entity.User;
import com.irontower.gcbuser.mvp.presenter.DispatchCarHistoryDetailPresenter;
import com.irontower.gcbuser.mvp.ui.adapter.OrderVehicleAdapter;
import com.irontower.gcbuser.mvp.ui.adapter.TimeLineAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class DispatchCarHistoryDetailActivity extends
    BaseActivity<DispatchCarHistoryDetailPresenter> implements
    DispatchCarHistoryDetailContract.View {

  @BindView(R.id.tvAccompany)
  TextView tvAccompany;
  @BindView(R.id.rvTimeLine)
  RecyclerView rvTimeLine;
  @BindView(R.id.rvOrderVehicle)
  RecyclerView rvOrderVehicle;
  @Named("OrderVehicle")
  @Inject
  LayoutManager orderVehicleManager;
  @Named("TimeLine")
  @Inject
  LayoutManager timeLineLayoutManager;
  @Inject
  TimeLineAdapter timeLineAdapter;
  @Inject
  RxPermissions rxPermissions;
  @Inject
  OrderVehicleAdapter orderVehicleAdapter;
  @Inject
  MaterialDialog materialDialog;

  @BindView(R.id.tvGoOnDispatchCar)
  TextView tvGoOnDispatchCar;
  @BindView(R.id.tvOrderNo)
  TextView tvOrderNo;

  @BindView(R.id.tvNodeName)
  TextView tvNodeName;
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
  @BindView(R.id.layContent)
  LinearLayout layContent;
  @BindView(R.id.tvOrgName)
  TextView tvOrgName;
  @BindView(R.id.tvOrderReason)
  TextView tvOrderReason;

  private String todoId, orderBeginTime, orderEndTime, useOrgId, flowId, passengerTel;
  private Order postOrder;
  private User user;
  private List<Dict> carTypeDicts = new ArrayList<>();
  private boolean dyddy = false;
  private List<OrderCar> orderCarList = new ArrayList<>();
  private Attach attach;
  private List<Attach> attachList = new ArrayList<>();
  private List<FeeType> feeTypes = new ArrayList<>();
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
  private boolean canReject = true;
  @BindView(R.id.layReject)
  LinearLayout layReject;
  @BindView(R.id.layHide)
  LinearLayout layHide;
  private MaterialDialog rejectDialog;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerDispatchCarHistoryDetailComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .dispatchCarHistoryDetailModule(new DispatchCarHistoryDetailModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_dispatch_car_history_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    todoId = getIntent().getStringExtra("todoId");
    flowId = getIntent().getStringExtra("flowId");
    carTypeDicts.addAll(AppUtil.getCarTypes());
    user = AppUtil.getUser();
    useOrgId = String.valueOf(AppUtil.getUser().getOrgId());
    rvOrderVehicle.setNestedScrollingEnabled(false);
    rvOrderVehicle.setLayoutManager(orderVehicleManager);
    rvOrderVehicle.setHasFixedSize(true);
    orderVehicleAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
      @Override
      public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        OrderCar orderCar = (OrderCar) adapter.getData().get(position);
        Intent intent = new Intent();
        intent.putExtra("orderCarNo", orderCar.getOrderCarNo());
        intent.putExtra("todoId", todoId);
        intent.putExtra("order", postOrder);
        intent.putExtra("carType", orderCar.getCarType().toString());
        intent.putExtra("useOrgId", useOrgId);
        intent.putExtra("orderBeginTime", orderBeginTime);
        intent.putExtra("orderEndTime", orderEndTime);
        intent.putExtra("orderNo", postOrder.getOrderNo());

        switch (view.getId()) {
          case R.id.tvUnit:
            if (orderCar.getState() == 2) {
              intent.setClass(getActivity(), ChoosePlatformActivity.class);
              startActivity(intent);
            } else if (orderCar.getState() == 3) {
              mPresenter.getFeeTypes(orderCar.getOrgId(), orderCar.getFeeType(), intent);

            } else {
              mPresenter.callPhone(orderCar.getDriverPhone());
            }
            break;
          case R.id.tvAgainDispatchCar:
            intent.putExtra("selectedCarId", orderCar.getCarId().toString());
            intent.putExtra("selectedDriverId", orderCar.getDriverId().toString());
            if (orderCar.getOrgId() != null && AppUtil.getUser().getOrgId()
                .equals(orderCar.getOrgId())) {
              mPresenter.getFeeTypes(orderCar.getOrgId(), orderCar.getFeeType(), intent);

            } else {
              intent.setClass(getActivity(), ChooseVehicleActivity.class);
              startActivity(intent);
            }



            break;
          case R.id.tvCancelUseCar:
            MaterialDialog agreeDialog = new Builder(getActivity())
                .customView(R.layout.dialog_approve_choose, false)
                .backgroundColor(AlertDialog.THEME_HOLO_LIGHT)
                .show();
            View customView = agreeDialog.getCustomView();
            TextView textView = customView.findViewById(R.id.tvTitle);
            AppCompatEditText appCompatEditText = customView.findViewById(R.id.editText);
            appCompatEditText.setHint(orderCar.getState() == 3 ? "请输入驳回理由" : "请输入协调取消理由");
            appCompatEditText.setText("");
            textView.setText(orderCar.getState() == 3 ? "驳回" : "协调取消");
            RelativeLayout layCancel = customView.findViewById(R.id.layCancel);
            RelativeLayout layConfirm = customView.findViewById(R.id.layConfirm);
            layCancel.setOnClickListener(view1 -> agreeDialog.dismiss());
            layConfirm.setOnClickListener(view1 -> {
              if (StringUtils.isEmpty(appCompatEditText.getText().toString())) {
                ArmsUtils.makeText(getActivity(), "理由不能为空");
                return;
              }
              mPresenter
                  .cancelUseCar(orderCar.getOrderCarNo(), appCompatEditText.getText().toString(),
                      orderCar.getState());
              agreeDialog.dismiss();

            });

            break;
          default:

        }
      }
    });
    orderVehicleAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        OrderCar orderCar = (OrderCar) adapter.getData().get(position);
        if (!user.getOrgId().equals(orderCar.getOrgId()) && orderCar.getState() == 21) {
          new Builder(getActivity())
              .title("提示")
              .content("是否确认删除该派车单?")
              .positiveText(R.string.confirm)
              .positiveColor(ArmsUtils.getColor(getActivity(), R.color.colorPrimary))
              .negativeColor(ArmsUtils.getColor(getActivity(), R.color.empty_color))
              .negativeText(R.string.cancel)
              .onPositive(new SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                  mPresenter.delDispatchCar(orderCar.getOrderCarNo());
                }
              })
              .show();
        }
        return false;
      }
    });
    orderVehicleAdapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        OrderCar orderCar = (OrderCar) adapter.getData().get(position);

        if (orderCar.getDriverId() == null) {
          ArmsUtils.makeText(getActivity(), "暂未派车");
        } else {
          Intent intent = new Intent();
          intent.putExtra("orderCar", orderCar);
          intent.putExtra("order", postOrder);
          intent.putParcelableArrayListExtra("attachList",
              (ArrayList<? extends Parcelable>) attachList);

          intent.setClass(getActivity(), OrderCarDetail2Activity.class);
          startActivity(intent);
        }


      }
    });

    LinearLayout emptyLayout = (LinearLayout) LayoutInflater.from(getActivity())
        .inflate(R.layout.empty_dispatch_car, (ViewGroup) rvOrderVehicle.getParent(), false);
    orderVehicleAdapter.setEmptyView(emptyLayout);
    rvOrderVehicle.setAdapter(orderVehicleAdapter);
    TextView tvDispatchCar = emptyLayout.findViewById(R.id.tvDispatchCar);
    tvDispatchCar.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        choosePlatform();
      }
    });
    rvTimeLine.setLayoutManager(timeLineLayoutManager);
    rvTimeLine.setAdapter(timeLineAdapter);
    rvTimeLine.setNestedScrollingEnabled(false);

    mPresenter.getData(todoId);

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


  @Override
  public Activity getActivity() {
    return this;
  }

  @Override
  public void init(Order order, List<OrderCar> orderCars, String node,
      List<Attach> attaches) {
    orderCarList.clear();
    orderCarList.addAll(orderCars);
    layContent.setVisibility(View.VISIBLE);
    Map<String, Integer> map = new HashMap<>();
    Set<Integer> orgIds = new HashSet<>();
    for (OrderCar orderCar : orderCars) {
      if (orderCar.getState() != 21) {
        canReject = false;
      }
      String name = null;
      if (orderCar.getCarType() != null) {
        name = AppUtil.getDictName(orderCar.getCarType().toString(), carTypeDicts);
      }
      if (map.containsKey(name)) {
        map.put(name, map.get(name) + 1);
      } else {
        map.put(name, 1);
      }

      if (orderCar.getOrgId() != null) {
        orgIds.add(orderCar.getOrgId());
      }
    }
    if (orgIds.size() > 0) {
      if (orgIds.contains(Integer.parseInt(useOrgId))) {
        dyddy = true;

        Iterator<OrderCar> iterator = orderCars.iterator();
        while (iterator.hasNext()) {
          OrderCar orderCar = iterator.next();
          if (orderCar.getOrgId() == null || !orderCar.getOrgId()
              .equals(Integer.parseInt(useOrgId))) {
            iterator.remove();
          }
        }


      }
    }

    if (dyddy || orderCars.size() == 0) {
      tvGoOnDispatchCar.setVisibility(View.GONE);
    } else {
      tvGoOnDispatchCar.setVisibility(View.VISIBLE);
    }
    tvGoOnDispatchCar.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        choosePlatform();
      }
    });
    orderVehicleAdapter.setNewData(orderCars);

    tvCarDetail.setText("用车人:" + order.getPassenger());
    if (StringUtils.isEmpty(order.getDeptName())) {
      tvOrgName.setText("用车单位:" + order.getOrgName());
    } else {
      tvOrgName.setText("用车单位:" + order.getOrgName() + "/" + order.getDeptName());
    }

    tvNodeName.setText(AppUtil.getDictName(order.getState() + "", AppUtil.getOrderStates()));
    orderBeginTime = order.getBeginTime();
    orderEndTime = order.getEndTime();

    tvOrderNo.setText(order.getOrderNo());
    tvPassengerNum.setText(order.getPassengerNum() + "人");
    passengerTel = order.getPassengerTel();
    tvBeginTime.setText(orderBeginTime);
    tvEndTime.setText(orderEndTime);
    tvBeginAddr.setText(order.getBeginAddr());
    tvEndAddr.setText(order.getEndAddr());
    tvCreateTime.setText(order.getCreateTime());
    tvDistanceType.setText(
        order.getDistanceType() == 2 ? "短途/" + order.getTripType() : "长途/" + order.getTripType());
    tvCarUse.setText(order.getUseName());

    tvAccompany.setText(StringUtils.isEmpty(order.getAccompany()) ? "无" : order.getAccompany());
    tvRemark.setText(StringUtils.isEmpty(order.getRemark()) ? "无" : order.getRemark());
    tvOrderReason.setText(order.getOrderReason());
    postOrder = order;
    postOrder.setMap(map);
    if (attaches.size() == 0) {
      layNoAttachment.setVisibility(View.VISIBLE);
      layOneAttachment.setVisibility(View.GONE);
      layManyAttachment.setVisibility(View.GONE);
    } else {
      attach = attaches.get(0);
      attachList.addAll(attaches);
      if (attaches.size() == 1) {
        layOneAttachment.setVisibility(View.VISIBLE);
        layNoAttachment.setVisibility(View.GONE);
        layManyAttachment.setVisibility(View.GONE);
      } else {
        layManyAttachment.setVisibility(View.VISIBLE);
        layOneAttachment.setVisibility(View.VISIBLE);
        layNoAttachment.setVisibility(View.GONE);
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
    if (!dyddy && (orderCars.size() == 0)) {
      layReject.setVisibility(View.VISIBLE);
      layHide.setVisibility(View.VISIBLE);
    } else {
      layReject.setVisibility(View.GONE);
      layHide.setVisibility(View.GONE);
    }
  }


  @Override
  public RxPermissions getRxpermission() {
    return rxPermissions;
  }


  private void choosePlatform() {
    Intent intent = new Intent();
    intent.putExtra("todoId", todoId);
    intent.putExtra("order", postOrder);
    intent.putExtra("useOrgId", useOrgId);
    intent.putExtra("orderBeginTime", orderBeginTime);
    intent.putExtra("orderEndTime", orderEndTime);
    intent.putExtra("orderNo", postOrder.getOrderNo());

    intent.setClass(getActivity(), ChoosePlatformActivity.class);
    startActivity(intent);
  }


  @Subscriber(tag = EventBusTags.DISPATCHCARSUCCESS, mode = ThreadMode.MAIN)
  public void diapatchCarSuccess(String orderNo) {
    mPresenter.getData(todoId);

  }

  @Subscriber(tag = EventBusTags.RENT_CAR_SUCCESS, mode = ThreadMode.MAIN)
  public void rentCarSuccess(String str) {
    mPresenter.getData(todoId);

  }

  @Subscriber(tag = EventBusTags.DEL_DISPATCH_CAR, mode = ThreadMode.MAIN)
  public void delCarSuccess(String str) {
    mPresenter.getData(todoId);

  }

  @Subscriber(tag = EventBusTags.CANCEL_USE_CAR, mode = ThreadMode.MAIN)
  public void cancelUseCar(String str) {
    mPresenter.getData(todoId);

  }

  @OnClick(R.id.ivPassengerCall)
  public void onPassengerCallClick() {
    mPresenter.callPhone(passengerTel);
  }

  @OnClick({R.id.layOneAttachment, R.id.layManyAttachment})
  public void onViewClicked3(View view) {
    switch (view.getId()) {
      case R.id.layOneAttachment:
        mPresenter
            .getFile(Api.APP_DOMAIN.substring(0, Api.APP_DOMAIN.length() - 1) + attach.getPath(),
                attach.getAttachName());
        break;
      case R.id.layManyAttachment:
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("attachList",
            (ArrayList<? extends Parcelable>) attachList);
        intent.setClass(getActivity(), ManyAttachmentActivity.class);
        startActivity(intent);
        break;
      default:
        break;
    }
  }

  @Override
  public void initFeeTypes(List<FeeType> rows, Integer feeType, Intent intent) {
    feeTypes.clear();

    feeTypes.addAll(rows);
    FeeType selectFeeType = new FeeType("无", null);
    feeTypes.add(0, selectFeeType);
    List<String> feeName = new ArrayList<>();
    int index = 0;

    for (int i = 0; i < feeTypes.size(); i++) {
      if (i != 0 && feeTypes.get(i).getFeeId().equals(feeType)) {
        index = i;
      }
      feeName.add(feeTypes.get(i).getFeeName());
    }

    new Builder(this)
        .title("收费方式选择")
        .items(feeName)
        .itemsCallbackSingleChoice(index,
            new MaterialDialog.ListCallbackSingleChoice() {
              @Override
              public boolean onSelection(MaterialDialog dialog, View itemView,
                  int which, CharSequence text) {
                return false;
              }


            })
        .positiveText("确认")
        .negativeText("取消")
        .negativeColor(ArmsUtils.getColor(getActivity(), R.color.empty_color))

        .onPositive(new SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            intent
                .putExtra("feeType", dialog.getSelectedIndex() == 0 ? null
                    : feeTypes.get(dialog.getSelectedIndex()).getFeeId().toString());
            intent.setClass(getActivity(), ChooseVehicleActivity.class);
            startActivity(intent);
          }
        })
        .show();
  }

  @OnClick(R.id.layReject)
  public void onViewClicked() {

    if (rejectDialog == null) {
      rejectDialog = new Builder(this)
          .customView(R.layout.dialog_approve_choose, false)
          .backgroundColor(AlertDialog.THEME_HOLO_LIGHT)
          .show();
      View customView = rejectDialog.getCustomView();
      TextView textView = customView.findViewById(R.id.tvTitle);
      textView.setText("审批驳回");
      AppCompatEditText appCompatEditText = customView.findViewById(R.id.editText);
      appCompatEditText.setText("");

      RelativeLayout layCancel = customView.findViewById(R.id.layCancel);
      RelativeLayout layConfirm = customView.findViewById(R.id.layConfirm);
      layCancel.setOnClickListener(view1 -> rejectDialog.dismiss());
      layConfirm.setOnClickListener(view1 -> {
        if (StringUtils.isEmpty(appCompatEditText.getText().toString())) {
          ArmsUtils.makeText(getActivity(), "驳回理由不能为空");
          return;
        }
        rejectDialog.dismiss();
        mPresenter.rejectUseCar(appCompatEditText.getText().toString(), todoId);

      });

    } else {
      rejectDialog.show();
    }


  }

}