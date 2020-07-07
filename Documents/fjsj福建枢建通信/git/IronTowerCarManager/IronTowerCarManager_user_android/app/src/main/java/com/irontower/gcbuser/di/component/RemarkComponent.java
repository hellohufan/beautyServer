package com.irontower.gcbuser.di.component;

import com.irontower.gcbuser.di.module.RemarkModule;
import com.irontower.gcbuser.mvp.ui.activity.RemarkActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;


@ActivityScope
@Component(modules = RemarkModule.class, dependencies = AppComponent.class)
public interface RemarkComponent {

  void inject(RemarkActivity activity);


}