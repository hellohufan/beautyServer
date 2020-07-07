package com.irontower.gcbdriver.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.StringUtils;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.di.component.DaggerApplyFeeDetailComponent;
import com.irontower.gcbdriver.di.module.ApplyFeeDetailModule;
import com.irontower.gcbdriver.mvp.contract.ApplyFeeDetailContract.View;
import com.irontower.gcbdriver.mvp.model.entity.Report;
import com.irontower.gcbdriver.mvp.presenter.ApplyFeeDetailPresenter;
import com.irontower.gcbdriver.mvp.ui.widget.LengthFilter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;


public class ApplyFeeDetailActivity extends BaseActivity<ApplyFeeDetailPresenter> implements
    View {

  @BindView(R.id.etToll)
  AppCompatEditText etToll;
  @BindView(R.id.etPark)
  AppCompatEditText etPark;
  @BindView(R.id.etRest)
  AppCompatEditText etRest;
  @BindView(R.id.etMile)
  AppCompatEditText etMile;
  @BindView(R.id.etMinutes)
  AppCompatEditText etMinutes;
  @BindView(R.id.etGasCost)
  AppCompatEditText etGasCost;
  @BindView(R.id.etOtherCost)
  AppCompatEditText etOtherCost;
  private String orderCarNo;
  private Report report;
  private TimePickerView timePickerView;

  @Override
  public void setupActivityComponent(@NonNull AppComponent appComponent) {
    DaggerApplyFeeDetailComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .applyFeeDetailModule(new ApplyFeeDetailModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(@Nullable Bundle savedInstanceState) {
    return R.layout.activity_apply_fee_detail; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(@Nullable Bundle savedInstanceState) {
    orderCarNo = getIntent().getStringExtra("orderCarNo");
    report = getIntent().getParcelableExtra("report");
    if (report != null) {
      etToll.setText(report.getToll());

      etPark.setText(report.getPark());
      etRest.setText(report.getRest());
      etMile.setText(report.getMile());
      etMinutes.setText(report.getMinutes());
      etGasCost.setText(report.getGasCost());
      etOtherCost.setText(report.getOtherCost());
    }
    etToll.setSelection(etToll.getText().length());
    etToll.setFilters(new InputFilter[]{new LengthFilter()});
    etPark.setFilters(new InputFilter[]{new LengthFilter()});
    etRest.setFilters(new InputFilter[]{new LengthFilter()});
    etMile.setFilters(new InputFilter[]{new LengthFilter()});
    etMinutes.setFilters(new InputFilter[]{new LengthFilter()});
    etGasCost.setFilters(new InputFilter[]{new LengthFilter()});
    etOtherCost.setFilters(new InputFilter[]{new LengthFilter()});


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


  @OnClick(R.id.laySubmit)
  public void onViewClicked() {
    if (StringUtils.isEmpty(etToll.getText().toString().trim()) && StringUtils
        .isEmpty(etPark.getText().toString().trim()) && StringUtils
        .isEmpty(etRest.getText().toString().trim()) && StringUtils
        .isEmpty(etMile.getText().toString().trim()) && StringUtils
        .isEmpty(etMinutes.getText().toString().trim()) && StringUtils
        .isEmpty(etGasCost.getText().toString().trim()) && StringUtils
        .isEmpty(etOtherCost.getText().toString().trim())) {
      ArmsUtils.makeText(getActivity(), "填报费用不能全为空");
    } else {
      new Builder(
          ArmsUtils.obtainAppComponentFromContext(getActivity()).appManager()
              .getTopActivity())
          .title("提示").content("是否确认提交填报费用?")
          .positiveText("确认")
          .negativeText("取消")
          .negativeColor(ArmsUtils.getColor(getActivity(), R.color.empty_color))
          .onPositive(new SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog,
                @NonNull DialogAction which) {
//
              mPresenter
                  .submit(orderCarNo, etToll.getText().toString(), etPark.getText().toString(),
                      etRest.getText().toString(), etMile.getText().toString(),
                      etMinutes.getText().toString(),
                      etGasCost.getText().toString(), etOtherCost.getText().toString());

            }
          })
          .show()
      ;


    }
  }

  @Override
  public Activity getActivity() {
    return this;
  }


}
