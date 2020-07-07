package com.irontower.gcbdriver.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.ApplyBegOffContract;
import com.irontower.gcbdriver.mvp.model.ApplyBegOffModel;
import com.irontower.gcbdriver.mvp.model.entity.BegOff;
import com.irontower.gcbdriver.mvp.ui.adapter.ApplyBegOffAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class ApplyBegOffModule {

  private ApplyBegOffContract.View view;

  /**
   * 构建ApplyBegOffModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ApplyBegOffModule(ApplyBegOffContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ApplyBegOffContract.View provideApplyBegOffView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ApplyBegOffContract.Model provideApplyBegOffModel(ApplyBegOffModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  List<BegOff> provideBegOff() {
    return new ArrayList<>();
  }


  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideManager() {
    return new LinearLayoutManager(view.getActivity());
  }

  @ActivityScope
  @Provides
  ApplyBegOffAdapter provideAdapter(List<BegOff> begOffs) {
    return new ApplyBegOffAdapter(R.layout.item_apply_beg_off, begOffs);
  }
}