package com.irontower.gcbuser.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.CarPositionMapModule;
import com.irontower.gcbuser.mvp.contract.CarPositionMapContract;

import com.jess.arms.di.scope.ActivityScope;
import com.irontower.gcbuser.mvp.ui.activity.CarPositionMapActivity;


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
@ActivityScope
@Component(modules = CarPositionMapModule.class, dependencies = AppComponent.class)
public interface CarPositionMapComponent {

  void inject(CarPositionMapActivity activity);

  @Component.Builder
  interface Builder {

    @BindsInstance
    CarPositionMapComponent.Builder view(CarPositionMapContract.View view);

    CarPositionMapComponent.Builder appComponent(AppComponent appComponent);

    CarPositionMapComponent build();
  }
}