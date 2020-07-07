package com.irontower.gcbdriver.mvp.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jianghaifeng on 2017/12/13.
 */

public class MyAppCompatEditText extends AppCompatEditText {

  private DrawableRightListener mRightListener;


  public MyAppCompatEditText(Context context) {
    super(context);
  }

  public MyAppCompatEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MyAppCompatEditText(Context context, AttributeSet attrs,
      int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }


  public void setDrawableRightListener(DrawableRightListener listener) {
    this.mRightListener = listener;
  }

  public interface DrawableRightListener {

    public void onDrawableRightClick(View view);
  }

  @SuppressLint("ClickableViewAccessibility")
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_UP:
        if (mRightListener != null) {
          Drawable drawableRight = getCompoundDrawables()[2];
          if (drawableRight != null && event.getRawX() >= (getRight() - drawableRight.getBounds()
              .width())) {
            mRightListener.onDrawableRightClick(this);
            return true;
          }
        }

        break;
      default:
    }

    return super.onTouchEvent(event);
  }
}
