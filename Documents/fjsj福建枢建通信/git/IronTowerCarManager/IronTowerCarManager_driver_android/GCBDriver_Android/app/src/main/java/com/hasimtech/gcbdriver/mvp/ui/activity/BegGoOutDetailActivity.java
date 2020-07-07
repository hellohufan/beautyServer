package com.irontower.gcbdriver.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.blankj.utilcode.util.StringUtils;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.app.EventBusTags;
import com.irontower.gcbdriver.app.utils.AppUtil;
import com.irontower.gcbdriver.app.utils.DateUtil;
import com.irontower.gcbdriver.di.component.DaggerBegGoOutDetailComponent;
import com.irontower.gcbdriver.di.module.BegGoOutDetailModule;
import com.irontower.gcbdriver.mvp.contract.BegGoOutDetailContract.View;
import com.irontower.gcbdriver.mvp.model.entity.BegGoOut;
import com.irontower.gcbdriver.mvp.presenter.BegGoOutDetailPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class BegGoOutDetailActivity extends BaseActivity<BegGoOutDetailPresenter> implements
    View {

  @BindView(R.id.tvCarNo)
  TextView tvCarNo;
  @BindView(R.id.tvBeginTime)
  TextView tvBeginTime;
  @BindView(R.id.tvEndTime)
  TextView tvEndTime;
  @BindView(R.id.tvState)
  TextView tvState;
  @BindView(R.id.tvRealStartTime)
  TextView tvRealStartTime;
  @BindView(R.id.layRealStartTime)
  RelativeLayout layRealStartTime;
  @BindView(R.id.tvRealEndTime)
  TextView tvRealEndTime;
  @BindView(R.id.tvSumbit)
  TextView tvSumbit;
  @BindView(R.id.layRealEndTime)
  RelativeLayout layRealEndTime;
  @BindView(R.id.tvType)
  TextView tvType;
  @BindView(R.id.ivReason)
  ImageView ivReason;
  @BindView(R.id.tvReason)
  TextView tvReason;

  @BindView(R.id.line1)
  android.view.View line1;
  @BindView(R.id.line2)
  android.view.View line2;
  @BindView(R.id.laySubmit)
  RelativeLayout laySubmit;
  private BegGoOut begGoOut;
  @Inject
  MaterialDialog materialDialog;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerBegGoOutDetailComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .begGoOutDetailModule(new BegGoOutDetailModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_beg_go_out_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    begGoOut = getIntent().getParcelableExtra("begGoOut");
    tvCarNo.setText(begGoOut.getCarNo());
    tvBeginTime.setText(DateUtil.formatDate(DateUtil.YYYY_MM_DD_HH_MI_SS, begGoOut.getOutTime(),
        DateUtil.YYYY_MM_DD_HH_MI));
    tvEndTime.setText(DateUtil
        .formatDate(DateUtil.YYYY_MM_DD_HH_MI_SS, begGoOut.getInTime(), DateUtil.YYYY_MM_DD_HH_MI));
    tvState.setText(
        AppUtil.getDictName(String.valueOf(begGoOut.getState()), AppUtil.getCarOutState()));
    tvReason.setText(StringUtils.isEmpty(begGoOut.getApplyDesc()) ? "暂无" : begGoOut.getApplyDesc());
    tvType.setText(
        AppUtil.getDictName(String.valueOf(begGoOut.getOutType()), AppUtil.getCarOutType()));
    tvRealStartTime.setText(begGoOut.getRealOutTime());
    tvRealEndTime.setText(begGoOut.getRealInTime());
    if (begGoOut.getState() == 0 || begGoOut.getState() == 2 || begGoOut.getState() == 5) {
      laySubmit.setVisibility(android.view.View.GONE);
      layRealEndTime.setVisibility(android.view.View.GONE);
      layRealStartTime.setVisibility(android.view.View.GONE);
      line1.setVisibility(android.view.View.GONE);
      line2.setVisibility(android.view.View.GONE);
    } else if (begGoOut.getState() == 1) {

      layRealEndTime.setVisibility(android.view.View.GONE);
      layRealStartTime.setVisibility(android.view.View.GONE);
      line1.setVisibility(android.view.View.GONE);
      line2.setVisibility(android.view.View.GONE);
      tvSumbit.setText("确认出场");

    } else if (begGoOut.getState() == 3) {

      tvSumbit.setText("确认回场");
      layRealEndTime.setVisibility(android.view.View.GONE);
      line2.setVisibility(android.view.View.GONE);

    } else if (begGoOut.getState() == 4) {
      laySubmit.setVisibility(android.view.View.GONE);
    }


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


  @OnClick(R.id.laySubmit)
  public void onViewClicked() {

    new MaterialDialog.Builder(getActivity())
        .title("提示")
        .positiveColor(ArmsUtils.getColor(getActivity(), R.color.colorPrimary))
        .negativeColor(ArmsUtils.getColor(getActivity(), R.color.empty_color))
        .content(begGoOut.getState() == 1 ? "是否确认出场?" : "是否确认回场?")
        .negativeText(R.string.cancel)
        .positiveText(R.string.confirm)
        .onPositive(new SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog takeOrderDialog,
              @NonNull DialogAction which) {
            mPresenter.changeGoOut(begGoOut.getHid(), begGoOut.getState() == 1 ? "3" : "4");

          }
        })
        .show();
  }

  @Override
  public Activity getActivity() {
    return this;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (materialDialog != null) {
      materialDialog.dismiss();
      materialDialog = null;
    }

  }

  @Subscriber(tag = EventBusTags.CHANGE_GO_OUT_SUCCESS, mode = ThreadMode.MAIN)
  public void changeUserName(String userName) {
    finish();

  }

}
