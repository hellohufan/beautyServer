package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.UseCarMoneyContract;
import com.irontower.gcbuser.mvp.model.UseCarMoneyModel;
import com.irontower.gcbuser.mvp.model.entity.ChartData;
import com.irontower.gcbuser.mvp.ui.adapter.UseCarMoneyAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class UseCarMoneyModule {

  private UseCarMoneyContract.View view;

  /**
   * 构建UseCarMoneyModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public UseCarMoneyModule(UseCarMoneyContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  UseCarMoneyContract.View provideUseCarMoneyView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  UseCarMoneyContract.Model provideUseCarMoneyModel(UseCarMoneyModel model) {
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
  UseCarMoneyAdapter provideAdapter(List<ChartData> list) {
    return new UseCarMoneyAdapter(R.layout.item_use_car_money, list);
  }
}

