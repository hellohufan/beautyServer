package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.ApproveUseVehicleContract;
import com.irontower.gcbuser.mvp.model.ApproveUseVehicleModel;
import com.irontower.gcbuser.mvp.model.entity.ApproveOrder;
import com.irontower.gcbuser.mvp.ui.adapter.ApproveUseVehicleAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class ApproveUseVehicleModule {

  private ApproveUseVehicleContract.View view;

  /**
   * 构建ApproveCarModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ApproveUseVehicleModule(ApproveUseVehicleContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ApproveUseVehicleContract.View provideApproveCarView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ApproveUseVehicleContract.Model provideApproveCarModel(ApproveUseVehicleModel model) {
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
  ApproveUseVehicleAdapter provideAdapter(List<ApproveOrder> list) {
    return new ApproveUseVehicleAdapter(R.layout.item_use_vehicle, list);
  }
}