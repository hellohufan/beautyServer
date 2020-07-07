package com.irontower.gcbuser.app.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import com.jess.arms.utils.ArmsUtils;
import java.io.File;

public class IntentUtil {

  private static final String authority = "com.irontower.gcbuser.file_provider";

  public static void viewFile(final Context context, final String filePath) {
    String type = getMimeType(filePath);

    if (!TextUtils.isEmpty(type)) {
      /* 设置intent的file与MimeType */
      Intent intent = new Intent();
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      intent.setAction(Intent.ACTION_VIEW);
      if (Build.VERSION.SDK_INT >= 24) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(FileProvider
                .getUriForFile(context, authority, new File(filePath))
            , type);
      } else {
        intent.setDataAndType(Uri.fromFile(new File(filePath)), type);

      }

      context.startActivity(intent);
    } else {
      ArmsUtils.makeText(context, "未知类型的文件");
    }
  }

  private static String getMimeType(String filePath) {
    int dotPosition = filePath.lastIndexOf('.');
    if (dotPosition == -1) {
      return "*/*";
    }

    String ext = filePath.substring(dotPosition + 1, filePath.length()).toLowerCase();
    String mimeType = MimeUtils.extensionToMimeTypeMap.get(ext);
    if (ext.equals("mtz")) {
      mimeType = "application/miui-mtz";
    }

    return mimeType != null ? mimeType : "*/*";
  }

}
