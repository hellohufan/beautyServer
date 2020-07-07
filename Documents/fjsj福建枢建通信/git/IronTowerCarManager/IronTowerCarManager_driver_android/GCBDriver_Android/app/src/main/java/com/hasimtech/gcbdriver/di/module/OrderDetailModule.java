package com.irontower.gcbdriver.di.module;

import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.OrderDetailContract;
import com.irontower.gcbdriver.mvp.model.OrderDetailModel;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Module;
import dagger.Provides;


@Module
public class OrderDetailModule {

  private OrderDetailContract.View view;

  /**
   * 构建OrderDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public OrderDetailModule(OrderDetailContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  OrderDetailContract.View provideOrderDetailView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  OrderDetailContract.Model provideOrderDetailModel(OrderDetailModel model) {
    return model;
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