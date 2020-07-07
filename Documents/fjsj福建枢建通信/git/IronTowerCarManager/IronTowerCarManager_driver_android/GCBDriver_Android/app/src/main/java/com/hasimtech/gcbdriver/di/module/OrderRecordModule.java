package com.irontower.gcbdriver.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.OrderRecordContract;
import com.irontower.gcbdriver.mvp.model.OrderRecordModel;
import com.irontower.gcbdriver.mvp.model.entity.Order;
import com.irontower.gcbdriver.mvp.ui.adapter.OrderRecordAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class OrderRecordModule {

  private OrderRecordContract.View view;

  /**
   * 构建OrderRecordModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public OrderRecordModule(OrderRecordContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  OrderRecordContract.View provideOrderRecordView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  OrderRecordContract.Model provideOrderRecordModel(OrderRecordModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager() {
    return new LinearLayoutManager(view.getActivity());
  }

  @ActivityScope
  @Provides
  List<Order> provideTaskList() {
    return new ArrayList<>();
  }

  @ActivityScope
  @Provides
  OrderRecordAdapter provideAdapter(List<Order> list) {
    return new OrderRecordAdapter(R.layout.item_order_record, list);
  }

  @ActivityScope
  @Provides
  RxPermissions provideRxpermissions(List<Order> list) {
    return new RxPermissions(view.getActivity());
  }
}