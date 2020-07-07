package com.irontower.gcbuser.mvp.ui.fragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemLongClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter.RequestLoadMoreListener;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.di.component.DaggerFinishOrderComponent;
import com.irontower.gcbuser.di.module.FinishOrderModule;
import com.irontower.gcbuser.mvp.contract.FinishOrderContract;
import com.irontower.gcbuser.mvp.model.entity.Order;
import com.irontower.gcbuser.mvp.presenter.FinishOrderPresenter;
import com.irontower.gcbuser.mvp.ui.activity.OrderRecordDetailActivity;
import com.irontower.gcbuser.mvp.ui.adapter.FinishOrderAdapter;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class FinishOrderFragment extends BaseFragment<FinishOrderPresenter> implements
    FinishOrderContract.View, SwipeRefreshLayout.OnRefreshListener {

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;
  @Inject
  LayoutManager mLayoutManager;
  @Inject
  FinishOrderAdapter adapter;
  private Context context;

  public static FinishOrderFragment newInstance() {
    FinishOrderFragment fragment = new FinishOrderFragment();
    return fragment;
  }

  @Override
  public void setupFragmentComponent(AppComponent appComponent) {
    DaggerFinishOrderComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .finishOrderModule(new FinishOrderModule(this))
        .build()
        .inject(this);
  }

  @Override
  public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_finish_order, container, false);
    context = view.getContext();
    return view;
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    swipeRefreshLayout.setOnRefreshListener(this);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setHasFixedSize(true);
    adapter.setOnLoadMoreListener(new RequestLoadMoreListener() {
      @Override
      public void onLoadMoreRequested() {
        mPresenter.refresh(false);

      }
    }, recyclerView);

    adapter.setEmptyView(LayoutInflater
        .from(context)
        .inflate(R.layout.empty_my_task, (ViewGroup) recyclerView.getParent(), false));
    adapter.setOnItemClickListener((adapter, view, position) -> {
      Order multipleOrder = (Order) adapter.getData().get(position);
      Intent intent = new Intent();
      intent.putExtra("orderNo", multipleOrder.getOrderNo());
      intent.setClass(context, OrderRecordDetailActivity.class);

      startActivity(intent);

    });
    adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
        Order order = (Order) adapter.getData().get(position);
        if (order.getState() == 21 || order.getState() == 0) {
          new MaterialDialog.Builder(
              ArmsUtils.obtainAppComponentFromContext(context).appManager()
                  .getTopActivity())
              .title("提示").content("是否确认删除该订单?")
              .positiveText("确认")
              .negativeText("取消")
              .negativeColor(ArmsUtils.getColor(context, R.color.empty_color))
              .onPositive(new SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog,
                    @NonNull DialogAction which) {
//
                  mPresenter.delOrder(order.getOrderNo());


                }
              })
              .show()
          ;

        }
        return false;
      }
    });

    recyclerView.setAdapter(adapter);
    onRefresh();

  }

  /**
   * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法, 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
   * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
   *
   * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onCreate还没执行 setData里却调用了presenter的方法时,是会报空的,因为dagger注入是在onCreated方法中执行的,然后才创建的presenter
   * 如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
   */

  @Override
  public void setData(Object data) {
    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);


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

  }

  @Override
  public Fragment getFragment() {
    return this;
  }

  @Override
  public void onRefresh() {
    mPresenter.refresh(true);

  }

  @Override
  public void onResume() {
    super.onResume();
    MobclickAgent.onPageStart(
        "FinishOrderFragment"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
  }

  @Override

  public void onPause() {
    super.onPause();
    MobclickAgent.onPageEnd(
        "FinishOrderFragment"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
  }

  @Subscriber(tag = EventBusTags.DEL_ORDER, mode = ThreadMode.MAIN)
  public void delOrder(String str) {
    mPresenter.refresh(true);
  }
}
