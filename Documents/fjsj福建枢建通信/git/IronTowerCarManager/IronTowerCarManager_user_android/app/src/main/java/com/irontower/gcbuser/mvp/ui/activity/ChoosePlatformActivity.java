package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.StringUtils;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.di.component.DaggerChoosePlatformComponent;
import com.irontower.gcbuser.di.module.ChoosePlatformModule;
import com.irontower.gcbuser.mvp.contract.ChoosePlatformContract.View;
import com.irontower.gcbuser.mvp.model.entity.Dict;
import com.irontower.gcbuser.mvp.model.entity.Order;
import com.irontower.gcbuser.mvp.presenter.ChoosePlatformPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.util.ArrayList;
import java.util.List;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class ChoosePlatformActivity extends BaseActivity<ChoosePlatformPresenter> implements
    View {


  @BindView(R.id.tvType)
  TextView tvType;

  private MaterialDialog chooseTypeDialog, chooseCarTypeDialog;
  private List<String> types = new ArrayList<>();
  private List<String> carTypes = new ArrayList<>();
  private List<Dict> carTypeDicts = new ArrayList<>();
  private Order order;
  private String todoId;
  private String orderCarNo, orderNo;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerChoosePlatformComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .choosePlatformModule(new ChoosePlatformModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_choose_platform; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    types.add("单位派车");
    types.add("平台派车");
    types.add("平台租车");
    carTypes.addAll(AppUtil.getCarTypes1());
    carTypeDicts.addAll(AppUtil.getCarTypes());
    order = getIntent().getParcelableExtra("order");
    todoId = getIntent().getStringExtra("todoId");
    orderCarNo = getIntent().getStringExtra("orderCarNo");
    orderNo = getIntent().getStringExtra("orderNo");


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


  @OnClick({R.id.layType, R.id.layNext})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layType:
        if (chooseTypeDialog == null) {
          chooseTypeDialog = new MaterialDialog.Builder(this)
              .title("派车方式")
              .items(types)
              .itemsCallbackSingleChoice(0,
                  new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, android.view.View itemView,
                        int which, CharSequence text) {
                      tvType.setText(text);
                      return false;
                    }


                  })
              .show();

        } else {
          chooseTypeDialog.setSelectedIndex(StringUtils.isEmpty(tvType.getText()) ? 0
              : types.indexOf(tvType.getText().toString()));
          chooseTypeDialog.show();

        }
        break;

      case R.id.layNext:
        if (StringUtils.isEmpty(tvType.getText())) {
          ArmsUtils.makeText(getActivity(), "派车方式不能为空");
          return;
        }

        Intent intent = new Intent();
        intent.putExtra("orderCarNo", orderCarNo);
        if (StringUtils.equals(tvType.getText(), "平台派车")) {

          intent.putExtra("todoId", todoId);
          intent.putExtra("order", order);
          intent.putExtra("rent", false);
          intent.setClass(getActivity(), RentVehicleActivity.class);
        }
        if (StringUtils.equals(tvType.getText(), "平台租车")) {

          intent.putExtra("todoId", todoId);

          intent.putExtra("order", order);
          intent.putExtra("rent", true);
          intent.setClass(getActivity(), RentVehicleActivity.class);
        }
        if (StringUtils.equals(tvType.getText(), "单位派车")) {

          intent.putExtra("todoId", todoId);

          intent.putExtra("order", order);
          intent.putExtra("useOrgId", getIntent().getStringExtra("useOrgId"));
          intent.putExtra("orderNo", orderNo);
          intent.putExtra("orderBeginTime", getIntent().getStringExtra("orderBeginTime"));
          intent.putExtra("orderEndTime", getIntent().getStringExtra("orderEndTime"));
          intent.setClass(getActivity(), ChooseVehicleActivity.class);
        }
        startActivity(intent);

        break;
      default:
    }
  }

  @Override
  public Activity getActivity() {
    return this;
  }

  @Subscriber(tag = EventBusTags.RENT_CAR_SUCCESS, mode = ThreadMode.MAIN)
  public void rentCarSuccess(String str) {
    finish();

  }

  @Subscriber(tag = EventBusTags.DISPATCHCARSUCCESS, mode = ThreadMode.MAIN)
  public void diapatchCarSuccess(String str) {
    finish();

  }
}
