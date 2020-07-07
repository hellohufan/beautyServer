package com.irontower.gcbuser.di.component;

import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ChooseAttachmentModule;

import com.jess.arms.di.scope.ActivityScope;
import com.irontower.gcbuser.mvp.ui.activity.ChooseAttachmentActivity;

@ActivityScope
@Component(modules = ChooseAttachmentModule.class, dependencies = AppComponent.class)
public interface ChooseAttachmentComponent {

  void inject(ChooseAttachmentActivity activity);
}