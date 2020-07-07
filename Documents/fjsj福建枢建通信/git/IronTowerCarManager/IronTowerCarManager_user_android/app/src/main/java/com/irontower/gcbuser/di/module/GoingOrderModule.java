package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.GoingOrderContract;
import com.irontower.gcbuser.mvp.model.GoingOrderModel;
import com.irontower.gcbuser.mvp.model.entity.Order;
import com.irontower.gcbuser.mvp.ui.adapter.GoingOrderAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class GoingOrderModule {

  private GoingOrderContract.View view;

  /**
   * 构建GoingOrderModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public GoingOrderModule(GoingOrderContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  GoingOrderContract.View provideGoingOrderView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  GoingOrderContract.Model provideGoingOrderModel(GoingOrderModel model) {
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
  GoingOrderAdapter provideAdapter(List<Order> list) {
    return new GoingOrderAdapter(R.layout.item_order_record_waiting, list);
  }
}