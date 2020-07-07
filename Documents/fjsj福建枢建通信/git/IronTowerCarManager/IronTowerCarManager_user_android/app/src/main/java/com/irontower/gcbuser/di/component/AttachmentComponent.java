package com.irontower.gcbuser.di.component;

import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbuser.di.module.AttachmentModule;

import com.jess.arms.di.scope.ActivityScope;
import com.irontower.gcbuser.mvp.ui.activity.AttachmentActivity;

@ActivityScope
@Component(modules = AttachmentModule.class, dependencies = AppComponent.class)
public interface AttachmentComponent {

  void inject(AttachmentActivity activity);
}