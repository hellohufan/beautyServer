package com.irontower.gcbuser.di.module;

import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.CarPositionMapContract;
import com.irontower.gcbuser.mvp.model.CarPositionMapModel;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;


/**
 * ================================================ Description:
 * <p>
 * Created by MVPArmsTemplate on 06/15/2019 14:26
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
public abstract class CarPositionMapModule {

  @Binds
  abstract CarPositionMapContract.Model bindCarPositionMapModel(CarPositionMapModel model);

  @ActivityScope
  @Provides
  static MaterialDialog provideMaterialDialog(CarPositionMapContract.View view) {
    return new MaterialDialog.Builder(view.getActivity())
        .content(R.string.loading)
        .progress(true, 0).build()
        ;
  }
}