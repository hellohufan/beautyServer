package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.ApproveGoOutContract;
import com.irontower.gcbuser.mvp.model.ApproveGoOutModel;
import com.irontower.gcbuser.mvp.model.entity.BegGoOut;
import com.irontower.gcbuser.mvp.ui.adapter.ApproveGoOutAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class ApproveGoOutModule {

  private ApproveGoOutContract.View view;

  /**
   * 构建ApproveGoOutModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ApproveGoOutModule(ApproveGoOutContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ApproveGoOutContract.View provideApproveGoOutView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ApproveGoOutContract.Model provideApproveGoOutModel(ApproveGoOutModel model) {
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
  ApproveGoOutAdapter provideAdapter(List<BegGoOut> list) {
    return new ApproveGoOutAdapter(R.layout.item_approve_go_out, list);
  }
}