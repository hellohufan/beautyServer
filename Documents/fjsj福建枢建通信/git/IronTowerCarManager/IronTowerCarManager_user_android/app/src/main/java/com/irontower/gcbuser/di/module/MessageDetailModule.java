package com.irontower.gcbuser.di.module;

import com.jess.arms.di.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

import com.irontower.gcbuser.mvp.contract.MessageDetailContract;
import com.irontower.gcbuser.mvp.model.MessageDetailModel;


@Module
public class MessageDetailModule {

  private MessageDetailContract.View view;

  /**
   * 构建MessageDetailModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public MessageDetailModule(MessageDetailContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  MessageDetailContract.View provideMessageDetailView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  MessageDetailContract.Model provideMessageDetailModel(MessageDetailModel model) {
    return model;
  }
}