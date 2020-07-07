package com.irontower.gcbdriver.di.module;

import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.BasicInfoContract;
import com.irontower.gcbdriver.mvp.model.BasicInfoModel;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Module;
import dagger.Provides;


@Module
public class BasicInfoModule {

  private BasicInfoContract.View view;

  /**
   * 构建BasicInfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public BasicInfoModule(BasicInfoContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  BasicInfoContract.View provideBasicInfoView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  BasicInfoContract.Model provideBasicInfoModel(BasicInfoModel model) {
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
        .content(R.string.please_wait)
        .progress(true, 0)
        .show();
  }
}