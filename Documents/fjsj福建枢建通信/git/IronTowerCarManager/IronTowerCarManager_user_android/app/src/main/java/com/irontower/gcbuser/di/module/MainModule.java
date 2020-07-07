package com.irontower.gcbuser.di.module;

import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.MainContract;
import com.irontower.gcbuser.mvp.model.MainModel;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Module;
import dagger.Provides;


@Module
public class MainModule {

  private MainContract.View view;

  /**
   * 构建MainModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public MainModule(MainContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  MainContract.View provideMainView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  MainContract.Model provideMainModel(MainModel model) {
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