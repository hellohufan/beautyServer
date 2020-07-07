package com.irontower.gcbuser.app.utils;

import android.os.Handler;
import android.os.Looper;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TrackPlayer implements OnSeekBarChangeListener {

  private BaiduMap aMap;
  private List<LatLng> points;
  private SeekBar seekBar;
  private Marker marker;
//	private Queue<LatLng> animationQueue;

  private AtomicInteger progress;
  private Handler handler;

  private static final double DISTANCE = 0.0005;

  public static final int STATE_PLAY = 0;
  private static final int STATE_STOP = 1;
  private static final int STATE_PAUSE = 2;
  private MapStatus.Builder builder = new MapStatus.Builder();

  private OnProgressChangedListener mChangedListener;
  private OnPlayStateChangedListener mPlayStateChangedListener;

  private int state;

  public TrackPlayer(BaiduMap aMap, List<LatLng> points, SeekBar seekBar, Marker marker,
      OnPlayStateChangedListener stateChangedListener, OnProgressChangedListener listener) {
    this.aMap = aMap;
    this.points = points;
    this.seekBar = seekBar;
    this.marker = marker;
    state = STATE_STOP;
    progress = new AtomicInteger(0);
    handler = new Handler(Looper.getMainLooper());
    seekBar.setMax(points.size() - 1);
    seekBar.setOnSeekBarChangeListener(this);
    mPlayStateChangedListener = stateChangedListener;
    mChangedListener = listener;
//    builder.zoom(19.0f);
//		animationQueue = new LinkedBlockingDeque<LatLng>();
    onProgressChanged(seekBar, 0, true);
  }

  private void play() {
    if (state != STATE_PLAY) {
      state = STATE_PLAY;
      handler.post(r);
      if (mPlayStateChangedListener != null) {
        mPlayStateChangedListener.onPlayStateChanged(STATE_PLAY);
      }
    }

  }

  private void pause() {
    state = STATE_PAUSE;
    handler.removeCallbacks(r);
    if (mPlayStateChangedListener != null) {
      mPlayStateChangedListener.onPlayStateChanged(STATE_PAUSE);
    }
  }

  public void toggle() {
    if (state != STATE_PLAY) {
      play();
    } else {
      pause();
    }
  }

  private Runnable r = new Runnable() {

    @Override
    public void run() {
      if (state != STATE_PLAY) {
        return;
      }
      int progress = seekBar.getProgress();
      if (progress < points.size() - 1) {
        progress++;
        seekBar.setProgress(progress);

        handler.postDelayed(r, 80);
      } else {
        stop();
      }

    }
  };

  public void stop() {
    handler.removeCallbacks(r);
    state = STATE_STOP;
//		animationQueue.clear();
    progress.set(0);
    seekBar.setProgress(0);

    if (mPlayStateChangedListener != null) {
      mPlayStateChangedListener.onPlayStateChanged(STATE_STOP);
    }
  }

  public void destroy() {
    handler.removeCallbacks(r);
//		animationQueue.clear();

  }

  private void animate(LatLng fromPoint, LatLng toPoint) {
    double slope = getSlope(fromPoint, toPoint);
    boolean isReverse = (fromPoint.latitude > toPoint.latitude);
    double intercept = getInterception(slope, fromPoint);
    double xMoveDistance = isReverse ? getXMoveDistance(slope) : (-1 * getXMoveDistance(slope));
//		animationQueue.clear();
    for (double j = fromPoint.latitude; j > toPoint.latitude == isReverse; j = j - xMoveDistance) {
      LatLng latLng;
      if (slope != Double.MAX_VALUE) {
        latLng = new LatLng(j, (j - intercept) / slope);
      } else {
        latLng = new LatLng(j, fromPoint.longitude);
      }
//			animationQueue.add(latLng);
    }
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
   * 算斜率
   */
  private double getSlope(LatLng fromPoint, LatLng toPoint) {
    if (toPoint.longitude == fromPoint.longitude) {
      return Double.MAX_VALUE;
    }
    return ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));

  }

  private double getInterception(double slope, LatLng point) {

    return point.latitude - slope * point.longitude;
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

  @Override
  public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    LatLng latLng = points.get(progress);
    marker.setPosition(latLng);
//    if (aMap.getMapScreenMarkers() == null || aMap.getMapScreenMarkers().isEmpty()) {
//      aMap.animateCamera(
//          CameraUpdateFactory.newLatLngZoom(marker.getPosition(), aMap.getCameraPosition().zoom));
//    }
    mChangedListener.onProgressChanged(progress);
    if (progress < points.size() - 1) {
      marker.setRotate((float) getAngle(points.get(progress), points.get(progress + 1)));
    }
//    builder.target(latLng);
//    aMap.animateMapStatus(
//        MapStatusUpdateFactory.newMapStatus(builder.build()));
  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {
    handler.removeCallbacks(r);
  }

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {
    if (state == STATE_PLAY) {
      handler.post(r);
    }
  }

  public interface OnPlayStateChangedListener {

    void onPlayStateChanged(int state);
  }

  public interface OnProgressChangedListener {

    void onProgressChanged(int progress);
  }

}
