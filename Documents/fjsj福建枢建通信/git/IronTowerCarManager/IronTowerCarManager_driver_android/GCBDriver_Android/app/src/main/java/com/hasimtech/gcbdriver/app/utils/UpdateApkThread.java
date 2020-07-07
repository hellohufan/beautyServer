package com.irontower.gcbdriver.app.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import com.irontower.gcbdriver.R;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>
 * Copyright: (c) 2016,　福建简悦信息科技有限公司 All rights reserved。
 * </p>
 * <p>
 * 文件名称：UpdateApkThread.java
 * </p>
 * <p>
 * 描 述：更新版本接口
 * </p>
 *
 * @author jhf
 * @version 1.0 ******************************************************************************* <p>
 *          修改记录 </p> <p> 修改时间： </p> <p> 修 改 人： </p> <p> 修改内容： </p>
 * @date 2016-5-20
 */
public class UpdateApkThread extends Thread {
  private String downloadUrl;
  private File saveFile;
  private String fileName;
  private Context context;
  int progress = 0;
  private Notification notification ;
  private NotificationManager notificationManager;// 状态栏通知管理类
  private Timer timer;// 定时器，用于更新下载进度
  private TimerTask task;// 定时器执行的任务
  NotificationCompat.Builder mBuilder;
  private final int notificationID = 1;// 通知的id
  private final int updateProgress = 1;// 更新状态栏的下载进度
  private final int downloadSuccess = 2;// 下载成功
  private final int downloadError = 3;// 下载失败
  private DownLoadUtil downLoadUtil;

  public UpdateApkThread(String downloadUrl, String fileLocation, String fileName, Context context) {
    this.downloadUrl = downloadUrl;
    this.saveFile = new File(fileLocation);
    this.context = context;
    this.fileName = fileName;
  }

  @Override
  public void run() {
    super.run();
    try {
      initNofication();
      handlerTask();
      downLoadUtil = new DownLoadUtil();
      int downSize = downLoadUtil.downloadUpdateFile(downloadUrl, saveFile, fileName, callback);
      if (downSize == downLoadUtil.getRealSize() && downSize != 0) {
        handler.sendEmptyMessage(downloadSuccess);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   *
   * @author wangqian@iliveasia.com
   * @Description: 初始化通知栏
   */
  private void initNofication() {
    notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    mBuilder = new NotificationCompat.Builder(context);
    notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).build();
    mBuilder.setWhen(System.currentTimeMillis()).setSmallIcon(R.mipmap.ic_launcher)
        .setTicker("开始下载。。。").setContentTitle("公务约租车司机端下载中")
        .setContentText( "已下载" + progress + "%").setProgress(100, progress, false);
    notification.defaults=Notification.DEFAULT_SOUND;

    notificationManager.notify(notificationID,notification);


  }

  /**
   *
   * @author wangqian@iliveasia.com
   * @Description: 定时通知handler去显示下载百分比
   */
  private void handlerTask() {
    timer = new Timer();
    task = new TimerTask() {

      @Override
      public void run() {
        handler.sendEmptyMessage(updateProgress);
      }
    };
    timer.schedule(task, 500, 500);
  }

  Handler handler = new Handler() {

    @Override
    public void handleMessage(Message msg) {
      if (msg.what == updateProgress) {// 更新下载进度
        int fileSize = downLoadUtil.getRealSize();
        int totalReadSize = downLoadUtil.getTotalSize();
        if (totalReadSize > 0) {
          float size = (float) totalReadSize * 100 / (float) fileSize;
          DecimalFormat format = new DecimalFormat("0.00");
          String str = format.format(size);
          mBuilder.setProgress(100,(int)size, false);
          mBuilder.setContentText( "已下载" + str + "%");
          notificationManager.notify(notificationID,mBuilder.build());
        }
      } else if (msg.what == downloadSuccess) {// 下载完成
        mBuilder.setContentText( "下载完成,点击安装.");
        mBuilder.setContentTitle("公务约租车司机端");
        mBuilder.setTicker("下载完成.");
        mBuilder.setProgress(0, 0, false);
        notificationManager.notify(notificationID, notification);
        if (timer != null && task != null) {
          timer.cancel();
          task.cancel();
          timer = null;
          task = null;
        }
        // 安装apk
        File file = new File(saveFile + "/"+fileName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
          Uri apkUri = FileProvider.getUriForFile(context, "com.irontower.gcbdriver.file_provider", file);
          intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
          intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {

          intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        // PendingIntent 通知栏跳转
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setWhen(System.currentTimeMillis());

        notificationManager.notify(notificationID, mBuilder.build());

      } else if (msg.what == downloadError) {// 下载失败
        if (timer != null && task != null) {
          timer.cancel();
          task.cancel();
          timer = null;
          task = null;
        }
        mBuilder.setContentText( "网络异常,请检查网络是否正常.");
        mBuilder.setProgress(0, 0, false);
        mBuilder.setContentTitle("公务约租车司机端");
        mBuilder.setAutoCancel(true);
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder.setTicker("网络异常");
        mBuilder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(), 0));
        notificationManager.notify(notificationID,mBuilder.build());

      }
    }

  };
  /**
   * 下载回调
   */
  DownloadFileCallback callback = new DownloadFileCallback() {

    @Override
    public void downloadError(String msg) {
      handler.sendEmptyMessage(downloadError);
    }
  };

}
