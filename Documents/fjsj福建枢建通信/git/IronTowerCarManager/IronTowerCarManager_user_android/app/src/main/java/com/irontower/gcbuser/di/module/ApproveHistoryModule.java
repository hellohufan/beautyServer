package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.ApproveHistoryContract;
import com.irontower.gcbuser.mvp.model.ApproveHistoryModel;
import com.irontower.gcbuser.mvp.model.entity.ApproveOrder;
import com.irontower.gcbuser.mvp.ui.adapter.ApproveHistoryAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class ApproveHistoryModule {

  private ApproveHistoryContract.View view;

  /**
   * 构建ApproveHistoryModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ApproveHistoryModule(ApproveHistoryContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ApproveHistoryContract.View provideApproveHistoryView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ApproveHistoryContract.Model provideApproveHistoryModel(ApproveHistoryModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager() {
    return new LinearLayoutManager(view.getActivity());
  }

  @ActivityScope
  @Provides
  List<ApproveOrder> provideTaskList() {
    return new ArrayList<>();
  }

  @ActivityScope
  @Provides
  ApproveHistoryAdapter provideAdapter(List<ApproveOrder> list) {
    return new ApproveHistoryAdapter(R.layout.item_approve_history, list);
  }
}