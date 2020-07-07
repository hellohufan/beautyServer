package com.irontower.gcbdriver.app;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import cn.jpush.android.api.JPushInterface;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.google.gson.Gson;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.ui.activity.MessageActivity;
import com.irontower.gcbdriver.mvp.ui.activity.OrderDetailActivity;
import com.irontower.gcbdriver.mvp.ui.activity.OrderRecordActivity;
import java.util.Map;

/**
 * 极光推送接收处理
 */


/**
 * 极光推送接收处理
 */
public class JiGuangReceiver extends BroadcastReceiver {

  private static final String PUSH_CHANNEL_ID = "hasimtech_gcb_driver_id";
  private static final String PUSH_CHANNEL_NAME = "hasimtech_gcb_driver_name";


  @Override
  public void onReceive(Context context, Intent intent)

  {
    Bundle bundle = intent.getExtras();

    if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
      Gson gson = new Gson();
      if (bundle != null) {
        if (bundle.containsKey(JPushInterface.EXTRA_EXTRA)) {
          if (!StringUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
            if (SPUtils.getInstance().getBoolean("isLogin")) {
              Map<String, String> map = gson
                  .fromJson(bundle.getString(JPushInterface.EXTRA_EXTRA), Map.class);
              NotificationManager notificationManager = (NotificationManager) context
                  .getSystemService(Context.NOTIFICATION_SERVICE);

              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(PUSH_CHANNEL_ID,
                    PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                if (notificationManager != null) {
                  notificationManager.createNotificationChannel(channel);
                }
              }

              NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                  PUSH_CHANNEL_ID)
                  .setContentText(bundle.getString(JPushInterface.EXTRA_MESSAGE))
                  .setContentTitle("铁塔车管家司机端")
                  .setSmallIcon(R.mipmap.ic_launcher)
                  .setTicker("新消息")
                  .setAutoCancel(true)
                  .setWhen(System.currentTimeMillis());//设置时间，设置为系统当前的时间

              Intent jumpIntent = new Intent();
              if (map.containsKey("page")) {
                switch (map.get("page")) {
                  case "20000":
                    jumpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    jumpIntent.putExtra("orderCarNo", map.get("order"));
                    jumpIntent.setClass(context, OrderDetailActivity.class);
                    break;
                  case "20001":
                    jumpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    jumpIntent.setClass(context, OrderRecordActivity.class);
                    break;
                  case "10004":
                    jumpIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    jumpIntent.setClass(context, MessageActivity.class);
                    break;

                  default:
                }

              }

              PendingIntent pendingIntent = PendingIntent
                  .getActivity(context, 0, jumpIntent, PendingIntent.FLAG_UPDATE_CURRENT);
              builder.setContentIntent(pendingIntent);
              Notification notification = builder.build();
              if (SPUtils.getInstance().getBoolean("sound")) {
                if (map.containsKey("sound")) {
                  if (map.get("sound").equals("default")) {
                    notification.defaults = Notification.DEFAULT_ALL;
                  }

                  AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                  if (am != null) {
                    if (!am.isMusicActive()) {
                      if (SPUtils.getInstance().getBoolean("sound")) {
                        switch (map.get("sound")) {

                          case "dispatching.wav":
                            MediaPlayer mp2 = MediaPlayer
                                .create(context, R.raw.dispatch);//重新设置要播放的音频
                            mp2.start();//开始播放
                            break;
                          case "cancel.wav":
                            MediaPlayer mp3 = MediaPlayer.create(context, R.raw.cancel);//重新设置要播放的音频
                            mp3.start();//开始播放
                            break;

                          default:

                        }
                      }
                    }
                  }

                }
              }
              if (notificationManager != null) {
                notificationManager.notify((int) System.currentTimeMillis(), notification);
              }
            }
          }

        }
      }


    }


  }

}
