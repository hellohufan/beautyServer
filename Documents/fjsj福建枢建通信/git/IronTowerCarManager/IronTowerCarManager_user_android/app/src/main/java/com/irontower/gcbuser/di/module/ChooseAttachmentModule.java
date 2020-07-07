package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.ChooseAttachmentContract;
import com.irontower.gcbuser.mvp.model.ChooseAttachmentModel;
import com.irontower.gcbuser.mvp.ui.adapter.FileAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Module
public class ChooseAttachmentModule {

  private ChooseAttachmentContract.View view;

  /**
   * 构建ChooseAttachmentModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ChooseAttachmentModule(ChooseAttachmentContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ChooseAttachmentContract.View provideChooseAttachmentView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ChooseAttachmentContract.Model provideChooseAttachmentModel(ChooseAttachmentModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  FileAdapter provideAdapter(List<File> list) {
    return new FileAdapter(R.layout.item_file, list);
  }

  @ActivityScope
  @Provides
  List<File> provideTaskList() {

    return new ArrayList<>();
  }

  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager() {
    return new LinearLayoutManager(view.getActivity());
  }
}