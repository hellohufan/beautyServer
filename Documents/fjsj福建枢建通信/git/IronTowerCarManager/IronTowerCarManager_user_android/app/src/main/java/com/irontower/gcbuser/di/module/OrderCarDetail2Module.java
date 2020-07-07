package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.OrderCarDetail2Contract;
import com.irontower.gcbuser.mvp.model.OrderCarDetail2Model;
import com.irontower.gcbuser.mvp.model.entity.OrderTrack;
import com.irontower.gcbuser.mvp.ui.adapter.TimeLineAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class OrderCarDetail2Module {

  private OrderCarDetail2Contract.View view;

  /**
   * 构建OrderCarDetail2Module时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public OrderCarDetail2Module(OrderCarDetail2Contract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  OrderCarDetail2Contract.View provideOrderCarDetail2View() {
    return this.view;
  }

  @ActivityScope
  @Provides
  OrderCarDetail2Contract.Model provideOrderCarDetail2Model(OrderCarDetail2Model model) {
    return model;
  }

  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager2() {
    return new LinearLayoutManager(view.getActivity());
  }

  @ActivityScope
  @Provides
  List<OrderTrack> provideTaskList() {
    return new ArrayList<>();
  }

  @ActivityScope
  @Provides
  TimeLineAdapter provideAdapter(List<OrderTrack> list) {
    return new TimeLineAdapter(R.layout.item_time_line, list);
  }

  @ActivityScope
  @Provides
  RxPermissions provideRxPermissions() {
    return new RxPermissions(view.getActivity());
  }

  @ActivityScope
  @Provides
  MaterialDialog provideMaterialDialog() {
    return new MaterialDialog.Builder(view.getActivity())
        .content(R.string.loading)
        .progress(true, 0).build()
        ;
  }
}