package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.ManageDriverContract;
import com.irontower.gcbuser.mvp.model.ManageDriverModel;
import com.irontower.gcbuser.mvp.model.entity.Driver;
import com.irontower.gcbuser.mvp.ui.adapter.ManageDriverAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class ManageDriverModule {

  private ManageDriverContract.View view;

  /**
   * 构建ManageDriverModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ManageDriverModule(ManageDriverContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ManageDriverContract.View provideManageDriverView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ManageDriverContract.Model provideManageDriverModel(ManageDriverModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager() {
    return new LinearLayoutManager(view.getActivity());
  }

  @ActivityScope
  @Provides
  List<Driver> provideTaskList() {
    return new ArrayList<>();
  }

  @ActivityScope
  @Provides
  ManageDriverAdapter provideAdapter(List<Driver> list) {
    return new ManageDriverAdapter(R.layout.item_manage_driver, list);
  }
}