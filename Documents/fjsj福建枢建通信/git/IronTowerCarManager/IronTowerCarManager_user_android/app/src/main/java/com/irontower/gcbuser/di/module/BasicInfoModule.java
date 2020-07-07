package com.irontower.gcbuser.di.module;

import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.BasicInfoContract;
import com.irontower.gcbuser.mvp.model.BasicInfoModel;
import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import dagger.Module;
import dagger.Provides;


@Module
public class BasicInfoModule {

  private BasicInfoContract.View view;

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
        .content(R.string.loading)
        .progress(true, 0).build()
        ;
  }
}