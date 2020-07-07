package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.ManageCarContract;
import com.irontower.gcbuser.mvp.model.ManageCarModel;
import com.irontower.gcbuser.mvp.model.entity.Car;
import com.irontower.gcbuser.mvp.ui.adapter.ManageCarAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class ManageCarModule {

  private ManageCarContract.View view;

  /**
   * 构建ManageCarModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ManageCarModule(ManageCarContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ManageCarContract.View provideManageCarView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ManageCarContract.Model provideManageCarModel(ManageCarModel model) {
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
  ManageCarAdapter provideAdapter(List<Car> list) {
    return new ManageCarAdapter(R.layout.item_manage_car, list);
  }
}