package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.ApproveDetailContract;
import com.irontower.gcbuser.mvp.model.ApproveDetailModel;
import com.irontower.gcbuser.mvp.model.entity.CarDriver;
import com.irontower.gcbuser.mvp.model.entity.OrderTrack;
import com.irontower.gcbuser.mvp.ui.adapter.TimeLineAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class ApproveDetailModule {

  private ApproveDetailContract.View view;

  /**
   * 构建OrderDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ApproveDetailModule(ApproveDetailContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ApproveDetailContract.View provideOrderDetailView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ApproveDetailContract.Model provideOrderDetailModel(ApproveDetailModel model) {
    return model;
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
  List<CarDriver> provideTaskList2() {
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

  @ActivityScope
  @Provides
  RxPermissions provideRxPermissions() {
    return new RxPermissions(view.getActivity());
  }
}