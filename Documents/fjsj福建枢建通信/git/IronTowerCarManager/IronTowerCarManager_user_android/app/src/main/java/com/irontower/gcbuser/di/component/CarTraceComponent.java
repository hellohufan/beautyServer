package com.irontower.gcbuser.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.CarTraceModule;
import com.irontower.gcbuser.mvp.contract.CarTraceContract;

import com.jess.arms.di.scope.ActivityScope;
import com.irontower.gcbuser.mvp.ui.activity.CarTraceActivity;


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
@ActivityScope
@Component(modules = CarTraceModule.class, dependencies = AppComponent.class)
public interface CarTraceComponent {

  void inject(CarTraceActivity activity);

  @Component.Builder
  interface Builder {

    @BindsInstance
    CarTraceComponent.Builder view(CarTraceContract.View view);

    CarTraceComponent.Builder appComponent(AppComponent appComponent);

    CarTraceComponent build();
  }
}