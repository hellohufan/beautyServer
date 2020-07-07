package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.ChooseDriverContract;
import com.irontower.gcbuser.mvp.model.ChooseDriverModel;
import com.irontower.gcbuser.mvp.model.entity.Driver;
import com.irontower.gcbuser.mvp.ui.adapter.ChooseDriverAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class ChooseDriverModule {

  private ChooseDriverContract.View view;

  /**
   * 构建ChooseDriverModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ChooseDriverModule(ChooseDriverContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ChooseDriverContract.View provideChooseDriverView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ChooseDriverContract.Model provideChooseDriverModel(ChooseDriverModel model) {
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
  ChooseDriverAdapter provideAdapter(List<Driver> list) {
    return new ChooseDriverAdapter(R.layout.item_choose_driver, list);
  }

  @ActivityScope
  @Provides
  MaterialDialog provideMaterialDialog() {
    return new MaterialDialog.Builder(view.getActivity())
        .content("加载中")
        .progress(true, 0).build()
        ;
  }
}