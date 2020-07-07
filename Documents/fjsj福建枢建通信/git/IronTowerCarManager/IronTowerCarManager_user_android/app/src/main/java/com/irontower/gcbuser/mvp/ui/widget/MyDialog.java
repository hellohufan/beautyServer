package com.irontower.gcbuser.mvp.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;
import com.irontower.gcbuser.R;

/**
 * Created by jianghaifeng on 2017/12/11.
 */

public class MyDialog extends Dialog {

  Context mContext;

  public MyDialog(Context context) {
    super(context, R.style.MyDialog);
    this.mContext = context;


  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.dialog_choose_position);
  }

  @Override
  public void show() {
    super.show();
    /**
     * 设置宽度全屏，要设置在show的后面
     */
    LayoutParams layoutParams = getWindow().getAttributes();
    layoutParams.gravity = Gravity.BOTTOM;
    layoutParams.width = LayoutParams.MATCH_PARENT;
    layoutParams.height = LayoutParams.MATCH_PARENT;

    getWindow().getDecorView().setPadding(0, 0, 0, 0);

    getWindow().setAttributes(layoutParams);

  }

}