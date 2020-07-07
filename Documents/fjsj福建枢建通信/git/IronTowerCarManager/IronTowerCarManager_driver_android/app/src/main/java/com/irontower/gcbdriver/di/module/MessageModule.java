package com.irontower.gcbdriver.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.mvp.contract.MessageContract;
import com.irontower.gcbdriver.mvp.model.MessageModel;
import com.irontower.gcbdriver.mvp.model.entity.Message;
import com.irontower.gcbdriver.mvp.ui.adapter.MessageAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class MessageModule {

  private MessageContract.View view;

  /**
   * 构建MessageModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public MessageModule(MessageContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  MessageContract.View provideMessageView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  MessageContract.Model provideMessageModel(MessageModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  List<Message> provideOrder() {
    return new ArrayList<>();
  }

  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager() {
    return new LinearLayoutManager(view.getActivity());
  }


  @ActivityScope
  @Provides
  MessageAdapter provideAdapter(List<Message> list) {
    return new MessageAdapter(R.layout.item_meesage, list);
  }

}