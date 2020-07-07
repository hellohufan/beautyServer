package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.ManyAttachmentContract;
import com.irontower.gcbuser.mvp.model.ManyAttachmentModel;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import com.irontower.gcbuser.mvp.ui.adapter.ManyAttachAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class ManyAttachmentModule {

  private ManyAttachmentContract.View view;

  /**
   * 构建ManyAttachmentModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public ManyAttachmentModule(ManyAttachmentContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  ManyAttachmentContract.View provideManyAttachmentView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  ManyAttachmentContract.Model provideManyAttachmentModel(ManyAttachmentModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  ManyAttachAdapter provideAdapter(List<Attach> list) {
    return new ManyAttachAdapter(R.layout.item_many_attach, list);
  }

  @ActivityScope
  @Provides
  List<Attach> provideTaskList() {

    return new ArrayList<>();
  }

  @ActivityScope
  @Provides
  RecyclerView.LayoutManager provideLayoutManager() {
    return new LinearLayoutManager(view.getActivity());
  }

  @ActivityScope
  @Provides
  MaterialDialog provideMaterialDialog() {
    return new MaterialDialog.Builder(view.getActivity())
        .content(R.string.downloading)

        .progress(true, 0).build()
        ;
  }
}