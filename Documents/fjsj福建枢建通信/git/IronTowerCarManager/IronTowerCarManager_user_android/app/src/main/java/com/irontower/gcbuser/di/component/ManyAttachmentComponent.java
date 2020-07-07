package com.irontower.gcbuser.di.component;

import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.ManyAttachmentModule;

import com.jess.arms.di.scope.ActivityScope;
import com.irontower.gcbuser.mvp.ui.activity.ManyAttachmentActivity;

@ActivityScope
@Component(modules = ManyAttachmentModule.class, dependencies = AppComponent.class)
public interface ManyAttachmentComponent {

  void inject(ManyAttachmentActivity activity);
}