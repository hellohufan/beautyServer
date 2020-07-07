package com.irontower.gcbdriver.di.module;

import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.BegGoOutDetailContract;
import com.irontower.gcbdriver.mvp.model.BegGoOutDetailModel;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;


@Module
public class BegGoOutDetailModule {

  private BegGoOutDetailContract.View view;

  /**
   * 构建BegGoOutDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public BegGoOutDetailModule(BegGoOutDetailContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  BegGoOutDetailContract.View provideBegGoOutDetailView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  BegGoOutDetailContract.Model provideBegGoOutDetailModel(BegGoOutDetailModel model) {
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