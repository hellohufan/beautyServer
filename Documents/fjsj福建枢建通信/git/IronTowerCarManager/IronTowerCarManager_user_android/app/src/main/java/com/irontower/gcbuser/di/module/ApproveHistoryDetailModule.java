package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.ApproveHistoryDetailContract;
import com.irontower.gcbuser.mvp.model.ApproveHistoryDetailModel;
import com.irontower.gcbuser.mvp.model.entity.OrderCar;
import com.irontower.gcbuser.mvp.model.entity.OrderTrack;
import com.irontower.gcbuser.mvp.ui.adapter.OrderCarAdapter;
import com.irontower.gcbuser.mvp.ui.adapter.TimeLineAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;


@Module
public class ApproveHistoryDetailModule {

  private ApproveHistoryDetailContract.View view;

  /**
   * 构建ApproveHistoryDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ApproveHistoryDetailModule(ApproveHistoryDetailContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ApproveHistoryDetailContract.View provideApproveHistoryDetailView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ApproveHistoryDetailContract.Model provideApproveHistoryDetailModel(
      ApproveHistoryDetailModel model) {
    return model;
  }

  @Named("CarDriver")
  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager() {
    return new LinearLayoutManager(view.getActivity());
  }

  @Named("TimeLine")
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
  List<OrderCar> provideTaskList2() {
    return new ArrayList<>();
  }

  @ActivityScope
  @Provides
  TimeLineAdapter provideAdapter(List<OrderTrack> list) {
    return new TimeLineAdapter(R.layout.item_time_line, list);
  }

  @ActivityScope
  @Provides
  OrderCarAdapter provideAdapter2(List<OrderCar> list) {
    return new OrderCarAdapter(R.layout.item_order_car, list);
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