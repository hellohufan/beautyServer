package com.irontower.gcbdriver.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.WaitingOrderContract;
import com.irontower.gcbdriver.mvp.model.WaitingOrderModel;
import com.irontower.gcbdriver.mvp.model.entity.Order;
import com.irontower.gcbdriver.mvp.ui.adapter.WaitingOrderAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class WaitingOrderModule {

  private WaitingOrderContract.View view;

  /**
   * 构建WaitingOrderModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public WaitingOrderModule(WaitingOrderContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  WaitingOrderContract.View provideWaitingOrderView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  WaitingOrderContract.Model provideWaitingOrderModel(WaitingOrderModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager() {
    return new LinearLayoutManager(view.getFragment().getActivity());
  }

  @ActivityScope
  @Provides
  List<Order> provideTaskList() {
    return new ArrayList<>();
  }

  @ActivityScope
  @Provides
  WaitingOrderAdapter provideAdapter(List<Order> list) {
    return new WaitingOrderAdapter(R.layout.item_waiting_order, list);
  }

  @ActivityScope
  @Provides
  RxPermissions provideRxPermissions() {
    return new RxPermissions(view.getFragment().getActivity());
  }

}