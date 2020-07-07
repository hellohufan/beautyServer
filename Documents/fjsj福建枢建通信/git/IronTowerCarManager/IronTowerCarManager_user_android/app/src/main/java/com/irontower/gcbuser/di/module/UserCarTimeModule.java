package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.UserCarTimeContract;
import com.irontower.gcbuser.mvp.model.UserCarTimeModel;
import com.irontower.gcbuser.mvp.model.entity.ChartData;
import com.irontower.gcbuser.mvp.ui.adapter.UseCarTimeAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class UserCarTimeModule {

  private UserCarTimeContract.View view;

  /**
   * 构建UserCarTimeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public UserCarTimeModule(UserCarTimeContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  UserCarTimeContract.View provideUserCarTimeView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  UserCarTimeContract.Model provideUserCarTimeModel(UserCarTimeModel model) {
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
  UseCarTimeAdapter provideAdapter(List<ChartData> list) {
    return new UseCarTimeAdapter(R.layout.item_use_car_time, list);
  }
}