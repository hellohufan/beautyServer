package com.irontower.gcbuser.di.module;

import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.CarTraceContract;
import com.irontower.gcbuser.mvp.model.CarTraceModel;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;


/**
 * ================================================ Description:
 * <p>
 * Created by MVPArmsTemplate on 06/12/2019 10:53
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class CarTraceModule {

  @Binds
  abstract CarTraceContract.Model bindCarTraceModel(CarTraceModel model);


  @ActivityScope
  @Provides
  static MaterialDialog provideMaterialDialog(CarTraceContract.View view) {
    return new MaterialDialog.Builder(view.getActivity())
        .content(R.string.loading)
        .progress(true, 0).build()
        ;
  }
}