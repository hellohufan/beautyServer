package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import butterknife.OnClick;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.di.component.DaggerManagePeopleCarComponent;
import com.irontower.gcbuser.di.module.ManagePeopleCarModule;
import com.irontower.gcbuser.mvp.contract.ManagePeopleCarContract.View;
import com.irontower.gcbuser.mvp.presenter.ManagePeopleCarPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;


public class ManagePeopleCarActivity extends BaseActivity<ManagePeopleCarPresenter> implements
    View {


  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerManagePeopleCarComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .managePeopleCarModule(new ManagePeopleCarModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_manage_people_car; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {

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


  @OnClick({R.id.layManageCar, R.id.layManageDriver, R.id.layBegOff, R.id.layBusiness,
      R.id.layAppearRecord, R.id.layBackRecord})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layManageCar:
        ArmsUtils.startActivity(getActivity(), ManageCarActivity.class);
        break;
      case R.id.layManageDriver:
        ArmsUtils.startActivity(getActivity(), ManageDriverActivity.class);

        break;
      case R.id.layBegOff:
        ArmsUtils.startActivity(getActivity(), ApproveBegOffActivity.class);

        break;
      case R.id.layBusiness:
        ArmsUtils.startActivity(getActivity(), ApproveGoOutActivity.class);

        break;

      case R.id.layAppearRecord:
        ArmsUtils.startActivity(getActivity(), GoOutRecordActivity.class);

        break;
      case R.id.layBackRecord:
        ArmsUtils.startActivity(getActivity(), GoBackRecordActivity.class);

        break;
      default:
    }
  }

  @Override
  public Activity getActivity() {
    return this;
  }
}
