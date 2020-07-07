package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.di.component.DaggerChooseDriverComponent;
import com.irontower.gcbuser.di.module.ChooseDriverModule;
import com.irontower.gcbuser.mvp.contract.ChooseDriverContract;
import com.irontower.gcbuser.mvp.model.entity.Driver;
import com.irontower.gcbuser.mvp.presenter.ChooseDriverPresenter;
import com.irontower.gcbuser.mvp.ui.adapter.ChooseDriverAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.util.List;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class ChooseDriverActivity extends BaseActivity<ChooseDriverPresenter> implements
    ChooseDriverContract.View, SwipeRefreshLayout.OnRefreshListener {

  @BindView(R.id.editText)
  AppCompatEditText editText;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;
  @Inject
  ChooseDriverAdapter adapter;
  @Inject
  LayoutManager mLayoutManager;
  private Integer driverId = null;
  private String orderBeginTime, orderEndTime, useOrgId, carId, todoId, orderCarNo, carType, selectedDriverId, feeType;
  @Inject
  MaterialDialog materialDialog;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerChooseDriverComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .chooseDriverModule(new ChooseDriverModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_choose_driver; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    orderBeginTime = getIntent().getStringExtra("orderBeginTime");
    orderEndTime = getIntent().getStringExtra("orderEndTime");
    useOrgId = getIntent().getStringExtra("useOrgId");
    todoId = getIntent().getStringExtra("todoId");
    orderCarNo = getIntent().getStringExtra("orderCarNo");
    carId = getIntent().getStringExtra("carId");
    carType = getIntent().getStringExtra("carType");
    selectedDriverId = getIntent().getStringExtra("selectedDriverId");
    feeType = getIntent().getStringExtra("feeType");
    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    swipeRefreshLayout.setOnRefreshListener(this);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setHasFixedSize(true);
    adapter.setOnLoadMoreListener(new RequestLoadMoreListener() {
      @Override
      public void onLoadMoreRequested() {

        mPresenter
            .getData(editText.getText().toString(), false, orderBeginTime, orderEndTime, useOrgId,
                selectedDriverId);

      }
    }, recyclerView);
    adapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<Driver> drivers = adapter.getData();
        for (Driver driver : drivers) {
          if (drivers.indexOf(driver) == position) {
            driver.setSelect(!driver.isSelect());
            driverId = driver.isSelect() ? driver.getUserId() : null;
          } else {
            driver.setSelect(false);
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
                selectedDriverId);
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
        selectedDriverId);
  }

  @OnClick({R.id.layConfirm, R.id.ivSearch})
  public void onViewClicked(View view) {

    switch (view.getId()) {
      case R.id.layConfirm:
        if (StringUtils.isEmpty(carId)) {
          ArmsUtils.makeText(this, "请先选择车辆");
          return;
        }
        if (driverId == null) {
          ArmsUtils.makeText(this, "请先选择司机");
          return;
        }
        mPresenter.submit(todoId, orderCarNo, carId, driverId, carType, feeType);
        break;

      case R.id.ivSearch:
        mPresenter
            .getData(editText.getText().toString(), true, orderBeginTime, orderEndTime, useOrgId,
                selectedDriverId);

        break;
      default:
    }


  }

  @Subscriber(tag = EventBusTags.DISPATCHCARSUCCESS, mode = ThreadMode.MAIN)
  public void diapatchCarSuccess(String str) {
    finish();

  }

  @Override
  public void showLoading1() {
    if (materialDialog != null) {
      materialDialog.show();
    }

  }

  @Override
  public void hideLoading1() {
    if (materialDialog != null) {
      materialDialog.dismiss();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (materialDialog != null) {
      materialDialog.dismiss();
      materialDialog = null;
    }
  }
}
