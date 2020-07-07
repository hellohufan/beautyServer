package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.MyLocationData.Builder;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.utils.DateUtil;
import com.irontower.gcbuser.di.component.DaggerDriverPositionComponent;
import com.irontower.gcbuser.di.module.DriverPositionModule;
import com.irontower.gcbuser.mvp.contract.DriverPositionContract.View;
import com.irontower.gcbuser.mvp.presenter.DriverPositionPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import java.util.Date;
import java.util.Map;
import javax.inject.Inject;


public class DriverPositionActivity extends BaseActivity<DriverPositionPresenter> implements
    View {

  @BindView(R.id.tvDriverName)
  TextView tvDriverName;
  @BindView(R.id.tvDriverNo)
  TextView tvDriverNo;
  private Integer carId;
  private String phone;
  @Inject
  RxPermissions rxPermissions;
  @BindView(R.id.bmapView)
  MapView mMapView;
  private BaiduMap mBaiduMap;
  public LocationClient mLocationClient;
  private Marker driverMarker;
  private LatLng startLatLng, driverLatlng;
  private InfoWindow infoWindow;

  private TextView textView;
  private CoordinateConverter converter = new CoordinateConverter();

  @Override
  public void setupActivityComponent(@NonNull AppComponent appComponent) {
    DaggerDriverPositionComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .driverPositionModule(new DriverPositionModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(@Nullable Bundle savedInstanceState) {
    return R.layout.activity_driver_position; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(@Nullable Bundle savedInstanceState) {
    converter.from(CoordType.GPS);
    MyLocationListener myLocationListener = new MyLocationListener();
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
    mMapView.showZoomControls(true);
    mBaiduMap = mMapView.getMap();
    mBaiduMap.setMyLocationEnabled(true);

    carId = getIntent().getIntExtra("carId", 0);
    tvDriverName.setText(getIntent().getStringExtra("driverName"));
    tvDriverNo.setText(getIntent().getStringExtra("carNo"));
    phone = getIntent().getStringExtra("driverPhone");
    mPresenter.getData(carId);

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

  @Override
  public Activity getActivity() {
    return this;
  }

  @Override
  public void init(Map<String, Object> data) {

    if (data.get("pos") != null) {
      Map<String, Object> posMap = (Map<String, Object>) data.get("pos");
      driverLatlng = convert(new LatLng(Double.parseDouble(String.valueOf(posMap.get("lat"))),
          Double.parseDouble(String.valueOf(posMap.get("lng")))));
      long upTime = (long) (double) posMap.get("upTime");
      if (driverMarker != null) {
        driverMarker.setPosition(driverLatlng);
        mBaiduMap.hideInfoWindow();
        infoWindow = null;
        textView.setText("更新于" + DateUtil.format("HH:mm:ss", new Date(upTime)));
        infoWindow = new InfoWindow(textView, driverLatlng, -85);
        mBaiduMap.showInfoWindow(infoWindow);

      } else {
        BitmapDescriptor bitmap = BitmapDescriptorFactory
            .fromResource(R.mipmap.car_position);

        OverlayOptions option = new MarkerOptions()
            .position(driverLatlng)
            .icon(bitmap);
        textView = new TextView(getApplicationContext());
        textView.setBackgroundResource(R.mipmap.car_move_bg);
        textView.setTextColor(Color.BLACK);
        textView.setText("更新于" + DateUtil.format("HH:mm:ss", new Date(upTime)));
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(5, 0, 5, 10);
        textView.setTextSize(15);

        infoWindow = new InfoWindow(textView, driverLatlng, -85);
        driverMarker = (Marker) mBaiduMap.addOverlay(option);
        mBaiduMap.showInfoWindow(infoWindow);


      }
      if (startLatLng != null) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(driverLatlng);
        builder.include(startLatLng);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory
            .newLatLngBounds(builder.build()));

      } else {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(driverLatlng).zoom(18.0f);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
      }


    }
  }


  @OnClick(R.id.ivCall)
  public void onViewClicked() {
    mPresenter.call(phone);
  }

  @Override
  public RxPermissions getRxpermission() {
    return rxPermissions;
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
      startLatLng = new LatLng(location.getLatitude(),
          location.getLongitude());
      if (driverLatlng != null) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(driverLatlng);
        builder.include(startLatLng);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory
            .newLatLngBounds(builder.build()));

      } else {

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(startLatLng).zoom(18.0f);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
      }


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

    mLocationClient.stop();
    // 关闭定位图层
    mBaiduMap.setMyLocationEnabled(false);
    mMapView.onDestroy();
    mMapView = null;
    super.onDestroy();

  }

  private LatLng convert(LatLng latLng) {
    converter.coord(latLng);

    return converter.convert();
  }

}
