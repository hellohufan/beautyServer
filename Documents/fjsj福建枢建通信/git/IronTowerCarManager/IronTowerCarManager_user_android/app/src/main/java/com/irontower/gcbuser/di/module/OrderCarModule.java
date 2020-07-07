package com.irontower.gcbuser.di.module;

import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.OrderCarContract;
import com.irontower.gcbuser.mvp.model.OrderCarModel;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;


@Module
public class OrderCarModule {

  private OrderCarContract.View view;

  /**
   * 构建OrderCarModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public OrderCarModule(OrderCarContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  OrderCarContract.View provideOrderCarView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  OrderCarContract.Model provideOrderCarModel(OrderCarModel model) {
    return model;
  }


  @ActivityScope
  @Provides
  MaterialDialog provideMaterialDialog() {
    return new MaterialDialog.Builder(view.getActivity())
        .content(R.string.please_wait)
        .progress(true, 0).build()
        ;
  }
}