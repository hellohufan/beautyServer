package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.ApproveGoOutHistoryContract;
import com.irontower.gcbuser.mvp.model.ApproveGoOutHistoryModel;
import com.irontower.gcbuser.mvp.model.entity.BegGoOut;
import com.irontower.gcbuser.mvp.ui.adapter.ApproveGoOutHistoryAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class ApproveGoOutHistoryModule {

  private ApproveGoOutHistoryContract.View view;

  /**
   * 构建ApproveGoOutHistoryModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ApproveGoOutHistoryModule(ApproveGoOutHistoryContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ApproveGoOutHistoryContract.View provideApproveGoOutHistoryView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ApproveGoOutHistoryContract.Model provideApproveGoOutHistoryModel(
      ApproveGoOutHistoryModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager() {
    return new LinearLayoutManager(view.getActivity());
  }

  @ActivityScope
  @Provides
  List<BegGoOut> provideTaskList() {
    return new ArrayList<>();
  }

  @ActivityScope
  @Provides
  ApproveGoOutHistoryAdapter provideAdapter(List<BegGoOut> list) {
    return new ApproveGoOutHistoryAdapter(R.layout.item_approve_go_out_history, list);
  }
}