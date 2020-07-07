package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.FinishOrderContract;
import com.irontower.gcbuser.mvp.model.FinishOrderModel;
import com.irontower.gcbuser.mvp.model.entity.Order;
import com.irontower.gcbuser.mvp.ui.adapter.FinishOrderAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class FinishOrderModule {

  private FinishOrderContract.View view;

  /**
   * 构建FinishOrderModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public FinishOrderModule(FinishOrderContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  FinishOrderContract.View provideFinishOrderView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  FinishOrderContract.Model provideFinishOrderModel(FinishOrderModel model) {
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
  FinishOrderAdapter provideAdapter(List<Order> list) {
    return new FinishOrderAdapter(R.layout.item_order_record_finish, list);
  }
}