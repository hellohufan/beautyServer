package com.irontower.gcbuser.di.module;

import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.EvaluateContract;
import com.irontower.gcbuser.mvp.model.EvaluateModel;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;


@Module
public class EvaluateModule {

  private EvaluateContract.View view;

  /**
   * 构建EvaluateModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public EvaluateModule(EvaluateContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  EvaluateContract.View provideEvaluateView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  EvaluateContract.Model provideEvaluateModel(EvaluateModel model) {
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