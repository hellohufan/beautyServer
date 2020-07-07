package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.MyLocationData.Builder;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult.ERRORNO;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.app.utils.NotificationsUtils;
import com.irontower.gcbuser.di.component.DaggerMainComponent;
import com.irontower.gcbuser.di.module.MainModule;
import com.irontower.gcbuser.mvp.contract.MainContract.View;
import com.irontower.gcbuser.mvp.model.api.Api;
import com.irontower.gcbuser.mvp.model.entity.Module;
import com.irontower.gcbuser.mvp.model.entity.User;
import com.irontower.gcbuser.mvp.presenter.MainPresenter;
import com.irontower.gcbuser.mvp.ui.adapter.AddressAdapter;
import com.irontower.gcbuser.mvp.ui.widget.MyDialog;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class MainActivity extends BaseActivity<MainPresenter> implements View {

  @BindView(R.id.bmapView)
  MapView mMapView;

  @BindView(R.id.toolBar)
  Toolbar toolbar;
  @BindView(R.id.drawer_layout)
  DrawerLayout drawerLayout;

  @BindView(R.id.tvName)
  TextView tvName;

  // 定位相关
  @BindView(R.id.tvStartPosition)
  TextView tvStartPosition;

  @BindView(R.id.tvEndPosition)
  TextView tvEndPosition;
  @BindView(R.id.tvTitleCity)
  TextView tvTitleCity;
  @Inject
  Gson gson;
  @BindView(R.id.ivBackToLocation)
  ImageView ivBackToLocation;


  @BindView(R.id.ivUserImage)
  CircleImageView ivUserImage;

  @Inject
  RxPermissions mRxPermissions;
  @BindView(R.id.layApproveCar)
  LinearLayout layApproveCar;
  @BindView(R.id.layCarManage)
  LinearLayout layCarManage;
  @BindView(R.id.layCarTrace)
  LinearLayout layCarTrace;
  @BindView(R.id.layPeopleCarManage)
  LinearLayout layPeopleCarManage;
  @BindView(R.id.layShareRentCar)
  LinearLayout layShareRentCar;


  private BaiduMap mBaiduMap;
  boolean isFirstLoc = true;
  private GeoCoder mGeocodeStartSearch;


  public LocationClient mLocationClient;
  private Marker startMarker;
  private String city = "福州市";
  private MaterialDialog exitAppDialog;
  private MyDialog startDialog, endDialog;
  private Boolean needGeo = true;
  private LatLng startLatLng, endLatLng, latLng1;
  private String startPosition, endPosition, endCity;
  private String startCity;
  private PoiSearch startPoiSearch, endPoiSearch;
  private User user;
  @Inject
  MaterialDialog materialDialog;


  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerMainComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .mainModule(new MainModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0

  }

  @Override
  public void initData(Bundle savedInstanceState) {

    mPresenter.getDicts();
    mPresenter.getCarUses();
    List<Module> modules = new ArrayList<>();
    for (JsonElement jsonElement : new JsonParser()
        .parse(SPUtils.getInstance().getString("modules")).getAsJsonArray()) {
      modules.add(gson.fromJson(jsonElement, Module.class));
    }
    for (Module module : modules) {
      if (StringUtils.equals(module.getId(), "10001")) {
        layApproveCar.setVisibility(android.view.View.VISIBLE);
      }
      if (StringUtils.equals(module.getId(), "10002")) {
        layCarManage.setVisibility(android.view.View.VISIBLE);
      }
      if (StringUtils.equals(module.getId(), "10003")) {
        layPeopleCarManage.setVisibility(android.view.View.VISIBLE);
      }
      if (StringUtils.equals(module.getId(), "10004")) {
        layCarTrace.setVisibility(android.view.View.VISIBLE);
      }
    }

    user = AppUtil.getUser();
    tvName.setText(user.getUserName());
    GlideArms.with(getActivity()).load(Api.APP_DOMAIN + user.getHeadUrl())
        .placeholder(R.mipmap.passenger_photo)
        .error(R.mipmap.passenger_photo).into(ivUserImage);

    mPresenter.init();
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawerLayout, toolbar, R.string.start,
        R.string.end);
    toggle.syncState();
    toolbar.setNavigationIcon(R.mipmap.user_icon);
    drawerLayout.addDrawerListener(toggle);

    MyLocationListener myLocationListener = new MyLocationListener();
    MyGeoCoderResultListener myGeoCoderResultListener = new MyGeoCoderResultListener();
    mGeocodeStartSearch = GeoCoder.newInstance();

    mGeocodeStartSearch.setOnGetGeoCodeResultListener(myGeoCoderResultListener);

    mLocationClient = new LocationClient(getApplicationContext());

    mLocationClient.registerLocationListener(myLocationListener);
    LocationClientOption option = new LocationClientOption();
    option.setLocationMode(LocationMode.Hight_Accuracy);
    option.setCoorType("bd09ll");
    option.setOpenGps(true); // 打开gps
    option.setLocationNotify(true);
    option.setScanSpan(10);
    option.setWifiCacheTimeOut(5 * 60 * 1000);
    mLocationClient.setLocOption(option);
    mLocationClient.start();
    mMapView.showZoomControls(false);
    mBaiduMap = mMapView.getMap();
    mBaiduMap.setMyLocationEnabled(true);
    mPresenter.checkAppUpdate();
    mPresenter.isUrgentUser();

    if (!NotificationsUtils.isNotificationEnabled(this)) {
      new MaterialDialog.Builder(
          getActivity())
          .title("提示").content("尚未开启通知栏权限,是否开启?")
          .positiveText("确认")
          .negativeText("取消")
          .negativeColor(ArmsUtils.getColor(getActivity(), R.color.empty_color))
          .onPositive(new SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog,
                @NonNull DialogAction which) {
              Intent localIntent = new Intent();
              localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent
                    .setData(Uri.fromParts("package", getActivity().getPackageName(), null));
              } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);

                localIntent.setClassName("com.android.settings",
                    "com.android.settings.InstalledAppDetails");

                localIntent.putExtra("com.android.settings.ApplicationPkgName",
                    getActivity().getPackageName());
              }
              startActivity(localIntent);
            }
          })
          .show()
      ;


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


  @OnClick({R.id.layStartPosition, R.id.layEndPosition, R.id.layBasicInfo, R.id.layExitApp,
      R.id.ivUserImage, R.id.layTravelRecords, R.id.layApproveCar, R.id.layPeopleCarManage,
      R.id.laySystemMessage, R.id.layCarManage, R.id.layChartStatistial, R.id.ivBackToLocation,
      R.id.layShareRentCar, R.id.layCarTrace})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layStartPosition:
        if (startDialog != null) {
          startDialog.show();
          TextView tvCity = startDialog.findViewById(R.id.tvCity);

          tvCity.setText(city);
        } else {
          startDialog = new MyDialog(this);
          startDialog.show();
          LinearLayout linearLayout = startDialog.findViewById(R.id.layCancel);
          TextView tvCancel = startDialog.findViewById(R.id.tvCancel);

          RecyclerView recyclerView = startDialog.findViewById(R.id.recyclerView);
          LinearLayout layCity = startDialog.findViewById(R.id.layCity);
          TextView tvCity = startDialog.findViewById(R.id.tvCity);
          tvCity.setText(city);
          layCity.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
              Intent intent = new Intent();
              intent.putExtra("type", EventBusTags.STARTCITY);
              intent.putExtra("city", city);
              intent.setClass(getActivity(), CityPickerActivity.class);
              startActivity(intent);
            }
          });
          LayoutManager layoutManager = new LinearLayoutManager(getActivity());

          AppCompatEditText etSearch = startDialog.findViewById(R.id.etSearch);
          linearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
              if ("确定".equals(tvCancel.getText().toString())) {

                startLatLng = null;
                startCity = tvCity.getText().toString();
                startPosition = etSearch.getText().toString();

                tvStartPosition.setText(startPosition);
              }
              startDialog.dismiss();
            }
          });

          etSearch.setHint(ArmsUtils.getString(getActivity(), R.string.where_start));
          etSearch.setFocusable(true);
          etSearch.setFocusableInTouchMode(true);
          etSearch.requestFocus();
          Window window = startDialog.getWindow();
          if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

          }

          recyclerView.setLayoutManager(layoutManager);
          recyclerView.setHasFixedSize(true);
          AddressAdapter addressAdapter = new AddressAdapter(R.layout.item_address,
              new ArrayList<>());
          addressAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, android.view.View view,
                int position) {
              PoiInfo poiInfo = addressAdapter.getData().get(position);

              MapStatus.Builder builder = new MapStatus.Builder();
              builder.target(poiInfo.location);
              city = poiInfo.city;

              needGeo = false;
              mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
              startLatLng = poiInfo.location;
              startPosition = poiInfo.name;
              startCity = poiInfo.city;
              startDialog.dismiss();
              tvStartPosition.setText(poiInfo.name);
              tvTitleCity.setText(city);


            }
          });

          recyclerView.setAdapter(addressAdapter);
          OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult result) {

              if (result.getAllPoi() != null) {
                addressAdapter.setNewData(result.getAllPoi());
              } else {
                addressAdapter.setNewData(new ArrayList<>());
              }
              //获取POI检索结果
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult result) {
              //获取Place详情页检索结果
            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
            }
          };
          startPoiSearch = PoiSearch.newInstance();
          startPoiSearch.setOnGetPoiSearchResultListener(poiListener);

          etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
              if (charSequence.length() == 0) {
                tvCancel.setText("取消");
              } else {
                tvCancel.setText("确定");
              }
              startPoiSearch.searchInCity((new PoiCitySearchOption())
                  .city(tvCity.getText().toString())
                  .keyword(charSequence.toString())
                  .isReturnAddr(true));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
          });

          startDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
              etSearch.setText("");
              addressAdapter.setNewData(new ArrayList<>());
            }
          });

        }

        break;
      case R.id.layEndPosition:
        if (endDialog != null) {
          endDialog.show();
          TextView tvCity = endDialog.findViewById(R.id.tvCity);
          tvCity.setText(city);
        } else {
          endDialog = new MyDialog(this);
          endDialog.show();
          LinearLayout linearLayout = endDialog.findViewById(R.id.layCancel);

          RecyclerView recyclerView = endDialog.findViewById(R.id.recyclerView);
          LayoutManager layoutManager = new LinearLayoutManager(getActivity());
          LinearLayout layCity = endDialog.findViewById(R.id.layCity);

          TextView tvCity = endDialog.findViewById(R.id.tvCity);
          TextView tvCancel = endDialog.findViewById(R.id.tvCancel);
          tvCity.setText(city);
          layCity.setOnClickListener(view13 -> {
            Intent intent = new Intent();
            intent.putExtra("type", EventBusTags.ENDCITY);
            intent.putExtra("city", city);
            intent.setClass(getActivity(), CityPickerActivity.class);
            startActivity(intent);
          });
          AppCompatEditText etSearch = endDialog.findViewById(R.id.etSearch);
          linearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
              if ("确定".equals(tvCancel.getText().toString())) {
                endCity = tvCity.getText().toString();
                endPosition = etSearch.getText().toString();
                endLatLng = null;
                if (startPosition != null && startCity != null) {
                  Intent intent = new Intent();
                  intent.putExtra("startPosition", startPosition);
                  intent.putExtra("endCity", endCity);
                  intent.putExtra("startCity", startCity);
                  intent.putExtra("endPosition", endPosition);
                  intent.putExtra("endLatLng", endLatLng);
                  intent.putExtra("startLatLng", startLatLng);
                  intent.setClass(getActivity(), OrderCarActivity.class);
                  startActivity(intent);
                } else {
                  ArmsUtils.makeText(getActivity(), "出发地或目的地不能为空");
                }
              }
              endDialog.dismiss();
            }
          });
          Window window = endDialog.getWindow();
          if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

          }
          recyclerView.setLayoutManager(layoutManager);
          recyclerView.setHasFixedSize(true);
          AddressAdapter addressAdapter = new AddressAdapter(R.layout.item_address,
              new ArrayList<>());

          addressAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, android.view.View view,
                int position) {
              PoiInfo poiInfo = addressAdapter.getData().get(position);

              endLatLng = poiInfo.location;
              endPosition = poiInfo.name;
              endCity = poiInfo.city;
              endDialog.dismiss();

              if (startPosition != null && startCity != null) {
                Intent intent = new Intent();
                intent.putExtra("startPosition", startPosition);
                intent.putExtra("endCity", endCity);
                intent.putExtra("startCity", startCity);
                intent.putExtra("endPosition", endPosition);
                intent.putExtra("endLatLng", endLatLng);
                intent.putExtra("startLatLng", startLatLng);
                intent.setClass(getActivity(), OrderCarActivity.class);
                startActivity(intent);
              } else {
                ArmsUtils.makeText(getActivity(), "出发地或目的地不能为空");
              }
            }
          });

          recyclerView.setAdapter(addressAdapter);

          endDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
              etSearch.setText("");
              addressAdapter.setNewData(new ArrayList<>());
            }
          });

          endPoiSearch = PoiSearch.newInstance();
          endPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
              if (poiResult.getAllPoi() != null) {
                addressAdapter.setNewData(poiResult.getAllPoi());
              } else {
                addressAdapter.setNewData(new ArrayList<>());
              }

            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
          });
          etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
              if (charSequence.length() == 0) {
                tvCancel.setText("取消");
              } else {
                tvCancel.setText("确定");
              }
              endPoiSearch.searchInCity((new PoiCitySearchOption())
                  .city(tvCity.getText().toString())
                  .keyword(charSequence.toString())
                  .isReturnAddr(true));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
          });


        }

        break;
      case R.id.layBasicInfo:
        ArmsUtils.startActivity(this, BasicInfoActivity.class);

        break;
      case R.id.layExitApp:
        mPresenter.logout();

        if (exitAppDialog == null) {
          exitAppDialog = new MaterialDialog.Builder(this)
              .customView(R.layout.dialog_get_order, false)
              .backgroundColor(AlertDialog.THEME_HOLO_LIGHT)
              .show();
          android.view.View customView = exitAppDialog.getCustomView();
          TextView textView = customView.findViewById(R.id.tvContent);
          RelativeLayout layCancel = customView.findViewById(R.id.layCancel);
          RelativeLayout layConfirm = customView.findViewById(R.id.layConfirm);
          textView.setText(R.string.exit_app_info);
          layCancel.setOnClickListener(view1 -> exitAppDialog.dismiss());
          layConfirm.setOnClickListener(view1 -> {
            exitAppDialog.dismiss();
            JPushInterface.resumePush(getApplicationContext());

            SPUtils.getInstance().clear();
            JPushInterface.deleteAlias(getActivity(), (int) System.currentTimeMillis());
            ArmsUtils.startActivity(getActivity(), LoginActivity.class);
            ArmsUtils.killAll();
          });

        } else {
          exitAppDialog.show();
        }

        break;
      case R.id.ivUserImage:

        ArmsUtils.startActivity(this, BasicInfoActivity.class);
        break;
      case R.id.layTravelRecords:
        ArmsUtils.startActivity(this, OrderRecordActivity.class);
        break;
      case R.id.layApproveCar:
        ArmsUtils.startActivity(this, ApproveUseVehicleActivity.class);
        break;
      case R.id.layPeopleCarManage:
        ArmsUtils.startActivity(this, ManagePeopleCarActivity.class);
        break;
      case R.id.laySystemMessage:
        ArmsUtils.startActivity(this, MessageActivity.class);
        break;
      case R.id.layCarManage:
        ArmsUtils.startActivity(this, ManageDispatchVehicleActivity.class);
        break;
      case R.id.layChartStatistial:
        ArmsUtils.startActivity(this, ChartStatisticsActivity.class);
        break;
      case R.id.ivBackToLocation:
        if (latLng1 != null) {
          MapStatus.Builder builder = new MapStatus.Builder();
          builder.target(latLng1).zoom(18.0f);
          mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }

        break;
      case R.id.layShareRentCar:
        PackageManager packageManager = getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage("com.utripcar.youchichuxing");
        if (intent == null) {
          Uri uri = Uri.parse("http://www.utripcar.com/download/newapp.html");
          intent = new Intent(Intent.ACTION_VIEW, uri);
        }
        startActivity(intent);
        break;
      case R.id.layCarTrace:
        ArmsUtils.startActivity(this, CarTraceActivity.class);
        break;
      default:

        break;
    }
  }


  public class MyLocationListener implements BDLocationListener {

    @Override
    public void onReceiveLocation(BDLocation location) {

      if (location == null || mMapView == null) {
        return;
      }

      int mCurrentDirection = 0;
      MyLocationData locData = new Builder()
          .accuracy(location.getRadius())
          // 此处设置开发者获取到的方向信息，顺时针0-360
          .direction(mCurrentDirection).latitude(location.getLatitude())
          .longitude(location.getLongitude()).build();
      mBaiduMap.setMyLocationData(locData);
      if (isFirstLoc) {
        isFirstLoc = false;
        LatLng ll = new LatLng(location.getLatitude(),
            location.getLongitude());
        latLng1 = ll;
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        BitmapDescriptor bitmap = BitmapDescriptorFactory
            .fromResource(R.mipmap.start);

        OverlayOptions option = new MarkerOptions()
            .position(ll)
            .icon(bitmap);
        startMarker = (Marker) mBaiduMap.addOverlay(option);
        mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
          @Override
          public void onMapStatusChangeStart(MapStatus mapStatus) {
            tvStartPosition.setHint("正在获取出发位置");


          }

          @Override
          public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
          }

          @Override
          public void onMapStatusChange(MapStatus mapStatus) {

            startMarker.setPosition(mapStatus.target);

          }

          @Override
          public void onMapStatusChangeFinish(MapStatus mapStatus) {
            if (needGeo) {
              mGeocodeStartSearch
                  .reverseGeoCode(new ReverseGeoCodeOption().location(mapStatus.target));

            } else {
              needGeo = true;
            }


          }

        });

        mGeocodeStartSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
      }
    }


  }


  public class MyGeoCoderResultListener implements OnGetGeoCoderResultListener {

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
      if (geoCodeResult == null || geoCodeResult.error != ERRORNO.NO_ERROR) {
        //没有检索到结果
        ArmsUtils.makeText(getActivity(), "没有查到地址");

      }

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
      if (reverseGeoCodeResult == null
          || reverseGeoCodeResult.error != ERRORNO.NO_ERROR) {
        ArmsUtils.makeText(getActivity(), "没有查到地址");
        return;
      }

      if (reverseGeoCodeResult.getPoiList().size() == 0) {
        tvStartPosition.setText(reverseGeoCodeResult.getAddress());


      } else {

        tvStartPosition.setText(reverseGeoCodeResult.getPoiList().get(0).name);

      }

      city = reverseGeoCodeResult.getAddressDetail().city;
      startLatLng = reverseGeoCodeResult.getLocation();
      startPosition = tvStartPosition.getText().toString();
      startCity = reverseGeoCodeResult.getAddressDetail().city;
      tvTitleCity.setText(startCity);

    }
  }


  @Override
  protected void onPause() {
    mMapView.onPause();
    super.onPause();
  }

  @Override
  protected void onResume() {
    mMapView.onResume();
    super.onResume();
    //为系统的方向传感器注册监听器

  }

  @Override
  protected void onDestroy() {
    // 退出时销毁定位
    mGeocodeStartSearch.destroy();
    mLocationClient.stop();
    // 关闭定位图层
    mBaiduMap.setMyLocationEnabled(false);
    mMapView.onDestroy();
    mMapView = null;
    super.onDestroy();
    gson = null;
    if (exitAppDialog != null) {
      exitAppDialog.dismiss();
      exitAppDialog = null;
    }

    if (startDialog != null) {
      startDialog.dismiss();
      startDialog = null;
    }
    if (endDialog != null) {
      endDialog.dismiss();
      endDialog = null;
    }
    if (materialDialog != null) {
      materialDialog.dismiss();
      materialDialog = null;
    }

    mRxPermissions = null;

    if (startPoiSearch != null) {
      startPoiSearch.destroy();
    }
    if (endPoiSearch != null) {
      endPoiSearch.destroy();
    }
  }

  @Override
  public Activity getActivity() {
    return this;
  }


  @Override
  public RxPermissions getRxPermissions() {
    return mRxPermissions;
  }

  @Subscriber(tag = EventBusTags.CHANGEHEADURL, mode = ThreadMode.MAIN)
  public void changeImage(String url) {
    GlideArms.with(getActivity()).load(Api.APP_DOMAIN + url)
        .placeholder(R.mipmap.passenger_photo)
        .error(R.mipmap.passenger_photo).into(ivUserImage);

  }

  @Subscriber(tag = EventBusTags.CHANGEUSERNAME, mode = ThreadMode.MAIN)
  public void changeUserName(String userName) {
    tvName.setText(userName);

  }


  @Subscriber(tag = EventBusTags.STARTCITY, mode = ThreadMode.MAIN)
  public void startCity(String city) {
    if (startDialog != null) {
      TextView textView = startDialog.findViewById(R.id.tvCity);
      textView.setText(city);

    }


  }

  @Subscriber(tag = EventBusTags.ENDCITY, mode = ThreadMode.MAIN)
  public void endCity(String city) {
    if (endDialog != null) {
      TextView textView = endDialog.findViewById(R.id.tvCity);
      textView.setText(city);
    }


  }
}
