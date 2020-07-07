package com.irontower.gcbdriver.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.DriverNumberContract;
import com.irontower.gcbdriver.mvp.model.DriverNumberModel;
import com.irontower.gcbdriver.mvp.model.entity.ChartData;
import com.irontower.gcbdriver.mvp.ui.adapter.DriveNumberAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class DriverNumberModule {

  private DriverNumberContract.View view;

  /**
   * 构建DriverNumberModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public DriverNumberModule(DriverNumberContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  DriverNumberContract.View provideDriverNumberView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  DriverNumberContract.Model provideDriverNumberModel(DriverNumberModel model) {
    return model;
  }


  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager() {
    return new LinearLayoutManager(view.getFragment().getActivity());
  }

  @ActivityScope
  @Provides
  List<ChartData> provideTaskList() {
    return new ArrayList<>();
  }

  @ActivityScope
  @Provides
  DriveNumberAdapter provideAdapter(List<ChartData> list) {
    return new DriveNumberAdapter(R.layout.item_drive_number, list);
  }
}