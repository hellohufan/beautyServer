package com.irontower.gcbuser.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.TracePayBackModule;
import com.irontower.gcbuser.mvp.contract.TracePayBackContract;

import com.jess.arms.di.scope.ActivityScope;
import com.irontower.gcbuser.mvp.ui.activity.TracePayBackActivity;


/**
 * ================================================ Description:
 * <p>
 * Created by MVPArmsTemplate on 06/15/2019 17:47
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = TracePayBackModule.class, dependencies = AppComponent.class)
public interface TracePayBackComponent {

  void inject(TracePayBackActivity activity);

  @Component.Builder
  interface Builder {

    @BindsInstance
    TracePayBackComponent.Builder view(TracePayBackContract.View view);

    TracePayBackComponent.Builder appComponent(AppComponent appComponent);

    TracePayBackComponent build();
  }
}