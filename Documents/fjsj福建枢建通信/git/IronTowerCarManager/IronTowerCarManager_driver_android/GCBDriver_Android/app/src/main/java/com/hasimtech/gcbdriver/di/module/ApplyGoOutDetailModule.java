package com.irontower.gcbdriver.di.module;

import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.ApplyGoOutDetailContract;
import com.irontower.gcbdriver.mvp.model.ApplyGoOutDetailModel;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;


@Module
public class ApplyGoOutDetailModule {

  private ApplyGoOutDetailContract.View view;

  /**
   * 构建ApplyGoOutDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ApplyGoOutDetailModule(ApplyGoOutDetailContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ApplyGoOutDetailContract.View provideApplyGoOutDetailView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ApplyGoOutDetailContract.Model provideApplyGoOutDetailModel(ApplyGoOutDetailModel model) {
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