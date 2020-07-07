package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.utils.TrackPlayer;
import com.irontower.gcbuser.app.utils.TrackPlayer.OnPlayStateChangedListener;
import com.irontower.gcbuser.app.utils.TrackPlayer.OnProgressChangedListener;
import com.irontower.gcbuser.di.component.DaggerTracePayBackComponent;
import com.irontower.gcbuser.mvp.contract.TracePayBackContract.View;
import com.irontower.gcbuser.mvp.model.entity.CarSearch;
import com.irontower.gcbuser.mvp.model.entity.Track;
import com.irontower.gcbuser.mvp.presenter.TracePayBackPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * ================================================ Description:
 * <p>
 * Created by MVPArmsTemplate on 06/15/2019 17:47
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class TracePayBackActivity extends BaseActivity<TracePayBackPresenter> implements
    View, OnProgressChangedListener,
    OnPlayStateChangedListener {

  @BindView(R.id.mapView)
  MapView mMapView;
  private BaiduMap mBaiduMap;
  @BindView(R.id.tvCarNo)
  TextView tvCarNo;
  @BindView(R.id.tvSpeed)
  TextView tvSpeed;
  @BindView(R.id.tvMeter)
  TextView tvMeter;
  @BindView(R.id.tvLocationTime)
  TextView tvLocationTime;
  @BindView(R.id.tvTime)
  TextView tvTime;
  @BindView(R.id.seekbar)
  SeekBar seekbar;
  TrackPlayer player;
  @BindView(R.id.ivPlay)
  ImageView ivPlay;
  private Polyline mPolyline;
  private Marker mMoveMarker;
  private ArrayList<Track> list = new ArrayList<>();
  private List<LatLng> latLngs = new ArrayList<>();
  private CarSearch carSearch;
  private NumberFormat numberFormat = NumberFormat.getNumberInstance();
  private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
  private CoordinateConverter converter = new CoordinateConverter();

  @Override
  public void setupActivityComponent(@NonNull AppComponent appComponent) {
    DaggerTracePayBackComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .view(this)
        .build()
        .inject(this);
  }

  @Override
  public int initView(@Nullable Bundle savedInstanceState) {
    return R.layout.activity_trace_pay_back; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(@Nullable Bundle savedInstanceState) {

    mMapView.onCreate(this, savedInstanceState);
    mBaiduMap = mMapView.getMap();
    mMapView.showZoomControls(false);

    numberFormat.setMaximumFractionDigits(2);
    carSearch = getIntent().getParcelableExtra("carSearch");
    tvCarNo.setText(carSearch.getCarNo());

    list = new ArrayList<>();
    list.addAll(TraceViewActivity.tracks);
    tvMeter.setText(list.get(0).getgMile() + "KM");
    tvSpeed.setText(numberFormat.format(list.get(0).getgSpeed() / 1000) + "KM/H");
    tvLocationTime.setText(timeStamp2Date(list.get(0).getUpTime()));

    tvTime.setText(carSearch.getBeginTime() + " ~ " + carSearch.getEndTime());
    converter.from(CoordType.GPS);
    init();
  }

  private void init() {
    for (int index = 0; index < list.size(); index++) {
      latLngs.add(convert(new LatLng(Double.parseDouble(list.get(index).getLat()),
          Double.parseDouble(list.get(index).getLng()))));
    }

    PolylineOptions polylineOptions = new PolylineOptions().points(latLngs).width(10)
        .color(Color.parseColor("#f6683c"));

    mPolyline = (Polyline) mBaiduMap.addOverlay(polylineOptions);
    OverlayOptions markerOptions;
    markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 0.5f)
        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_move)).position(latLngs.get(0))
        .rotate((float) getAngle(0));
    ;
    mMoveMarker = (Marker) mBaiduMap.addOverlay(markerOptions);
    zoomToSpan();
    player = new TrackPlayer(mBaiduMap, latLngs, seekbar, mMoveMarker, this, this);

  }

  @Override
  public void showLoading() {

  }

  /**
   * 根据点获取图标转的角度
   */
  private double getAngle(int startIndex) {
    if ((startIndex + 1) >= mPolyline.getPoints().size()) {
      throw new RuntimeException("index out of bonds");
    }
    LatLng startPoint = mPolyline.getPoints().get(startIndex);
    LatLng endPoint = mPolyline.getPoints().get(startIndex + 1);
    return getAngle(startPoint, endPoint);
  }

  /**
   * 根据两点算取图标转的角度
   */
  private double getAngle(LatLng fromPoint, LatLng toPoint) {
    double slope = getSlope(fromPoint, toPoint);
    if (slope == Double.MAX_VALUE) {
      if (toPoint.latitude > fromPoint.latitude) {
        return 0;
      } else {
        return 180;
      }
    }
    float deltAngle = 0;
    if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
      deltAngle = 180;
    }
    double radio = Math.atan(slope);
    return 180 * (radio / Math.PI) + deltAngle - 90;
  }

  /**
   * 根据点和斜率算取截距
   */
  private double getInterception(double slope, LatLng point) {

    return point.latitude - slope * point.longitude;
  }

  /**
   * 算斜率
   */
  private double getSlope(LatLng fromPoint, LatLng toPoint) {
    if (toPoint.longitude == fromPoint.longitude) {
      return Double.MAX_VALUE;
    }
    return ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude
        - fromPoint.longitude));
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
  public void onPlayStateChanged(int state) {
    if (ivPlay != null) {
      ivPlay.setImageResource(state == TrackPlayer.STATE_PLAY ? R.mipmap.stop : R.mipmap.play);
    }
  }

  @Override
  public void onProgressChanged(int progress) {
    Track his = list.get(progress);
    tvSpeed.setText(numberFormat.format(his.getgSpeed() / 1000) + "KM/H");
    tvLocationTime.setText(timeStamp2Date(his.getUpTime()));

  }

  @OnClick({R.id.ivPlay, R.id.ivStop})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.ivPlay:
        player.toggle();
        break;
      case R.id.ivStop:
        player.stop();
        break;
      default:
        break;
    }
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
    player.destroy();
    super.onDestroy();

    if (mMapView != null) {
      mMapView.onDestroy();
    }
    if (mBaiduMap != null) {
      mBaiduMap.clear();
    }

  }

  private LatLng convert(LatLng latLng) {
    converter.coord(latLng);

    return converter.convert();
  }

  public void zoomToSpan() {
    if (latLngs.size() > 0) {
      LatLngBounds.Builder builder = new LatLngBounds.Builder();
      for (LatLng latLng : latLngs) {
        builder.include(latLng);
      }
      mBaiduMap.setMapStatus(MapStatusUpdateFactory
          .newLatLngBounds(builder.build()));
    }
  }

  public String timeStamp2Date(String timestampString) {
    Long timestamp = Long.parseLong(timestampString) * 1000;
    String date = format.format(new java.util.Date(timestamp));
    return date;
  }
}
