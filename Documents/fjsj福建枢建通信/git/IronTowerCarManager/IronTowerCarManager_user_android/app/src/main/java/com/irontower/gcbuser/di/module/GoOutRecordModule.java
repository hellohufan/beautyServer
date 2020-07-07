package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.GoOutRecordContract;
import com.irontower.gcbuser.mvp.model.GoOutRecordModel;
import com.irontower.gcbuser.mvp.model.entity.BegGoOut;
import com.irontower.gcbuser.mvp.ui.adapter.GoOutRecordAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class GoOutRecordModule {

  private GoOutRecordContract.View view;

  /**
   * 构建GoOutRecordModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public GoOutRecordModule(GoOutRecordContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  GoOutRecordContract.View provideGoOutRecordView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  GoOutRecordContract.Model provideGoOutRecordModel(GoOutRecordModel model) {
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
  GoOutRecordAdapter provideAdapter(List<BegGoOut> list) {
    return new GoOutRecordAdapter(R.layout.item_go_out_record, list);
  }
}