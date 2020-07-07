package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.DispatchVehicleHistoryContract;
import com.irontower.gcbuser.mvp.model.DispatchVehicleHistoryModel;
import com.irontower.gcbuser.mvp.model.entity.ApproveOrder;
import com.irontower.gcbuser.mvp.ui.adapter.DispatchVehicleHistoryAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class DispatchVehicleHistoryModule {

  private DispatchVehicleHistoryContract.View view;

  /**
   * 构建DispatchVehicleHistoryModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public DispatchVehicleHistoryModule(DispatchVehicleHistoryContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  DispatchVehicleHistoryContract.View provideDispatchVehicleHistoryView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  DispatchVehicleHistoryContract.Model provideDispatchVehicleHistoryModel(
      DispatchVehicleHistoryModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager() {
    return new LinearLayoutManager(view.getActivity());
  }

  @ActivityScope
  @Provides
  List<ApproveOrder> provideTaskList() {
    return new ArrayList<>();
  }

  @ActivityScope
  @Provides
  DispatchVehicleHistoryAdapter provideAdapter(List<ApproveOrder> list) {
    return new DispatchVehicleHistoryAdapter(R.layout.item_dispatch_vehicle_history, list);
  }
}