package com.irontower.gcbdriver.di.module;

import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.ApplyBegOffDetailContract;
import com.irontower.gcbdriver.mvp.model.ApplyBegOffDetailModel;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;


@Module
public class ApplyBegOffDetailModule {

  private ApplyBegOffDetailContract.View view;

  /**
   * 构建ApplyBegOffDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ApplyBegOffDetailModule(ApplyBegOffDetailContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ApplyBegOffDetailContract.View provideApplyBegOffDetailView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ApplyBegOffDetailContract.Model provideApplyBegOffDetailModel(ApplyBegOffDetailModel model) {
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