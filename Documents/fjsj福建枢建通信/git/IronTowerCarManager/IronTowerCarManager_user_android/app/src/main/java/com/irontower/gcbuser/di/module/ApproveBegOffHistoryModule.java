package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.ApproveBegOffHistoryContract;
import com.irontower.gcbuser.mvp.model.ApproveBegOffHistoryModel;
import com.irontower.gcbuser.mvp.model.entity.BegOff;
import com.irontower.gcbuser.mvp.ui.adapter.ApproveBegOffHistoryAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class ApproveBegOffHistoryModule {

  private ApproveBegOffHistoryContract.View view;

  /**
   * 构建ApproveBegOffHistoryModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ApproveBegOffHistoryModule(ApproveBegOffHistoryContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ApproveBegOffHistoryContract.View provideApproveBegOffHistoryView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ApproveBegOffHistoryContract.Model provideApproveBegOffHistoryModel(
      ApproveBegOffHistoryModel model) {
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
  ApproveBegOffHistoryAdapter provideAdapter(List<BegOff> list) {
    return new ApproveBegOffHistoryAdapter(R.layout.item_approve_beg_off_history, list);
  }
}