package com.irontower.gcbdriver.mvp.ui.activity;

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
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import butterknife.BindView;
import butterknife.OnClick;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.app.EventBusTags;
import com.irontower.gcbdriver.di.component.DaggerChooseCarComponent;
import com.irontower.gcbdriver.di.module.ChooseCarModule;
import com.irontower.gcbdriver.mvp.contract.ChooseCarContract;
import com.irontower.gcbdriver.mvp.model.entity.Car;
import com.irontower.gcbdriver.mvp.presenter.ChooseCarPresenter;
import com.irontower.gcbdriver.mvp.ui.adapter.ChooseCarAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class ChooseCarActivity extends BaseActivity<ChooseCarPresenter> implements
    ChooseCarContract.View, SwipeRefreshLayout.OnRefreshListener {


  @BindView(R.id.editText)
  AppCompatEditText editText;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;
  @Inject
  ChooseCarAdapter adapter;
  @Inject
  LayoutManager mLayoutManager;
  private String beginTime, endTime;
  private Integer carId;
  private String carNo;


  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerChooseCarComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .chooseCarModule(new ChooseCarModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_choose_car; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    beginTime = getIntent().getStringExtra("beginTime");
    endTime = getIntent().getStringExtra("endTime");

    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    swipeRefreshLayout.setOnRefreshListener(this);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setHasFixedSize(true);
    adapter.setOnLoadMoreListener(new RequestLoadMoreListener() {
      @Override
      public void onLoadMoreRequested() {
        mPresenter.getData(beginTime, endTime, editText.getText().toString().trim(), false);
      }
    }, recyclerView);
    adapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, android.view.View view, int position) {
        List<Car> vehicles = adapter.getData();
        for (Car vehicle : vehicles) {
          if (vehicles.indexOf(vehicle) == position) {
            vehicle.setSelect(!vehicle.isSelect());
            carId = vehicle.isSelect() ? vehicle.getCarId() : null;
            carNo = vehicle.isSelect() ? vehicle.getCarNo() : null;
          } else {
            vehicle.setSelect(false);
          }
        }
        adapter.notifyDataSetChanged();
      }
    });

    adapter.setEmptyView(LayoutInflater.from(getActivity())
        .inflate(R.layout.empty_no_data, (ViewGroup) recyclerView.getParent(), false));
    recyclerView.setAdapter(adapter);
    onRefresh();
    editText.setOnEditorActionListener(new OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        mPresenter.getData(beginTime, endTime, editText.getText().toString().trim(), true);
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
    mPresenter.getData(beginTime, endTime, editText.getText().toString().trim(), true);

  }

  @OnClick({R.id.layNext, R.id.ivSearch})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layNext:
        if (carId == null) {
          ArmsUtils.makeText(getActivity(), "请先选择车辆");
          return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("carId", carId);
        map.put("carNo", carNo);
        EventBus.getDefault().post(map, EventBusTags.CHOOSE_CAR_SUCCESS);
        break;
      case R.id.ivSearch:
        mPresenter.getData(beginTime, endTime, editText.getText().toString().trim(), true);

        break;
      default:
    }

  }

  @Subscriber(tag = EventBusTags.CHOOSE_CAR_SUCCESS, mode = ThreadMode.MAIN)
  public void chooesCarSuccess(Map<String, Object> map) {
    finish();
  }
}
