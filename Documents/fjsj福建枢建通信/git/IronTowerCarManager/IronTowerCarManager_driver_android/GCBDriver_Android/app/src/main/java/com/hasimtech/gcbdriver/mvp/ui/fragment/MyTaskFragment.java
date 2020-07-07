package com.irontower.gcbdriver.mvp.ui.fragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.app.EventBusTags;
import com.irontower.gcbdriver.app.utils.AppUtil;
import com.irontower.gcbdriver.di.component.DaggerMyTaskComponent;
import com.irontower.gcbdriver.di.module.MyTaskModule;
import com.irontower.gcbdriver.mvp.contract.MyTaskContract;
import com.irontower.gcbdriver.mvp.model.entity.Order;
import com.irontower.gcbdriver.mvp.presenter.MyTaskPresenter;
import com.irontower.gcbdriver.mvp.ui.activity.TaskDetailActivity;
import com.irontower.gcbdriver.mvp.ui.adapter.MyTaskAdapter;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class MyTaskFragment extends BaseFragment<MyTaskPresenter> implements MyTaskContract.View,
    SwipeRefreshLayout.OnRefreshListener {


  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;
  @Inject
  LayoutManager mLayoutManager;
  @Inject
  MyTaskAdapter adapter;
  @Inject
  RxPermissions rxPermissions;
  private MaterialDialog installDialog;
  private Context context;
  private boolean canClick = true;

  public static MyTaskFragment newInstance() {
    MyTaskFragment fragment = new MyTaskFragment();
    return fragment;
  }

  @Override
  public void setupFragmentComponent(AppComponent appComponent) {
    DaggerMyTaskComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .myTaskModule(new MyTaskModule(this))
        .build()
        .inject(this);
  }

  @Override
  public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_waiting_order, container, false);
    context = view.getContext();
    return view;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

  }

  @Override
  public void initData(Bundle savedInstanceState) {

    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    swipeRefreshLayout.setOnRefreshListener(this);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setHasFixedSize(true);
//
    adapter.setOnItemChildClickListener((adapter, view, position) -> {
      Order order = (Order) adapter.getData().get(position);

      switch (view.getId()) {
        case (R.id.layNavigation):
          if (!canClick) {
            return;
          }
          canClick = false;
          RouteParaOption para = new RouteParaOption()
              .startPoint(AppUtil.getLatLng(order.getBeginGps()))
              .endPoint(AppUtil.getLatLng(order.getEndGps()))
              .startName(order.getBeginAddr()).endName(order.getEndAddr());

          try {
            BaiduMapRoutePlan.openBaiduMapDrivingRoute(para, view.getContext());
          } catch (Exception e) {
            e.printStackTrace();
            showDialog();
          }
          BaiduMapRoutePlan.finish(view.getContext());

          break;

        case R.id.layCall:
          mPresenter.call(order.getPassengerTel());

          break;
        default:
          break;
      }
    });
    adapter.setOnLoadMoreListener(new RequestLoadMoreListener() {
      @Override
      public void onLoadMoreRequested() {
        mPresenter.refresh(false);


      }
    }, recyclerView);
    adapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        Order order = (Order) adapter.getData().get(position);
        Intent intent = new Intent();
        intent.putExtra("orderCarNo", order.getOrderCarNo());
        intent.putExtra("beginGps", AppUtil.getLatLng(order.getBeginGps()));
        intent.putExtra("endGps", AppUtil.getLatLng(order.getEndGps()));
        intent.putExtra("beginAddr", order.getBeginAddr());

        intent.putExtra("endAddr", order.getEndAddr());

        intent.setClass(view.getContext(), TaskDetailActivity.class);
        startActivity(intent);
      }
    });

    adapter.setEmptyView(LayoutInflater.from(context)
        .inflate(R.layout.empty_my_task, (ViewGroup) recyclerView.getParent(), false));
    recyclerView.setAdapter(adapter);
    onRefresh();


  }


  @Override
  public void setData(Object data) {

  }


  @Override
  public void showLoading() {
    if (swipeRefreshLayout != null) {
      swipeRefreshLayout.setRefreshing(true);

    }


  }

  @Override
  public void hideLoading() {
    if (swipeRefreshLayout != null) {
      swipeRefreshLayout.setRefreshing(false);
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

  }


  @Override
  public void onRefresh() {
    mPresenter.refresh(true);
  }


  public void showDialog() {

    if (installDialog != null) {
      installDialog.show();

    } else {
      installDialog = new MaterialDialog.Builder(context)
          .title("提示")
          .positiveColor(ArmsUtils.getColor(context, R.color.colorPrimary))
          .negativeColor(ArmsUtils.getColor(getActivity(), R.color.empty_color))
          .content("您尚未安装百度地图app或app版本过低，点击确认安装")
          .negativeText(R.string.cancel)
          .positiveText(R.string.confirm)
          .onPositive(new SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog takeOrderDialog,
                @NonNull DialogAction which) {
              OpenClientUtil.getLatestBaiduMapApp(context);


            }
          })
          .show();


    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (installDialog != null) {
      installDialog.dismiss();
      installDialog = null;
    }
  }

  @Override
  public RxPermissions getRxpermissons() {
    return rxPermissions;
  }

  @Override
  public Fragment getFragment() {
    return this;
  }

  @Subscriber(tag = EventBusTags.END_ORDER, mode = ThreadMode.MAIN)
  public void endOrder(String userName) {
    onRefresh();
  }

  @Subscriber(tag = EventBusTags.TAKE_ORDER_SUCCESS, mode = ThreadMode.MAIN)
  public void takeOrderSuccess(String userName) {
    mPresenter.refresh(true);

  }

  @Override
  public void onResume() {
    super.onResume();
    MobclickAgent.onPageStart(
        "MyTaskFragment"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
  }

  @Override
  public void onPause() {
    super.onPause();
    MobclickAgent.onPageEnd(
        "MyTaskFragment"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
  }

  @Override
  public void onStop() {
    super.onStop();
    canClick = true;
  }

}
