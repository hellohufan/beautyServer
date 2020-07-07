package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.ApproveBegOffContract;
import com.irontower.gcbuser.mvp.model.ApproveBegOffModel;
import com.irontower.gcbuser.mvp.model.entity.BegOff;
import com.irontower.gcbuser.mvp.ui.adapter.ApproveBegOffAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class ApproveBegOffModule {

  private ApproveBegOffContract.View view;

  /**
   * 构建ApproveBegOffModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ApproveBegOffModule(ApproveBegOffContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ApproveBegOffContract.View provideApproveBegOffView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ApproveBegOffContract.Model provideApproveBegOffModel(ApproveBegOffModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager() {
    return new LinearLayoutManager(view.getActivity());
  }

  @ActivityScope
  @Provides
  List<BegOff> provideTaskList() {
    return new ArrayList<>();
  }

  @ActivityScope
  @Provides
  ApproveBegOffAdapter provideAdapter(List<BegOff> list) {
    return new ApproveBegOffAdapter(R.layout.item_approve_beg_off, list);
  }
}