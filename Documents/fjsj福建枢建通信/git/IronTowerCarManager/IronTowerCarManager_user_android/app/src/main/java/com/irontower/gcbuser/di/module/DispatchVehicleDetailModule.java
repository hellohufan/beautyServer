package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.DispatchVehicleDetailContract;
import com.irontower.gcbuser.mvp.model.DispatchVehicleDetailModel;
import com.irontower.gcbuser.mvp.model.entity.OrderCar;
import com.irontower.gcbuser.mvp.model.entity.OrderTrack;
import com.irontower.gcbuser.mvp.ui.adapter.OrderVehicleAdapter;
import com.irontower.gcbuser.mvp.ui.adapter.TimeLineAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;


@Module
public class DispatchVehicleDetailModule {

  private DispatchVehicleDetailContract.View view;

  /**
   * 构建DispatchVehicleDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public DispatchVehicleDetailModule(DispatchVehicleDetailContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  DispatchVehicleDetailContract.View provideDispatchVehicleDetailView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  DispatchVehicleDetailContract.Model provideDispatchVehicleDetailModel(
      DispatchVehicleDetailModel model) {
    return model;
  }

  @Named("OrderVehicle")
  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager() {
    return new LinearLayoutManager(view.getActivity());
  }

  @Named("TimeLine")
  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager2() {
    return new LinearLayoutManager(view.getActivity());
  }

  @ActivityScope
  @Provides
  List<OrderTrack> provideTaskList() {
    return new ArrayList<>();
  }

  @ActivityScope
  @Provides
  List<OrderCar> provideCarDriverList() {
    return new ArrayList<>();
  }


  @ActivityScope
  @Provides
  TimeLineAdapter provideAdapter(List<OrderTrack> list) {
    return new TimeLineAdapter(R.layout.item_time_line, list);
  }

  @ActivityScope
  @Provides
  OrderVehicleAdapter provideCarDriverAdapter(List<OrderCar> list) {
    return new OrderVehicleAdapter(R.layout.item_order_vehicle, list);
  }

  @ActivityScope
  @Provides
  RxPermissions provideRxPermissions() {
    return new RxPermissions(view.getActivity());
  }

  @ActivityScope
  @Provides
  MaterialDialog provideMaterialDialog() {
    return new MaterialDialog.Builder(view.getActivity())
        .content(R.string.loading)
        .progress(true, 0).build()
        ;
  }
}