package com.irontower.gcbdriver.di.component;

import com.jess.arms.di.scope.ActivityScope;
import dagger.Component;
import com.jess.arms.di.component.AppComponent;

import com.irontower.gcbdriver.di.module.ContactUsModule;

import com.irontower.gcbdriver.mvp.ui.activity.ContactUsActivity;

@ActivityScope
@Component(modules = ContactUsModule.class, dependencies = AppComponent.class)
public interface ContactUsComponent {

  void inject(ContactUsActivity activity);
}