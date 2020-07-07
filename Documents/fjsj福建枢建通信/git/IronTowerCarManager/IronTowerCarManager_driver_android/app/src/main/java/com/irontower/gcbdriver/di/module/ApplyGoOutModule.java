package com.irontower.gcbdriver.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.ApplyGoOutContract;
import com.irontower.gcbdriver.mvp.model.ApplyGoOutModel;
import com.irontower.gcbdriver.mvp.model.entity.BegGoOut;
import com.irontower.gcbdriver.mvp.ui.adapter.ApplyGoOutAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class ApplyGoOutModule {

  private ApplyGoOutContract.View view;

  /**
   * 构建ApplyGoOutModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ApplyGoOutModule(ApplyGoOutContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ApplyGoOutContract.View provideApplyGoOutView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ApplyGoOutContract.Model provideApplyGoOutModel(ApplyGoOutModel model) {
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
  ApplyGoOutAdapter provideAdapter(List<BegGoOut> list) {
    return new ApplyGoOutAdapter(R.layout.item_apply_go_out, list);
  }
}