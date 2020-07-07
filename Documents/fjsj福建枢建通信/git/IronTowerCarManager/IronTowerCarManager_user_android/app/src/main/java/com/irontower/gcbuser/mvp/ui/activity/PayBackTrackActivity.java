package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import butterknife.BindView;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
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
import com.irontower.gcbuser.di.component.DaggerPayBackTrackComponent;
import com.irontower.gcbuser.di.module.PayBackTrackModule;
import com.irontower.gcbuser.mvp.contract.PayBackTrackContract.View;
import com.irontower.gcbuser.mvp.model.entity.Track;
import com.irontower.gcbuser.mvp.presenter.PayBackTrackPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.util.ArrayList;
import java.util.List;


public class PayBackTrackActivity extends BaseActivity<PayBackTrackPresenter> implements
    View {

  @BindView(R.id.mapView)
  MapView mMapView;
  private BaiduMap mBaiduMap;
  private Polyline mPolyline;
  private Marker mMoveMarker;
  private Handler mHandler;

  private static final int TIME_INTERVAL = 80;
  private static final double DISTANCE = 0.3;
  private List<LatLng> mLatLngList = new ArrayList<>();
  private CoordinateConverter converter = new CoordinateConverter();


  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerPayBackTrackComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .payBackTrackModule(new PayBackTrackModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_pay_back_track; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {

    converter.from(CoordType.GPS);
    mPresenter.getOrderTrack(getIntent().getStringExtra("orderCarNo"));
    mMapView.onCreate(this, savedInstanceState);
    mBaiduMap = mMapView.getMap();
    mHandler = new Handler(Looper.getMainLooper());


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
  public void initLine(List<Track> data) {
    if (data.size() != 0) {
      Track beginTrack = data.get(0);
      Track endTrack = data.get(data.size() - 1);
      LatLng beginLatLng = new LatLng(Double.parseDouble(beginTrack.getLat()),
          Double.parseDouble(beginTrack.getLng()));
      LatLng endLatLng = new LatLng(Double.parseDouble(endTrack.getLat()),
          Double.parseDouble(endTrack.getLng()));
      BitmapDescriptor bitmap = BitmapDescriptorFactory
          .fromResource(R.mipmap.start);

      OverlayOptions option = new MarkerOptions()
          .position(convert(beginLatLng))
          .icon(bitmap);
      mBaiduMap.addOverlay(option);

      BitmapDescriptor bitmap1 = BitmapDescriptorFactory
          .fromResource(R.mipmap.end);

      OverlayOptions option1 = new MarkerOptions()
          .position(convert(endLatLng))
          .icon(bitmap1);
      mBaiduMap.addOverlay(option1);
      MapStatus.Builder builder = new MapStatus.Builder();
      builder.target(beginLatLng);
      builder.zoom(19.0f);
      mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

      drawPolyLine(data);
      mMapView.showZoomControls(false);

    } else {
      ArmsUtils.makeText(this, "没有可用的轨迹回放点");
    }

  }


  private void drawPolyLine(List<Track> data) {

    for (int index = 0; index < data.size(); index++) {
      mLatLngList.add(convert(new LatLng(Double.parseDouble(data.get(index).getLat()),
          Double.parseDouble(data.get(index).getLng()))));
    }
    zoomToSpan();
    PolylineOptions polylineOptions = new PolylineOptions().points(mLatLngList).width(10)
        .color(Color.parseColor("#ff6f6e"));

    mPolyline = (Polyline) mBaiduMap.addOverlay(polylineOptions);
    OverlayOptions markerOptions;
    markerOptions = new MarkerOptions().flat(true).anchor(0.5f, 0.5f)
        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.car_play)).position(mLatLngList.get(0))
        .rotate((float) getAngle(0));
    ;
    mMoveMarker = (Marker) mBaiduMap.addOverlay(markerOptions);
    moveLooper(mLatLngList);

  }

  public void zoomToSpan() {
    if (mLatLngList.size() > 0) {
      LatLngBounds.Builder builder = new LatLngBounds.Builder();
      for (LatLng latLng : mLatLngList) {
        builder.include(latLng);
      }
      mBaiduMap.setMapStatus(MapStatusUpdateFactory
          .newLatLngBounds(builder.build()));
    }
  }


  public void moveLooper(List<LatLng> latlngs) {
    new Thread() {

      @Override
      public void run() {

        for (int i = 0; i < latlngs.size() - 1; i++) {

          final LatLng startPoint = latlngs.get(i);
          final LatLng endPoint = latlngs.get(i + 1);
          mMoveMarker
              .setPosition(startPoint);

          mHandler.post(new Runnable() {
            @Override
            public void run() {
              // refresh marker's rotate
              if (mMapView == null) {
                return;
              }
              mMoveMarker.setRotate((float) getAngle(startPoint,
                  endPoint));
            }
          });
          double slope = getSlope(startPoint, endPoint);
          // 是不是正向的标示
          boolean isReverse = (startPoint.latitude > endPoint.latitude);

          double intercept = getInterception(slope, startPoint);

          double xMoveDistance = isReverse ? getXMoveDistance(slope) :
              -1 * getXMoveDistance(slope);

          for (double j = startPoint.latitude; !((j > endPoint.latitude) ^ isReverse);
              j = j - xMoveDistance) {
            LatLng latLng = null;
            if (slope == Double.MAX_VALUE) {
              latLng = new LatLng(j, startPoint.longitude);
            } else {
              latLng = new LatLng(j, (j - intercept) / slope);
            }

            final LatLng finalLatLng = latLng;
            mHandler.post(new Runnable() {
              @Override
              public void run() {
                if (mMapView == null) {
                  return;
                }
                mMoveMarker.setPosition(finalLatLng);
              }
            });
            try {
              Thread.sleep(TIME_INTERVAL);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }

        }
      }

    }.start();
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
    if (mMapView != null) {
      mMapView.onDestroy();
    }
    if (mBaiduMap != null) {
      mBaiduMap.clear();
    }

  }

  /**
   * 计算x方向每次移动的距离
   */
  private double getXMoveDistance(double slope) {
    if (slope == Double.MAX_VALUE) {
      return DISTANCE;
    }
    return Math.abs((DISTANCE * slope) / Math.sqrt(1 + slope * slope));
  }

  private LatLng convert(LatLng latLng) {
    converter.coord(latLng);

    return converter.convert();
  }

  @Override
  public Activity getActivity() {
    return this;
  }
}
