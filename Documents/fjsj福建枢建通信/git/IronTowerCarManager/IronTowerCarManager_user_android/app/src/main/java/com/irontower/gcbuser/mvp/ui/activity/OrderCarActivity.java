package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult.ERRORNO;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.OptionsPickerView.Builder;
import com.bigkoo.pickerview.OptionsPickerView.OnOptionsSelectListener;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.TimePickerView.OnTimeSelectListener;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.app.utils.ConstantUtils;
import com.irontower.gcbuser.app.utils.DateUtil;
import com.irontower.gcbuser.di.component.DaggerOrderCarComponent;
import com.irontower.gcbuser.di.module.OrderCarModule;
import com.irontower.gcbuser.mvp.contract.OrderCarContract.View;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import com.irontower.gcbuser.mvp.model.entity.Dict;
import com.irontower.gcbuser.mvp.model.entity.Order;
import com.irontower.gcbuser.mvp.model.entity.SubmitOrder;
import com.irontower.gcbuser.mvp.model.entity.User;
import com.irontower.gcbuser.mvp.presenter.OrderCarPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class OrderCarActivity extends BaseActivity<OrderCarPresenter> implements
    View {


  @BindView(R.id.tvBeginAddr)
  TextView tvBeginAddr;
  @BindView(R.id.tvEndAddr)
  TextView tvEndAddr;
  @BindView(R.id.tvBeginTime)
  TextView tvBeginTime;
  @BindView(R.id.tvEndTime)
  TextView tvEndTime;
  @BindView(R.id.tvPassengerNum)
  TextView tvPassengerNum;
  @BindView(R.id.tvCarUse)
  TextView tvCarUse;
  @BindView(R.id.tvDistanceType)
  TextView tvDistanceType;
  @BindView(R.id.tvTripType)
  TextView tvTripType;


  @BindView(R.id.tvUserName)
  TextView tvUserName;
  @BindView(R.id.tvRemak)
  TextView tvRemak;
  @BindView(R.id.tvAccompany)
  TextView tvAccompany;
  @BindView(R.id.tvReason)
  TextView tvReason;
  @BindView(R.id.tvAttachment)
  TextView tvAttachment;
  private String name;
  private String mobile;
  private TimePickerView goPV, backPV;
  private OptionsPickerView passengerPV, reasonPV, journeyTypePV, tripTypePV;
  private List<String> passengerNum = new ArrayList<>();
  private List<String> carUses = new ArrayList<>();
  private List<String> tripTypes = new ArrayList<>();
  private List<String> distanceTypes = new ArrayList<>();
  private List<Dict> carUseDicts = new ArrayList<>();
  private MaterialDialog submitDialog;
  private String startPosition, endPosition, startCity, endCity;
  private LatLng startLatLng, endLatLng;
  private User user;
  private Order order;
  private GeoCoder mGeocodeStartSearch, mGeocodeEndSearch;
  @Inject
  Gson mGson;
  @Inject
  MaterialDialog materialDialog;
  private List<Attach> attachList = new ArrayList<>();


  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerOrderCarComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .orderCarModule(new OrderCarModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_order_car; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    user = AppUtil.getUser();
    if (user.isUrgentUser()) {
      carUses.add("应急用车");
    } else {
      carUses.addAll(AppUtil.getCarUses1());
    }
    carUseDicts.addAll(AppUtil.getCarUses());
    passengerNum.addAll(ConstantUtils.getPassengerNums());
    distanceTypes.addAll(AppUtil.getDistanceTypes1());
    tripTypes.add("单程");
    tripTypes.add("往返");
    if (getIntent().getParcelableExtra("order") != null) {
      order = getIntent().getParcelableExtra("order");
      startPosition = order.getBeginAddr();
      endPosition = order.getEndAddr();

      attachList.addAll(order.getAttachList());
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < attachList.size(); i++) {
        Attach attach = attachList.get(i);

        if (i != 0) {
          sb.append(",");
        }
        sb.append(attach.getAttachName());
      }
      tvAttachment.setText(sb);
      tvBeginAddr.setText(startPosition);
      tvEndAddr.setText(endPosition);
      tvBeginTime.setText(order.getBeginTime().substring(0, order.getBeginTime().length() - 3));
      tvEndTime.setText(order.getEndTime().substring(0, order.getEndTime().length() - 3));
      tvPassengerNum.setText(String.valueOf(order.getPassengerNum()));
      tvCarUse.setText(order.getUseName());

      tvDistanceType.setText(order.getDistanceType() == 2 ? "短途" : "长途");
      tvRemak.setText(order.getRemark());
      tvAccompany.setText(order.getAccompany());
      tvReason.setText(order.getOrderReason());
      tvTripType.setText(order.getTripType());
      startLatLng = AppUtil.getLatLng(order.getBeginGps());
      endLatLng = AppUtil.getLatLng(order.getEndGps());
      if (!StringUtils.equals(order.getPassenger(), user.getUserName()) || !StringUtils
          .equals(order.getPassengerTel(), user.getPhoneNo())) {
        name = order.getPassenger();
        mobile = order.getPassengerTel();
        tvUserName.setText(name + "  " + mobile);

      }
      if (startLatLng == null) {
        startCity = "福州市";
      } else {
        mGeocodeStartSearch = GeoCoder.newInstance();
        mGeocodeStartSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
          @Override
          public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

          }

          @Override
          public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null
                || reverseGeoCodeResult.error != ERRORNO.NO_ERROR) {
              ArmsUtils.makeText(getActivity(), "没有查到地址");
              return;
            }
            startCity = reverseGeoCodeResult.getAddressDetail().city;


          }
        });
        mGeocodeStartSearch.reverseGeoCode(new ReverseGeoCodeOption().location(startLatLng));


      }
      if (endLatLng == null) {
        endCity = "福州市";
      } else {
        mGeocodeEndSearch = GeoCoder.newInstance();

        mGeocodeEndSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
          @Override
          public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

          }

          @Override
          public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null
                || reverseGeoCodeResult.error != ERRORNO.NO_ERROR) {
              ArmsUtils.makeText(getActivity(), "没有查到地址");
              return;
            }
            endCity = reverseGeoCodeResult.getAddressDetail().city;

          }
        });
        mGeocodeEndSearch.reverseGeoCode(new ReverseGeoCodeOption().location(endLatLng));

      }


    } else {
      startCity = getIntent().getStringExtra("startCity");
      endCity = getIntent().getStringExtra("endCity");
      startPosition = getIntent().getStringExtra("startPosition");
      endPosition = getIntent().getStringExtra("endPosition");
      startLatLng = getIntent().getParcelableExtra("startLatLng");
      endLatLng = getIntent().getParcelableExtra("endLatLng");
      tvBeginAddr.setText(startPosition);
      tvEndAddr.setText(endPosition);
      if (user.isUrgentUser()) {
        tvPassengerNum.setText("1人");
        tvCarUse.setText("应急用车");
        tvDistanceType.setText("短途");
        Date now = DateUtil.getNow();
        tvBeginTime
            .setText(DateUtil.format(DateUtil.YYYY_MM_DD_HH_MI, DateUtil.addDateOfMinute(now, 30)));
        tvEndTime.setText(
            DateUtil.format(DateUtil.YYYY_MM_DD_HH_MI, DateUtil.addDateOfMinute(now, 210)));
      }

    }


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


  @OnClick({R.id.layBeginAddr, R.id.layEndAddr, R.id.layBeginTime, R.id.layEndTime,
      R.id.layPassengerNum, R.id.layCarUse, R.id.layDistanceType,
      R.id.layHelpCall, R.id.layRemark, R.id.laySubmit, R.id.layTripType, R.id.layReason,
      R.id.layAttachment, R.id.layAccompany})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layHelpCall:
        Intent intent2 = new Intent();
        intent2.putExtra("name", name);
        intent2.putExtra("mobile", mobile);
        intent2.setClass(getActivity(), HelpCallCarActivity.class);
        startActivity(intent2);

        break;
      case R.id.layRemark:
        Intent intent = new Intent();
        intent.setClass(getActivity(), RemarkActivity.class);
        intent.putExtra("remark", tvRemak.getText());
        startActivity(intent);
        break;
      case R.id.layAccompany:

        Intent intent4 = new Intent();
        intent4.setClass(getActivity(), AccompanyActivity.class);
        intent4.putExtra("accompany", tvAccompany.getText());
        startActivity(intent4);
        break;
      case R.id.layReason:
        Intent intent1 = new Intent();
        intent1.setClass(getActivity(), ReasonActivity.class);
        intent1.putExtra("reason", tvReason.getText());
        startActivity(intent1);
        break;
      case R.id.layBeginTime:
        previous();
        break;
      case R.id.layEndTime:
        next();
        break;
      case R.id.layTripType:
        tripType();
        break;
      case R.id.layPassengerNum:
        if (passengerPV != null) {

          if (!StringUtils.isEmpty(tvPassengerNum.getText().toString())) {
            passengerPV.setSelectOptions(
                passengerNum.indexOf(tvPassengerNum.getText().toString().split("人")[0]));
          }
          passengerPV.show();
        } else {
          passengerPV = new Builder(this, new OnOptionsSelectListener() {


            @Override
            public void onOptionsSelect(int options1, int options2, int options3,
                android.view.View v) {

              tvPassengerNum.setText(passengerNum.get(options1) + "人");
            }
          }).setCancelColor(ArmsUtils.getColor(getActivity(), R.color.text_hint))
              .setTitleText("用车人数")
              .setSubmitColor(ArmsUtils.getColor(getActivity(), R.color.colorPrimary))
              .setCancelText("取消")//取消按钮文字
              .setSubmitText("确认").build();
          passengerPV.setPicker(passengerNum);
          if (!StringUtils.isEmpty(tvPassengerNum.getText().toString())) {
            passengerPV.setSelectOptions(
                passengerNum.indexOf(tvPassengerNum.getText().toString().split("人")[0]));
          }
          passengerPV.show();
        }
        break;
      case R.id.layCarUse:
        if (reasonPV != null) {

          if (!StringUtils.isEmpty(tvCarUse.getText().toString())) {
            reasonPV.setSelectOptions(carUses.indexOf(tvCarUse.getText().toString()));
          }
          reasonPV.show();
        } else {
          reasonPV = new Builder(this, new OnOptionsSelectListener() {


            @Override
            public void onOptionsSelect(int options1, int options2, int options3,
                android.view.View v) {

              tvCarUse.setText(carUses.get(options1));
            }
          }).setCancelColor(ArmsUtils.getColor(getActivity(), R.color.text_hint))
              .setTitleText("用车用途")

              .setSubmitColor(ArmsUtils.getColor(getActivity(), R.color.colorPrimary))
              .setCancelText("取消")//取消按钮文字
              .setSubmitText("确认").build();
          reasonPV.setPicker(carUses);
          if (!StringUtils.isEmpty(tvCarUse.getText().toString())) {
            reasonPV.setSelectOptions(carUses.indexOf(tvCarUse.getText().toString()));
          }
          reasonPV.show();
        }
        break;
      case R.id.layDistanceType:
        if (journeyTypePV != null) {

          if (!StringUtils.isEmpty(tvDistanceType.getText().toString())) {
            journeyTypePV
                .setSelectOptions(distanceTypes.indexOf(tvDistanceType.getText().toString()));
          }
          journeyTypePV.show();
        } else {
          journeyTypePV = new Builder(this, new OnOptionsSelectListener() {


            @Override
            public void onOptionsSelect(int options1, int options2, int options3,
                android.view.View v) {

              tvDistanceType.setText(distanceTypes.get(options1));
            }
          }).setCancelColor(ArmsUtils.getColor(getActivity(), R.color.text_hint))
              .setTitleText("长/短途")

              .setSubmitColor(ArmsUtils.getColor(getActivity(), R.color.colorPrimary))
              .setCancelText("取消")//取消按钮文字
              .setSubmitText("确认").build();
          journeyTypePV.setPicker(distanceTypes);
          if (!StringUtils.isEmpty(tvDistanceType.getText().toString())) {
            journeyTypePV
                .setSelectOptions(distanceTypes.indexOf(tvDistanceType.getText().toString()));
          }

          journeyTypePV.show();
        }
        break;

      case R.id.laySubmit:

        submit();

        break;
      case R.id.layBeginAddr:
        if (StringUtils.isEmpty(startCity)) {
          ArmsUtils.makeText(getActivity(), "城市不能为空");
          return;
        }
        chooseStartPositon();
        break;

      case R.id.layEndAddr:
        if (StringUtils.isEmpty(endCity)) {
          ArmsUtils.makeText(getActivity(), "城市不能为空");
          return;
        }
        chooseEndPosition();
        break;
      case R.id.layAttachment:
        Intent intent3 = new Intent();
        intent3.putParcelableArrayListExtra("attachList",
            (ArrayList<? extends Parcelable>) attachList);
        intent3.setClass(getActivity(), AttachmentActivity.class);
        startActivity(intent3);
        break;
      default:
    }
  }

  private void tripType() {
    if (tripTypePV != null) {

      if (!StringUtils.isEmpty(tvTripType.getText().toString())) {
        tripTypePV.setSelectOptions(
            tripTypes.indexOf(tvTripType.getText().toString()));
      }
      tripTypePV.show();
    } else {
      tripTypePV = new Builder(this, new OnOptionsSelectListener() {


        @Override
        public void onOptionsSelect(int options1, int options2, int options3,
            android.view.View v) {

          tvTripType.setText(tripTypes.get(options1));
        }
      }).setCancelColor(ArmsUtils.getColor(getActivity(), R.color.text_hint))
          .setTitleText("单程/往返")
          .setSubmitColor(ArmsUtils.getColor(getActivity(), R.color.colorPrimary))
          .setCancelText("取消")//取消按钮文字
          .setSubmitText("确认").build();
      tripTypePV.setPicker(tripTypes);
      if (!StringUtils.isEmpty(tvTripType.getText().toString())) {
        tripTypePV.setSelectOptions(
            tripTypes.indexOf(tvTripType.getText().toString()));
      }
      tripTypePV.show();
    }

  }

  private void submit() {
    if (invalidate()) {
      if (submitDialog == null) {
        submitDialog = new MaterialDialog.Builder(this)
            .customView(R.layout.dialog_get_order, false)
            .backgroundColor(AlertDialog.THEME_HOLO_LIGHT)
            .show();
        android.view.View customView = submitDialog.getCustomView();
        TextView textView = customView.findViewById(R.id.tvContent);
        RelativeLayout layCancel = customView.findViewById(R.id.layCancel);
        RelativeLayout layConfirm = customView.findViewById(R.id.layConfirm);
        textView.setText(R.string.sure_use_car);
        layCancel.setOnClickListener(view1 -> submitDialog.dismiss());
        layConfirm.setOnClickListener(view1 -> {
          SubmitOrder submitOrder = new SubmitOrder();
          submitOrder.setPassengerNum(tvPassengerNum.getText().toString().split("人")[0]);
          submitOrder.setBeginGps(
              startLatLng == null ? "" : startLatLng.longitude + "," + startLatLng.latitude);
          submitOrder.setBeginAddr(tvBeginAddr.getText().toString());
          submitOrder
              .setEndGps(endLatLng == null ? "" : endLatLng.longitude + "," + endLatLng.latitude);
          submitOrder.setEndAddr(tvEndAddr.getText().toString());
          submitOrder.setRemark(tvRemak.getText().toString());
          submitOrder.setAccompany(tvAccompany.getText().toString());
          submitOrder.setOrderReason(tvReason.getText().toString());
          submitOrder.setTripType(tvTripType.getText().toString());
          submitOrder.setDistanceType("短途".equals(tvDistanceType.getText()) ? 2 : 3);
          submitOrder.setUseId(
              Integer.valueOf(AppUtil.getDictType(tvCarUse.getText().toString(), carUseDicts)));
          submitOrder.setEndTime(tvEndTime.getText().toString());
          submitOrder.setBeginTime(tvBeginTime.getText().toString());
          if (mobile == null) {
            submitOrder.setPassengerTel(user.getPhoneNo());
          } else {
            submitOrder.setPassengerTel(mobile);
          }
          if (name == null) {
            submitOrder.setPassenger(user.getUserName());
          } else {
            submitOrder.setPassenger(name);
          }

          if (order != null) {
            mPresenter.updateOrder(order.getOrderNo(), submitOrder, attachList);
          } else {
            mPresenter.submit(submitOrder, attachList);
          }

          submitDialog.dismiss();
        });

      } else {
        submitDialog.show();
      }
    }

  }

  private boolean invalidate() {

    if (StringUtils.isEmpty(tvBeginAddr.getText())) {
      ArmsUtils.makeText(getActivity(), "出发地点不能为空");
      return false;
    }
    if (StringUtils.isEmpty(tvEndAddr.getText())) {
      ArmsUtils.makeText(getActivity(), "目的地点不能为空");
      return false;
    }
    if (StringUtils.isEmpty(tvBeginTime.getText())) {
      ArmsUtils.makeText(getActivity(), "出发时间不能为空");
      return false;
    }
    if (DateUtil
        .diffDate(DateUtil.getDate(DateUtil.YYYY_MM_DD_HH_MI, tvBeginTime.getText().toString()),
            DateUtil.getNow()) < 0) {
      ArmsUtils.makeText(getActivity(), "出发时间不能是过去的时间");
      return false;
    }
    if (DateUtil
        .diffDate(DateUtil.getDate(DateUtil.YYYY_MM_DD_HH_MI, tvEndTime.getText().toString()),
            DateUtil.getNow()) < 0) {
      ArmsUtils.makeText(getActivity(), "预计返回时间不能是过去的时间");
      return false;
    }
    if (StringUtils.isEmpty(tvEndTime.getText())) {
      ArmsUtils.makeText(getActivity(), "预计返回时间不能为空");
      return false;
    }
    if (DateUtil
        .diffDate(DateUtil.getDate(DateUtil.YYYY_MM_DD_HH_MI, tvBeginTime.getText().toString()),
            DateUtil.getDate(DateUtil.YYYY_MM_DD_HH_MI, tvEndTime.getText().toString())) > 0) {
      ArmsUtils.makeText(getActivity(), "出发时间不能晚于预计返回时间");
      return false;
    }
    if (StringUtils.isEmpty(tvPassengerNum.getText())) {
      ArmsUtils.makeText(getActivity(), "用车人数不能为空");
      return false;
    }

    if (StringUtils.isEmpty(tvCarUse.getText())) {
      ArmsUtils.makeText(getActivity(), "用车用途不能为空");
      return false;
    }

    if (StringUtils.isEmpty(tvDistanceType.getText())) {
      ArmsUtils.makeText(getActivity(), "长/短途不能为空");
      return false;
    }
    if (StringUtils.isEmpty(tvTripType.getText())) {
      ArmsUtils.makeText(getActivity(), "单程/往返不能为空");
      return false;
    }
    if (mobile == null && StringUtils.isEmpty(user.getPhoneNo())) {
      ArmsUtils.makeText(getActivity(), "本人电话不能为空");
      return false;
    }
    if (!user.isUrgentUser() && StringUtils.isEmpty(tvReason.getText())) {
      ArmsUtils.makeText(getActivity(), "用车事由不能为空");
      return false;
    }

    return true;

  }

  private void chooseEndPosition() {
    Intent intent = new Intent();
    intent.putExtra("city", endCity);

    intent.setClass(getActivity(), ChoosePositionActivity.class);
    startActivity(intent);
  }

  private void chooseStartPositon() {

    Intent intent = new Intent();
    intent.putExtra("city", startCity);
    intent.putExtra("startPosition", startPosition);
    intent.putExtra("latLng", startLatLng);
    intent.setClass(getActivity(), ChooseStartPositionActivity.class);
    startActivity(intent);

  }

  private void previous() {
    if (goPV == null) {
      Calendar startDate = Calendar.getInstance();
      Calendar endDate = Calendar.getInstance();
      endDate.set(2030, 11, 31);
      goPV = new TimePickerView.Builder(this, new OnTimeSelectListener() {
        @Override
        public void onTimeSelect(Date date, android.view.View v) {//选中事件回调
          tvBeginTime.setText(DateUtil.format(DateUtil.YYYY_MM_DD_HH_MI, date));
          next();
        }
      }).setType(new boolean[]{true, true, true, true, true, false})
          .setTitleText("预计出发时间")
          .setTitleSize(17)
          .setCancelColor(ArmsUtils.getColor(getActivity(), R.color.text_hint))
          .setSubmitColor(ArmsUtils.getColor(getActivity(), R.color.colorPrimary))
          .setRangDate(startDate, endDate)//起始终止年月日设定
          .setCancelText("取消")//取消按钮文字
          .setSubmitText("下一步")//确认按钮文字
          .setLabel("年", "月", "日", "时", "分", null)
          .build();
      Calendar calendar = Calendar.getInstance();
      if (StringUtils.isEmpty(tvBeginTime.getText().toString())) {
        goPV.setDate(calendar);

      } else {
        calendar
            .setTime(DateUtil.getDate(DateUtil.YYYY_MM_DD_HH_MI, tvBeginTime.getText().toString()));
        goPV.setDate(calendar);

      }
      goPV.show();
    } else {
      Calendar calendar = Calendar.getInstance();
      if (StringUtils.isEmpty(tvBeginTime.getText().toString())) {
        goPV.setDate(calendar);

      } else {
        calendar
            .setTime(DateUtil.getDate(DateUtil.YYYY_MM_DD_HH_MI, tvBeginTime.getText().toString()));
        goPV.setDate(calendar);

      }
      goPV.show();
    }
  }

  private void next() {
    if (backPV == null) {
      Calendar startDate = Calendar.getInstance();
      Calendar endDate = Calendar.getInstance();
      endDate.set(2030, 11, 31);
      backPV = new TimePickerView.Builder(this, new OnTimeSelectListener() {
        @Override
        public void onTimeSelect(Date date, android.view.View v) {//选中事件回调
          tvEndTime.setText(DateUtil.format(DateUtil.YYYY_MM_DD_HH_MI, date));
        }
      }).setType(new boolean[]{true, true, true, true, true, false})
          .setTitleText("预计返回时间")

          .setTitleSize(17)
          .setCancelColor(ArmsUtils.getColor(getActivity(), R.color.text_hint))
          .setSubmitColor(ArmsUtils.getColor(getActivity(), R.color.colorPrimary))
          .setRangDate(startDate, endDate)//起始终止年月日设定
          .setCancelText("取消")//取消按钮文字
          .setSubmitText("确认")//确认按钮文字
          .setLabel("年", "月", "日", "时", "分", null)
          .build();

      Calendar calendar = Calendar.getInstance();
      if (StringUtils.isEmpty(tvEndTime.getText().toString())) {
        backPV.setDate(calendar);

      } else {
        calendar
            .setTime(DateUtil.getDate(DateUtil.YYYY_MM_DD_HH_MI, tvEndTime.getText().toString()));
        backPV.setDate(calendar);

      }
      backPV.show();


    } else {
      Calendar calendar = Calendar.getInstance();
      if (StringUtils.isEmpty(tvEndTime.getText().toString())) {
        backPV.setDate(calendar);

      } else {
        calendar
            .setTime(DateUtil.getDate(DateUtil.YYYY_MM_DD_HH_MI, tvEndTime.getText().toString()));
        backPV.setDate(calendar);

      }
      backPV.show();
    }
  }

  @Override
  public Activity getActivity() {
    return this;
  }

  @Subscriber(tag = EventBusTags.REMARK, mode = ThreadMode.MAIN)
  public void getRemark(String remark) {
    tvRemak.setText(remark);

  }

  @Subscriber(tag = EventBusTags.ACCOMPANY, mode = ThreadMode.MAIN)
  public void getAccompany(String accompany) {
    tvAccompany.setText(accompany);

  }

  @Subscriber(tag = EventBusTags.REASON, mode = ThreadMode.MAIN)
  public void getReason(String reason) {
    tvReason.setText(reason);

  }


  @Subscriber(tag = EventBusTags.MOBILE, mode = ThreadMode.MAIN)
  public void getMobile(Message message) {
    Map<String, Object> map = (Map<String, Object>) message.obj;
    if (map.containsKey("name")) {
      name = (String) map.get("name");
    } else {
      name = null;
    }
    if (map.containsKey("mobile")) {
      mobile = (String) map.get("mobile");
    } else {
      mobile = null;
    }
    if (StringUtils.isEmpty(name)) {
      tvUserName.setText(mobile);
    } else {
      tvUserName.setText(name + "  " + mobile);
    }

  }

  @Subscriber(tag = EventBusTags.ENDCITY3, mode = ThreadMode.MAIN)
  public void endCity(Map<String, Object> map) {
    endLatLng = (LatLng) map.get("latLng");
    endCity = (String) map.get("city");
    endPosition = (String) map.get("address");

    tvEndAddr.setText(endPosition);


  }

  @Subscriber(tag = EventBusTags.STARTCITY4, mode = ThreadMode.MAIN)
  public void startCity(Map<String, Object> map) {
    startLatLng = (LatLng) map.get("latLng");
    startCity = (String) map.get("city");
    startPosition = (String) map.get("address");

    tvBeginAddr.setText(startPosition);


  }

  @Override
  protected void onDestroy() {
    if (mGeocodeStartSearch != null) {
      mGeocodeStartSearch.destroy();
    }
    if (mGeocodeEndSearch != null) {
      mGeocodeEndSearch.destroy();
    }
    super.onDestroy();
    mGson = null;
    if (materialDialog != null) {
      materialDialog.dismiss();
      materialDialog = null;
    }
  }


  @Subscriber(tag = EventBusTags.UPLOAD_FILE_SUCCESS, mode = ThreadMode.MAIN)
  public void uploadFileSuccess(Map<String, Object> map) {
    Map<String, Object> attachMap = (Map<String, Object>) map.get("attachMap");
    List<Attach> realAttachList = (List<Attach>) map.get("realAttachList");
    Iterator iterator = attachList.iterator();
    while (iterator.hasNext()) {
      Attach tem = (Attach) iterator.next();
      if (tem.getAttachId() != null) {
        boolean deleted = true;
        for (Attach attach : realAttachList) {
          if (attach.getAttachId() != null && attach.getAttachId().equals(tem.getAttachId())) {
            deleted = false;
          }
        }
        if (deleted) {
          iterator.remove();
        }

      } else {
        iterator.remove();
      }
    }
    attachList.addAll((Collection<? extends Attach>) map.get("attachList"));
    if (attachList.size() == 0) {
      tvAttachment.setText(null);
    } else {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < attachList.size(); i++) {
        Attach attach = attachList.get(i);
        for (Entry<String, Object> stringObjectEntry : attachMap.entrySet()) {
          if (stringObjectEntry.getValue().equals(attach.getAttachName())) {
            attach.setUrl(stringObjectEntry.getKey());
          }
        }

        if (i != 0) {
          sb.append(",");
        }
        sb.append(attach.getAttachName());
      }
      tvAttachment.setText(sb);
    }


  }


}
