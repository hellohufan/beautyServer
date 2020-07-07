package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.ManageDispatchVehicleContract;
import com.irontower.gcbuser.mvp.model.ManageDispatchVehicleModel;
import com.irontower.gcbuser.mvp.model.entity.ApproveOrder;
import com.irontower.gcbuser.mvp.ui.adapter.DispatchVehicleAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class ManageDispatchVehicleModule {

  private ManageDispatchVehicleContract.View view;

  /**
   * 构建ManageDispatchVehicleModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ManageDispatchVehicleModule(ManageDispatchVehicleContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ManageDispatchVehicleContract.View provideManageDispatchVehicleView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ManageDispatchVehicleContract.Model provideManageDispatchVehicleModel(
      ManageDispatchVehicleModel model) {
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
  DispatchVehicleAdapter provideAdapter(List<ApproveOrder> list) {
    return new DispatchVehicleAdapter(R.layout.item_approve_order, list);
  }
}