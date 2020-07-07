package com.irontower.gcbuser.di.component;

import dagger.BindsInstance;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.TraceViewModule;
import com.irontower.gcbuser.mvp.contract.TraceViewContract;

import com.jess.arms.di.scope.ActivityScope;
import com.irontower.gcbuser.mvp.ui.activity.TraceViewActivity;


/**
 * ================================================ Description:
 * <p>
 * Created by MVPArmsTemplate on 06/12/2019 22:13
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = TraceViewModule.class, dependencies = AppComponent.class)
public interface TraceViewComponent {

  void inject(TraceViewActivity activity);

  @Component.Builder
  interface Builder {

    @BindsInstance
    TraceViewComponent.Builder view(TraceViewContract.View view);

    TraceViewComponent.Builder appComponent(AppComponent appComponent);

    TraceViewComponent build();
  }
}