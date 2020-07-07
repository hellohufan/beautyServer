package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.di.component.DaggerEvaluateComponent;
import com.irontower.gcbuser.di.module.EvaluateModule;
import com.irontower.gcbuser.mvp.contract.EvaluateContract.View;
import com.irontower.gcbuser.mvp.presenter.EvaluatePresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class EvaluateActivity extends BaseActivity<EvaluatePresenter> implements
    View {


  @BindView(R.id.ivSpeed1)
  ImageView ivSpeed1;
  @BindView(R.id.ivSpeed2)
  ImageView ivSpeed2;
  @BindView(R.id.ivSpeed3)
  ImageView ivSpeed3;
  @BindView(R.id.ivSpeed4)
  ImageView ivSpeed4;
  @BindView(R.id.ivSpeed5)
  ImageView ivSpeed5;
  @BindView(R.id.ivService1)
  ImageView ivService1;
  @BindView(R.id.ivService2)
  ImageView ivService2;
  @BindView(R.id.ivService3)
  ImageView ivService3;
  @BindView(R.id.ivService4)
  ImageView ivService4;
  @BindView(R.id.ivService5)
  ImageView ivService5;
  @BindView(R.id.etIdea)
  AppCompatEditText etIdea;
  @BindView(R.id.tvSpeed)
  TextView tvSpeed;
  @BindView(R.id.tvService)
  TextView tvService;
  @BindView(R.id.tvAttitude)
  TextView tvAttitude;
  @BindView(R.id.ivAttitude1)
  ImageView ivAttitude1;
  @BindView(R.id.ivAttitude2)
  ImageView ivAttitude2;
  @BindView(R.id.ivAttitude3)
  ImageView ivAttitude3;
  @BindView(R.id.ivAttitude4)
  ImageView ivAttitude4;
  @BindView(R.id.ivAttitude5)
  ImageView ivAttitude5;
  private int grade1 = 0, grade2 = 0, grade3;
  private String orderNo;
  @Inject
  MaterialDialog materialDialog;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerEvaluateComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .evaluateModule(new EvaluateModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_evaluate; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    orderNo = getIntent().getStringExtra("orderCarNo");

  }


  @Override
  public void showLoading() {
    if (materialDialog != null) {
      materialDialog.show();
    }

  }

  @Override
  public void hideLoading() {
    if (materialDialog != null) {
      materialDialog.dismiss();
    }
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


  @OnClick({R.id.ivSpeed1, R.id.ivSpeed2, R.id.ivSpeed3, R.id.ivSpeed4, R.id.ivSpeed5,
      R.id.ivService1, R.id.ivService2, R.id.ivService3, R.id.ivService4, R.id.ivService5,
      R.id.layEvaluate})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.ivSpeed1:
        ivSpeed1.setImageResource(R.mipmap.star_yellow);
        ivSpeed2.setImageResource(R.mipmap.star_gray);
        ivSpeed3.setImageResource(R.mipmap.star_gray);
        ivSpeed4.setImageResource(R.mipmap.star_gray);
        ivSpeed5.setImageResource(R.mipmap.star_gray);
        grade1 = 1;
        tvSpeed.setText("很差");
        break;
      case R.id.ivSpeed2:
        ivSpeed1.setImageResource(R.mipmap.star_yellow);
        ivSpeed2.setImageResource(R.mipmap.star_yellow);
        ivSpeed3.setImageResource(R.mipmap.star_gray);
        ivSpeed4.setImageResource(R.mipmap.star_gray);
        ivSpeed5.setImageResource(R.mipmap.star_gray);
        grade1 = 2;
        tvSpeed.setText("一般");
        break;
      case R.id.ivSpeed3:
        ivSpeed1.setImageResource(R.mipmap.star_yellow);
        ivSpeed2.setImageResource(R.mipmap.star_yellow);
        ivSpeed3.setImageResource(R.mipmap.star_yellow);
        ivSpeed4.setImageResource(R.mipmap.star_gray);
        ivSpeed5.setImageResource(R.mipmap.star_gray);
        grade1 = 3;
        tvSpeed.setText("满意");

        break;
      case R.id.ivSpeed4:
        ivSpeed1.setImageResource(R.mipmap.star_yellow);
        ivSpeed2.setImageResource(R.mipmap.star_yellow);
        ivSpeed3.setImageResource(R.mipmap.star_yellow);
        ivSpeed4.setImageResource(R.mipmap.star_yellow);
        ivSpeed5.setImageResource(R.mipmap.star_gray);
        grade1 = 4;
        tvSpeed.setText("非常满意");

        break;
      case R.id.ivSpeed5:
        ivSpeed1.setImageResource(R.mipmap.star_yellow);
        ivSpeed2.setImageResource(R.mipmap.star_yellow);
        ivSpeed3.setImageResource(R.mipmap.star_yellow);
        ivSpeed4.setImageResource(R.mipmap.star_yellow);
        ivSpeed5.setImageResource(R.mipmap.star_yellow);
        grade1 = 5;
        tvSpeed.setText("无可挑剔");
        break;
      case R.id.ivService1:
        ivService1.setImageResource(R.mipmap.star_yellow);
        ivService2.setImageResource(R.mipmap.star_gray);
        ivService3.setImageResource(R.mipmap.star_gray);
        ivService4.setImageResource(R.mipmap.star_gray);
        ivService5.setImageResource(R.mipmap.star_gray);
        grade2 = 1;
        tvService.setText("很差");
        break;
      case R.id.ivService2:
        ivService1.setImageResource(R.mipmap.star_yellow);
        ivService2.setImageResource(R.mipmap.star_yellow);
        ivService3.setImageResource(R.mipmap.star_gray);
        ivService4.setImageResource(R.mipmap.star_gray);
        ivService5.setImageResource(R.mipmap.star_gray);
        grade2 = 2;
        tvService.setText("一般");

        break;
      case R.id.ivService3:
        ivService1.setImageResource(R.mipmap.star_yellow);
        ivService2.setImageResource(R.mipmap.star_yellow);
        ivService3.setImageResource(R.mipmap.star_yellow);
        ivService4.setImageResource(R.mipmap.star_gray);
        ivService5.setImageResource(R.mipmap.star_gray);
        grade2 = 3;
        tvService.setText("满意");

        break;
      case R.id.ivService4:
        ivService1.setImageResource(R.mipmap.star_yellow);
        ivService2.setImageResource(R.mipmap.star_yellow);
        ivService3.setImageResource(R.mipmap.star_yellow);
        ivService4.setImageResource(R.mipmap.star_yellow);
        ivService5.setImageResource(R.mipmap.star_gray);
        grade2 = 4;
        tvService.setText("非常满意");

        break;
      case R.id.ivService5:
        ivService1.setImageResource(R.mipmap.star_yellow);
        ivService2.setImageResource(R.mipmap.star_yellow);
        ivService3.setImageResource(R.mipmap.star_yellow);
        ivService4.setImageResource(R.mipmap.star_yellow);
        ivService5.setImageResource(R.mipmap.star_yellow);
        grade2 = 5;
        tvService.setText("无可挑剔");
        break;
      case R.id.layEvaluate:

        mPresenter.evaluate(orderNo, grade1, grade2, grade3, etIdea.getText().toString());

        break;

      default:
    }
  }

  @Override
  public Activity getActivity() {
    return this;
  }

  @Subscriber(tag = EventBusTags.EVALUATE_SUCCESS, mode = ThreadMode.MAIN)
  public void evaluate(String str) {
    finish();

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (materialDialog != null) {
      materialDialog.dismiss();
      materialDialog = null;
    }

  }


  @OnClick({R.id.ivAttitude1, R.id.ivAttitude2, R.id.ivAttitude3, R.id.ivAttitude4,
      R.id.ivAttitude5})
  public void onViewClicked3(android.view.View view) {
    switch (view.getId()) {
      case R.id.ivAttitude1:
        ivAttitude1.setImageResource(R.mipmap.star_yellow);
        ivAttitude2.setImageResource(R.mipmap.star_gray);
        ivAttitude3.setImageResource(R.mipmap.star_gray);
        ivAttitude4.setImageResource(R.mipmap.star_gray);
        ivAttitude5.setImageResource(R.mipmap.star_gray);
        grade3 = 1;
        tvAttitude.setText("很差");
        break;
      case R.id.ivAttitude2:
        ivAttitude1.setImageResource(R.mipmap.star_yellow);
        ivAttitude2.setImageResource(R.mipmap.star_yellow);
        ivAttitude3.setImageResource(R.mipmap.star_gray);
        ivAttitude4.setImageResource(R.mipmap.star_gray);
        ivAttitude5.setImageResource(R.mipmap.star_gray);
        grade3 = 2;
        tvAttitude.setText("一般");
        break;
      case R.id.ivAttitude3:
        ivAttitude1.setImageResource(R.mipmap.star_yellow);
        ivAttitude2.setImageResource(R.mipmap.star_yellow);
        ivAttitude3.setImageResource(R.mipmap.star_yellow);
        ivAttitude4.setImageResource(R.mipmap.star_gray);
        ivAttitude5.setImageResource(R.mipmap.star_gray);
        grade3 = 3;
        tvAttitude.setText("满意");

        break;
      case R.id.ivAttitude4:
        ivAttitude1.setImageResource(R.mipmap.star_yellow);
        ivAttitude2.setImageResource(R.mipmap.star_yellow);
        ivAttitude3.setImageResource(R.mipmap.star_yellow);
        ivAttitude4.setImageResource(R.mipmap.star_yellow);
        ivAttitude5.setImageResource(R.mipmap.star_gray);
        grade3 = 4;
        tvAttitude.setText("非常满意");

        break;
      case R.id.ivAttitude5:
        ivAttitude1.setImageResource(R.mipmap.star_yellow);
        ivAttitude2.setImageResource(R.mipmap.star_yellow);
        ivAttitude3.setImageResource(R.mipmap.star_yellow);
        ivAttitude4.setImageResource(R.mipmap.star_yellow);
        ivAttitude5.setImageResource(R.mipmap.star_yellow);
        grade3 = 5;
        tvAttitude.setText("无可挑剔");
        break;
      default:
        break;
    }
  }
}
