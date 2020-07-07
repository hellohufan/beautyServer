package com.irontower.gcbdriver.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.MyTaskContract;
import com.irontower.gcbdriver.mvp.model.MyTaskModel;
import com.irontower.gcbdriver.mvp.model.entity.Order;
import com.irontower.gcbdriver.mvp.ui.adapter.MyTaskAdapter;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class MyTaskModule {

  private MyTaskContract.View view;

  /**
   * 构建MyTaskModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public MyTaskModule(MyTaskContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  MyTaskContract.View provideMyTaskView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  MyTaskContract.Model provideMyTaskModel(MyTaskModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  List<Order> provideOrder() {
    return new ArrayList<>();
  }

  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager() {
    return new LinearLayoutManager(view.getFragment().getActivity());
  }


  @ActivityScope
  @Provides
  MyTaskAdapter provideAdapter(List<Order> list) {
    return new MyTaskAdapter(R.layout.item_my_task, list);
  }

  @ActivityScope
  @Provides
  RxPermissions getRxPermissions() {
    return new RxPermissions(view.getFragment().getActivity());
  }
}