package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.ChooseVehicleContract;
import com.irontower.gcbuser.mvp.model.ChooseVehicleModel;
import com.irontower.gcbuser.mvp.model.entity.Vehicle;
import com.irontower.gcbuser.mvp.ui.adapter.ChooseVehicleAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class ChooseVehicleModule {

  private ChooseVehicleContract.View view;

  /**
   * 构建ChooseVehicleModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ChooseVehicleModule(ChooseVehicleContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ChooseVehicleContract.View provideChooseVehicleView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ChooseVehicleContract.Model provideChooseVehicleModel(ChooseVehicleModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager() {
    return new LinearLayoutManager(view.getActivity());
  }

  @ActivityScope
  @Provides
  List<Vehicle> provideTaskList() {
    return new ArrayList<>();
  }

  @ActivityScope
  @Provides
  ChooseVehicleAdapter provideAdapter(List<Vehicle> list) {
    return new ChooseVehicleAdapter(R.layout.item_choose_vehicle, list);
  }
}