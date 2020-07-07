package com.irontower.gcbdriver.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.ChooseCarContract;
import com.irontower.gcbdriver.mvp.model.ChooseCarModel;
import com.irontower.gcbdriver.mvp.model.entity.Car;
import com.irontower.gcbdriver.mvp.ui.adapter.ChooseCarAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class ChooseCarModule {

  private ChooseCarContract.View view;

  /**
   * 构建ChooseCarModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ChooseCarModule(ChooseCarContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ChooseCarContract.View provideChooseCarView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ChooseCarContract.Model provideChooseCarModel(ChooseCarModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager() {
    return new LinearLayoutManager(view.getActivity());
  }

  @ActivityScope
  @Provides
  List<Car> provideTaskList() {
    return new ArrayList<>();
  }

  @ActivityScope
  @Provides
  ChooseCarAdapter provideAdapter(List<Car> list) {
    return new ChooseCarAdapter(R.layout.item_choose_car, list);
  }
}