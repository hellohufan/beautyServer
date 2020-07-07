package com.irontower.gcbdriver.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.KilometerNumberContract;
import com.irontower.gcbdriver.mvp.model.KilometerNumberModel;
import com.irontower.gcbdriver.mvp.model.entity.ChartData;
import com.irontower.gcbdriver.mvp.ui.adapter.KilomimeterAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class KilometerNumberModule {

  private KilometerNumberContract.View view;

  /**
   * 构建KilometerNumberModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public KilometerNumberModule(KilometerNumberContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  KilometerNumberContract.View provideKilometerNumberView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  KilometerNumberContract.Model provideKilometerNumberModel(KilometerNumberModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager() {
    return new LinearLayoutManager(view.getFragment().getActivity());
  }

  @ActivityScope
  @Provides
  List<ChartData> provideTaskList() {
    return new ArrayList<>();
  }


  @ActivityScope
  @Provides
  KilomimeterAdapter provideAdapter(List<ChartData> list) {
    return new KilomimeterAdapter(R.layout.item_kilometer, list);
  }
}