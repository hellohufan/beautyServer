package com.irontower.gcbdriver.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.irontower.gcbdriver.mvp.contract.ContactUsContract;
import com.irontower.gcbdriver.mvp.model.ContactUsModel;


@Module
public class ContactUsModule {

  private ContactUsContract.View view;

  /**
   * 构建ContactUsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ContactUsModule(ContactUsContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ContactUsContract.View provideContactUsView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ContactUsContract.Model provideContactUsModel(ContactUsModel model) {
    return model;
  }
}