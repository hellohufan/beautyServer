package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.ListCallbackSingleChoice;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.di.component.DaggerChooseVehicleComponent;
import com.irontower.gcbuser.di.module.ChooseVehicleModule;
import com.irontower.gcbuser.mvp.contract.ChooseVehicleContract.View;
import com.irontower.gcbuser.mvp.model.entity.Dict;
import com.irontower.gcbuser.mvp.model.entity.Vehicle;
import com.irontower.gcbuser.mvp.presenter.ChooseVehiclePresenter;
import com.irontower.gcbuser.mvp.ui.adapter.ChooseVehicleAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class ChooseVehicleActivity extends BaseActivity<ChooseVehiclePresenter> implements
    View, OnRefreshListener {


  @BindView(R.id.editText)
  AppCompatEditText editText;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;
  @Inject
  ChooseVehicleAdapter adapter;
  @Inject
  LayoutManager mLayoutManager;
  @BindView(R.id.tvCarType)
  TextView tvCarType;
  @BindView(R.id.tvCarNum)
  TextView tvCarNum;
  private MaterialDialog chooseCarTypeDialog;
  private List<String> carTypes = new ArrayList<>();
  private List<Dict> carTypeDicts = new ArrayList<>();
  private String dialogCarType;
  private Integer carId = null;
  private String orderBeginTime, orderEndTime, useOrgId, carType, todoId, orderCarNo, selectedDriverId, selectedCarId, feeType, orderNo;


  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerChooseVehicleComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .chooseVehicleModule(new ChooseVehicleModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_choose_vehicle; //如果你不需要框架帮你设置 setContentView(vehicleId) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    orderBeginTime = getIntent().getStringExtra("orderBeginTime");
    orderEndTime = getIntent().getStringExtra("orderEndTime");
    useOrgId = getIntent().getStringExtra("useOrgId");
    feeType = getIntent().getStringExtra("feeType");
    orderNo = getIntent().getStringExtra("orderNo");

    todoId = getIntent().getStringExtra("todoId");
    orderCarNo = getIntent().getStringExtra("orderCarNo");
    selectedDriverId = getIntent().getStringExtra("selectedDriverId");
    selectedCarId = getIntent().getStringExtra("selectedCarId");

    carTypes.add("全部车辆");
    carType = "";
    dialogCarType = "";
    carTypes.addAll(AppUtil.getCarTypes1());
    carTypeDicts.addAll(AppUtil.getCarTypes());

    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    swipeRefreshLayout.setOnRefreshListener(this);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setHasFixedSize(true);
    adapter.setOnLoadMoreListener(new RequestLoadMoreListener() {
      @Override
      public void onLoadMoreRequested() {
        mPresenter
            .getData(editText.getText().toString(), false, orderBeginTime, orderEndTime, useOrgId,
                dialogCarType, selectedCarId, feeType, orderNo);

      }
    }, recyclerView);
    adapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, android.view.View view, int position) {
        List<Vehicle> vehicles = adapter.getData();
        for (Vehicle vehicle : vehicles) {
          if (vehicles.indexOf(vehicle) == position) {
            vehicle.setSelect(!vehicle.isSelect());
            carId = vehicle.isSelect() ? vehicle.getCarId() : null;
            carType = vehicle.isSelect() ? String.valueOf(vehicle.getCarType()) : "";
          } else {
            vehicle.setSelect(false);
          }
        }
        adapter.notifyDataSetChanged();
      }
    });

    adapter.setEmptyView(LayoutInflater.from(getActivity())
        .inflate(R.layout.empty_my_task, (ViewGroup) recyclerView.getParent(), false));
    recyclerView.setAdapter(adapter);
    onRefresh();
    editText.setOnEditorActionListener(new OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        mPresenter
            .getData(editText.getText().toString(), true, orderBeginTime, orderEndTime, useOrgId,
                dialogCarType, selectedCarId, feeType, orderNo);

        return false;
      }
    });

  }


  @Override
  public void showLoading() {
    swipeRefreshLayout.setRefreshing(true);


  }

  @Override
  public void hideLoading() {
    swipeRefreshLayout.setRefreshing(false);


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

  @Override
  public Activity getActivity() {
    return this;
  }


  @Override
  public void onRefresh() {
    mPresenter.getData(editText.getText().toString(), true, orderBeginTime, orderEndTime, useOrgId,
        dialogCarType, selectedCarId, feeType, orderNo);
  }


  @OnClick({R.id.layNext, R.id.ivSearch})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layNext:
        if (carId == null) {
          ArmsUtils.makeText(this, "请先选择车辆");

        } else {
          Intent intent = new Intent();

          intent.putExtra("carId", carId.toString());
          intent.putExtra("todoId", todoId);
          intent.putExtra("useOrgId", useOrgId);
          intent.putExtra("carType", carType);
          intent.putExtra("orderCarNo", orderCarNo);
          intent.putExtra("orderBeginTime", orderBeginTime);
          intent.putExtra("orderEndTime", orderEndTime);
          intent.putExtra("selectedDriverId", selectedDriverId);
          intent.putExtra("feeType", feeType);
          intent.setClass(getActivity(), ChooseDriverActivity.class);

          startActivity(intent);
        }
        break;
      case R.id.ivSearch:
        mPresenter
            .getData(editText.getText().toString(), true, orderBeginTime, orderEndTime, useOrgId,
                dialogCarType, selectedCarId, feeType, orderNo);

        break;
      default:
    }

  }

  @Subscriber(tag = EventBusTags.DISPATCHCARSUCCESS, mode = ThreadMode.MAIN)
  public void diapatchCarSuccess(String str) {
    finish();

  }


  @OnClick(R.id.layChooseCarType)
  public void onViewClicked() {
    if (chooseCarTypeDialog == null) {
      chooseCarTypeDialog = new Builder(this)
          .title("车辆类型选择")
          .items(carTypes)
          .itemsCallbackSingleChoice(0,
              new ListCallbackSingleChoice() {
                @Override
                public boolean onSelection(MaterialDialog dialog, android.view.View itemView,
                    int which, CharSequence text) {
                  String tem =
                      "全部车辆".equals(text) ? "" : AppUtil.getDictType((String) text, carTypeDicts);
                  mPresenter
                      .getData(editText.getText().toString(), true, orderBeginTime, orderEndTime,
                          useOrgId,
                          tem, selectedCarId, feeType, orderNo);
                  return false;
                }


              })
          .show();

    } else {
      chooseCarTypeDialog.setSelectedIndex(StringUtils.isEmpty(dialogCarType) ? 0
          : carTypes.indexOf(tvCarType.getText().toString()));
      chooseCarTypeDialog.show();

    }
  }

  @Override
  public void initView(int num, String type) {
    dialogCarType = type;

    tvCarType.setText(StringUtils.isEmpty(type) ? "全部车辆" : AppUtil.getDictName(type, carTypeDicts));
    tvCarNum.setText("(" + num + ")");


  }
}
