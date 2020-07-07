package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.di.component.DaggerCarPositionMapComponent;
import com.irontower.gcbuser.mvp.contract.CarPositionMapContract.View;
import com.irontower.gcbuser.mvp.model.entity.MultipleCar;
import com.irontower.gcbuser.mvp.presenter.CarPositionMapPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.text.NumberFormat;
import java.util.Map;
import javax.inject.Inject;


/**
 * ================================================ Description:
 * <p>
 * Created by MVPArmsTemplate on 06/15/2019 14:26
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class CarPositionMapActivity extends BaseActivity<CarPositionMapPresenter> implements
    View {

  @BindView(R.id.mapView)
  MapView mMapView;
  private BaiduMap mBaiduMap;
  private Marker mMoveMarker;
  private MultipleCar multipleCar;
  private CoordinateConverter converter = new CoordinateConverter();
  public LocationClient mLocationClient;

  @Inject
  MaterialDialog materialDialog;
  private InfoWindow infoWindow;
  private RelativeLayout linearLayout;
  private LatLng location;
  private TextView tvStatus;
  private BitmapDescriptor bitmap = BitmapDescriptorFactory
      .fromResource(R.mipmap.car_move);
  private NumberFormat numberFormat = NumberFormat.getNumberInstance();

  @Override
  public void setupActivityComponent(@NonNull AppComponent appComponent) {
    DaggerCarPositionMapComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .view(this)
        .build()
        .inject(this);
  }

  @Override
  public int initView(@Nullable Bundle savedInstanceState) {
    return R.layout.activity_car_position_map; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(@Nullable Bundle savedInstanceState) {
    converter.from(CoordType.GPS);
    numberFormat.setMaximumFractionDigits(2);
    multipleCar = getIntent().getParcelableExtra("car");
    mMapView.onCreate(this, savedInstanceState);
    mBaiduMap = mMapView.getMap();
    mMapView.showZoomControls(false);
    linearLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.car_info, null);
    TextView tvCarNo = linearLayout.findViewById(R.id.tvCarNo);
    tvStatus = linearLayout.findViewById(R.id.tvStatus);
    tvCarNo.setText(multipleCar.getCarNo());
    mLocationClient = new LocationClient(getApplicationContext());
    MyLocationListener myLocationListener = new MyLocationListener();
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
    mPresenter.getData(multipleCar.getCarId());
  }

  @Override
  public void showLoading() {
    materialDialog.show();
  }

  @Override
  public void hideLoading() {
    materialDialog.dismiss();
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
  protected void onResume() {
    super.onResume();
    mMapView.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
    mMapView.onPause();
  }


  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    mMapView.onSaveInstanceState(outState);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mLocationClient != null) {
      mLocationClient.stop();
    }
    if (mMapView != null) {
      mMapView.onDestroy();
    }
    if (mBaiduMap != null) {
      mBaiduMap.clear();
    }

  }

  @Override
  public Activity getActivity() {
    return this;
  }


  @OnClick({R.id.ivLocation, R.id.tvLocation, R.id.tvTrace})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.ivLocation:
        if (location != null) {
          MapStatus.Builder builder = new MapStatus.Builder();
          builder.target(location).zoom(18.0f);
          mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }

        break;
      case R.id.tvLocation:
        mPresenter.getData(multipleCar.getCarId());
        break;
      case R.id.tvTrace:
        launchActivity(
            new Intent(getActivity(), TraceViewActivity.class).putExtra("car", multipleCar));
        break;
      default:
        break;
    }
  }

  public class MyLocationListener implements BDLocationListener {

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {

      if (bdLocation == null || mMapView == null) {
        return;
      }

      location = new LatLng(bdLocation.getLatitude(),
          bdLocation.getLongitude());
    }


  }

  @Override
  public void init(Map<String, Object> obj) {
    Map<String, Object> map = (Map<String, Object>) obj.get("pos");

    LatLng latLng = convert(new LatLng((Double) map.get("lat"), (Double) map.get("lng")));
    int status = ((int) (double) (Double) map.get("dAttr")) & 1;
    tvStatus.setText(
        status != 1 ? "离线"
            : numberFormat.format((Double) map.get("gSpeed") / 1000) + "KM/h");
    if (mMoveMarker == null) {

      OverlayOptions option = new MarkerOptions()
          .position(latLng)
          .icon(bitmap);
      mMoveMarker = (Marker) mBaiduMap.addOverlay(option);

      infoWindow = new InfoWindow(linearLayout, latLng, -100);
      mBaiduMap.showInfoWindow(infoWindow);
    } else {

      mMoveMarker.setPosition(latLng);

      infoWindow = new InfoWindow(linearLayout, latLng, -100);
      mBaiduMap.showInfoWindow(infoWindow);

    }
    mMoveMarker.setRotate(-1*Float.parseFloat(map.get("dir").toString()));
    MapStatus.Builder builder = new MapStatus.Builder();
    builder.target(latLng).zoom(18.0f);
    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

  }

  private LatLng convert(LatLng latLng) {
    converter.coord(latLng);

    return converter.convert();
  }
}
