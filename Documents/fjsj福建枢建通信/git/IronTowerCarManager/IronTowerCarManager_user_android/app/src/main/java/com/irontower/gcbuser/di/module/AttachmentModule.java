package com.irontower.gcbuser.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.AttachmentContract;
import com.irontower.gcbuser.mvp.model.AttachmentModel;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import com.irontower.gcbuser.mvp.ui.adapter.AttachmentAdapter;
import com.jess.arms.di.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;


@Module
public class AttachmentModule {

  private AttachmentContract.View view;

  /**
   * 构建AttachmentModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
   */
  public AttachmentModule(AttachmentContract.View view) {
    this.view = view;
  }

  @ActivityScope
  @Provides
  AttachmentContract.View provideAttachmentView() {
    return this.view;
  }

  @ActivityScope
  @Provides
  AttachmentContract.Model provideAttachmentModel(AttachmentModel model) {
    return model;
  }

  @ActivityScope
  @Provides
  AttachmentAdapter provideAdapter(List<Attach> list) {
    return new AttachmentAdapter(R.layout.item_attachment, list);
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
        .content("上传中...")
        .progress(true, 0)
        .build()
        ;
  }
}