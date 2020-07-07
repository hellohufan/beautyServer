package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.GoBackRecordContract;
import com.irontower.gcbuser.mvp.model.GoBackRecordModel;
import com.irontower.gcbuser.mvp.model.entity.BegGoOut;
import com.irontower.gcbuser.mvp.ui.adapter.GoBackRecordAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class GoBackRecordModule {

  private GoBackRecordContract.View view;

  /**
   * 构建GoBackRecordModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public GoBackRecordModule(GoBackRecordContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  GoBackRecordContract.View provideGoBackRecordView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  GoBackRecordContract.Model provideGoBackRecordModel(GoBackRecordModel model) {
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
  GoBackRecordAdapter provideAdapter(List<BegGoOut> list) {
    return new GoBackRecordAdapter(R.layout.item_go_out_record, list);
  }
}