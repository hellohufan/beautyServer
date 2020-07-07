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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import butterknife.BindView;
import butterknife.OnClick;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.app.utils.DateUtil;
import com.irontower.gcbuser.di.component.DaggerApproveHistoryComponent;
import com.irontower.gcbuser.di.module.ApproveHistoryModule;
import com.irontower.gcbuser.mvp.contract.ApproveHistoryContract.View;
import com.irontower.gcbuser.mvp.model.entity.ApproveOrder;
import com.irontower.gcbuser.mvp.presenter.ApproveHistoryPresenter;
import com.irontower.gcbuser.mvp.ui.adapter.ApproveHistoryAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.util.Calendar;
import java.util.Map;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class ApproveHistoryActivity extends BaseActivity<ApproveHistoryPresenter> implements
    View, OnRefreshListener {


  @BindView(R.id.editText)
  AppCompatEditText editText;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;
  @Inject
  ApproveHistoryAdapter adapter;
  @Inject
  LayoutManager mLayoutManager;
  @BindView(R.id.ivCreateTime)
  ImageView ivCreateTime;
  @BindView(R.id.ivApprovalTime)
  ImageView ivApprovalTime;
  private String orderCol = "dealTime";
  private String orderBy = "desc";
  private String beginCreateTime, endCreateTime, beginTime = "", endTime = "";
  private Calendar calendar = Calendar.getInstance();


  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerApproveHistoryComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .approveHistoryModule(new ApproveHistoryModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_approve_history; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    endCreateTime = DateUtil.format(DateUtil.YYYY_MM_DD, calendar.getTime());
    beginCreateTime = DateUtil
        .format(DateUtil.YYYY_MM_DD, DateUtil.addDateOfDate(calendar.getTime(), -7));
    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    swipeRefreshLayout.setOnRefreshListener(this);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setHasFixedSize(true);
    adapter.setOnLoadMoreListener(new RequestLoadMoreListener() {
      @Override
      public void onLoadMoreRequested() {
        mPresenter.getData(editText.getText().toString(), orderBy, orderCol, beginCreateTime,
            endCreateTime, false, beginTime,
            endTime);


      }
    }, recyclerView);
    adapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, android.view.View view, int position) {
        ApproveOrder approveOrder = (ApproveOrder) adapter.getData().get(position);
        Intent intent = new Intent();
        intent.putExtra("orderNo", approveOrder.getOrderNo());
        intent.setClass(getActivity(), ApproveHistoryDetailActivity.class);
        startActivity(intent);

      }
    });

    adapter.setEmptyView(LayoutInflater.from(getActivity())
        .inflate(R.layout.empty_my_task, (ViewGroup) recyclerView.getParent(), false));
    recyclerView.setAdapter(adapter);
    onRefresh();
    editText.setOnEditorActionListener(new OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        mPresenter.getData(editText.getText().toString(), orderBy, orderCol, beginCreateTime,
            endCreateTime, true, beginTime,
            endTime);
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
    mPresenter
        .getData(editText.getText().toString(), orderBy, orderCol, beginCreateTime, endCreateTime,
            true, beginTime, endTime);
  }


  @OnClick(R.id.ivSearch)
  public void onViewClicked() {
    mPresenter
        .getData(editText.getText().toString(), orderBy, orderCol, beginCreateTime, endCreateTime,
            true, beginTime, endTime);
  }


  @OnClick({R.id.layCreateTime, R.id.layApproveTime, R.id.layFilter})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layCreateTime:
        if ("commitTime".equals(orderCol)) {
          if (orderBy.equals("desc")) {
            orderBy = "asc";
            ivCreateTime.setImageResource(R.mipmap.time_up);
          } else {
            orderBy = "desc";
            ivCreateTime.setImageResource(R.mipmap.time_down);
          }
        } else {
          orderCol = "commitTime";
          orderBy = "desc";
          ivApprovalTime.setVisibility(android.view.View.INVISIBLE);
          ivCreateTime.setImageResource(R.mipmap.time_down);

          ivCreateTime.setVisibility(android.view.View.VISIBLE);


        }
        onRefresh();
        break;
      case R.id.layApproveTime:
        if ("dealTime".equals(orderCol)) {
          if (orderBy.equals("desc")) {
            orderBy = "asc";
            ivApprovalTime.setImageResource(R.mipmap.time_up);
          } else {
            orderBy = "desc";
            ivApprovalTime.setImageResource(R.mipmap.time_down);
          }
        } else {
          orderCol = "dealTime";
          orderBy = "desc";
          ivCreateTime.setVisibility(android.view.View.INVISIBLE);
          ivApprovalTime.setVisibility(android.view.View.VISIBLE);
          ivApprovalTime.setImageResource(R.mipmap.time_down);


        }
        onRefresh();
        break;
      case R.id.layFilter:
        Intent intent = new Intent();
        intent.putExtra("beginCreateTime", beginCreateTime);
        intent.putExtra("endCreateTime", endCreateTime);
        intent.setClass(getActivity(), TimeSelectActivity.class);
        startActivity(intent);

        break;
      default:
        break;
    }
  }

  @Subscriber(tag = EventBusTags.TIMESELECT, mode = ThreadMode.MAIN)
  public void getMobile(Map<String, Object> map) {
    beginTime = String.valueOf(map.get("beginTime"));
    endTime = String.valueOf(map.get("endTime"));
    beginCreateTime = String.valueOf(map.get("beginCreateTime"));
    endCreateTime = String.valueOf(map.get("endCreateTime"));
    mPresenter.getData(editText.getText().toString(), orderBy, orderCol, beginCreateTime,
        endCreateTime, true, beginTime, endTime
    );
  }


}


