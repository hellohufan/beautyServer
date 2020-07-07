package com.irontower.gcbdriver.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.OrderRecordDetailContract;
import com.irontower.gcbdriver.mvp.model.OrderRecordDetailModel;
import com.irontower.gcbdriver.mvp.model.entity.OrderTrack;
import com.irontower.gcbdriver.mvp.ui.adapter.TimeLineAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class OrderRecordDetailModule {

  private OrderRecordDetailContract.View view;

  /**
   * 构建OrderRecordDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public OrderRecordDetailModule(OrderRecordDetailContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  OrderRecordDetailContract.View provideOrderRecordDetailView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  OrderRecordDetailContract.Model provideOrderRecordDetailModel(OrderRecordDetailModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  RxPermissions provideRxPermissions() {
    return new RxPermissions(view.getActivity());
  }

  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager() {
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
  MaterialDialog provideMaterialDialog() {
    return new MaterialDialog.Builder(view.getActivity())
        .content(R.string.loading)
        .progress(true, 0).build()
        ;
  }
}