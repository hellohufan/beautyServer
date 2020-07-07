package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
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
import com.baidu.mapapi.search.core.SearchResult;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.di.component.DaggerChooseStartPositionComponent;
import com.irontower.gcbuser.di.module.ChooseStartPositionModule;
import com.irontower.gcbuser.mvp.contract.ChooseStartPositionContract.View;
import com.irontower.gcbuser.mvp.presenter.ChooseStartPositionPresenter;
import com.irontower.gcbuser.mvp.ui.adapter.AddressAdapter;
import com.irontower.gcbuser.mvp.ui.widget.MyDialog;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class ChooseStartPositionActivity extends
    BaseActivity<ChooseStartPositionPresenter> implements View {


  @BindView(R.id.tvCity)
  TextView tvCity;
  @BindView(R.id.tvStartPosition)
  TextView tvStartPosition;


  @BindView(R.id.bmapView)
  MapView mMapView;
  private BaiduMap mBaiduMap;
  private String returnAddress, returnCity;
  private LatLng returnLatLng;
  private MyDialog startDialog;
  private PoiSearch startPoiSearch;
  private Marker startMarker;
  private boolean needGeo = false;

  private GeoCoder mGeocodeStartSearch;
  private boolean isFirstLoc = true;


  public LocationClient mLocationClient;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerChooseStartPositionComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .chooseStartPositionModule(new ChooseStartPositionModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_choose_start_position; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    returnAddress = getIntent().getStringExtra("startPosition");
    returnLatLng = getIntent().getParcelableExtra("latLng");
    returnCity = getIntent().getStringExtra("city");
    tvCity.setText(getIntent().getStringExtra("city"));

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
    tvStartPosition.setText(returnAddress);
    mMapView.showZoomControls(false);
    mBaiduMap = mMapView.getMap();
    mBaiduMap.setMyLocationEnabled(true);
  }


  @Override
  public void showLoading() {

  }

  @Override
  public void hideLoading() {

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


  @OnClick({R.id.layCity, R.id.layCancel, R.id.tvSearch, R.id.tvConfirm})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layCity:
        chooseCity();

        break;
      case R.id.layCancel:
        finish();
        break;
      case R.id.tvSearch:
        if (startDialog != null) {
          startDialog.show();
          TextView textView = startDialog.findViewById(R.id.tvCity);
          textView.setText(tvCity.getText().toString());
        } else {
          startDialog = new MyDialog(this);
          startDialog.show();
          LinearLayout layCancel = startDialog.findViewById(R.id.layCancel);

          RecyclerView recyclerView = startDialog.findViewById(R.id.recyclerView);
          LayoutManager layoutManager = new LinearLayoutManager(getActivity());
          LinearLayout layCity = startDialog.findViewById(R.id.layCity);

          TextView textView = startDialog.findViewById(R.id.tvCity);
          TextView tvCancel = startDialog.findViewById(R.id.tvCancel);
          textView.setText(tvCity.getText().toString());
          layCity.setOnClickListener(view13 -> {
            Intent intent = new Intent();
            intent.putExtra("type", EventBusTags.STARTCITY3);
            intent.putExtra("city", textView.getText().toString());
            intent.setClass(getActivity(), CityPickerActivity.class);
            startActivity(intent);
          });
          AppCompatEditText etSearch = startDialog.findViewById(R.id.etSearch);
          etSearch.setHint(ArmsUtils.getString(getActivity(), R.string.where_start));
          layCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
              if ("确定".equals(tvCancel.getText().toString())) {

                Map<String, Object> map = new HashMap<>();
                map.put("address", etSearch.getText().toString());
                map.put("latLng", null);
                map.put("city", textView.getText().toString());
                EventBus.getDefault().post(map, EventBusTags.STARTCITY4);
                finish();

              }
              startDialog.dismiss();
            }
          });

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
              startDialog.dismiss();
              PoiInfo poiInfo = addressAdapter.getData().get(position);
              Map<String, Object> map = new HashMap<>();
              map.put("address", poiInfo.name);
              map.put("latLng", poiInfo.location);
              map.put("city", poiInfo.city);
              EventBus.getDefault().post(map, EventBusTags.STARTCITY4);
              finish();


            }
          });

          recyclerView.setAdapter(addressAdapter);

          startDialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
              etSearch.setText("");
              addressAdapter.setNewData(new ArrayList<>());
            }
          });

          startPoiSearch = PoiSearch.newInstance();
          startPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
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
              startPoiSearch.searchInCity((new PoiCitySearchOption())
                  .city(textView.getText().toString())
                  .keyword(charSequence.toString())
                  .isReturnAddr(true));


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
          });


        }

        break;
      case R.id.tvConfirm:
        confirm();

        break;
      default:

    }
  }

  private void confirm() {

    Map<String, Object> map = new HashMap<>();
    map.put("address", returnAddress);
    map.put("latLng", returnLatLng);
    map.put("city", returnCity);
    EventBus.getDefault().post(map, EventBusTags.STARTCITY4);
    finish();
  }

  private void chooseCity() {
    Intent intent = new Intent();
    intent.putExtra("type", EventBusTags.STARTCITY2);
    intent.putExtra("city", tvCity.getText().toString());
    intent.setClass(getActivity(), CityPickerActivity.class);
    startActivity(intent);
  }

  @Override
  public Activity getActivity() {
    return this;
  }

  @Subscriber(tag = EventBusTags.STARTCITY2, mode = ThreadMode.MAIN)
  public void startCity(String city) {
    tvCity.setText(city);


  }

  @Subscriber(tag = EventBusTags.STARTCITY3, mode = ThreadMode.MAIN)
  public void startCity2(String city) {
    TextView textView = startDialog.findViewById(R.id.tvCity);
    textView.setText(city);


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

    startDialog = null;

    if (startPoiSearch != null) {
      startPoiSearch.destroy();
    }

  }

  public class MyLocationListener implements BDLocationListener {

    @Override
    public void onReceiveLocation(BDLocation location) {

      if (location == null || mMapView == null) {
        return;
      }

      MyLocationData locData = new Builder()
          .accuracy(location.getRadius())
          // 此处设置开发者获取到的方向信息，顺时针0-360
          .direction(0).latitude(location.getLatitude())
          .longitude(location.getLongitude()).build();
      mBaiduMap.setMyLocationData(locData);
      if (isFirstLoc) {
        isFirstLoc = false;

        LatLng ll;
        if (returnLatLng == null) {
          ll = new LatLng(location.getLatitude(),
              location.getLongitude());
        } else {
          ll = returnLatLng;
        }
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        if (returnLatLng != null) {
          BitmapDescriptor bitmap = BitmapDescriptorFactory
              .fromResource(R.mipmap.start);

          OverlayOptions option = new MarkerOptions()
              .position(ll)
              .icon(bitmap);
          startMarker = (Marker) mBaiduMap.addOverlay(option);
        }

        mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {
          @Override
          public void onMapStatusChangeStart(MapStatus mapStatus) {
            tvStartPosition.setText("正在获取出发位置");


          }

          @Override
          public void onMapStatusChangeStart(MapStatus mapStatus, int i) {
          }

          @Override
          public void onMapStatusChange(MapStatus mapStatus) {
            if (returnLatLng != null) {
              if (startMarker != null) {
                startMarker.setPosition(mapStatus.target);

              } else {
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.start);

                OverlayOptions option = new MarkerOptions()
                    .position(mapStatus.target)
                    .icon(bitmap);
                startMarker = (Marker) mBaiduMap.addOverlay(option);
              }
            }

          }

          @Override
          public void onMapStatusChangeFinish(MapStatus mapStatus) {
            if (needGeo) {
              mGeocodeStartSearch
                  .reverseGeoCode(new ReverseGeoCodeOption().location(mapStatus.target));

            } else {
              tvStartPosition.setText(returnAddress);
              needGeo = true;
            }

          }

        });


      }
    }


  }


  public class MyGeoCoderResultListener implements OnGetGeoCoderResultListener {

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
      if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
        //没有检索到结果
        ArmsUtils.makeText(getActivity(), "没有查到地址");

      }

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
      if (reverseGeoCodeResult == null
          || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
        ArmsUtils.makeText(getActivity(), "没有查到地址");
        return;
      }
      if (reverseGeoCodeResult.getPoiList().size() == 0) {
        tvStartPosition.setText(reverseGeoCodeResult.getAddress());


      } else {

        tvStartPosition.setText(reverseGeoCodeResult.getPoiList().get(0).name);

      }

      tvCity.setText(reverseGeoCodeResult.getAddressDetail().city);
      returnLatLng = reverseGeoCodeResult.getLocation();
      returnAddress = tvStartPosition.getText().toString();
      returnCity = reverseGeoCodeResult.getAddressDetail().city;

    }
  }

}
