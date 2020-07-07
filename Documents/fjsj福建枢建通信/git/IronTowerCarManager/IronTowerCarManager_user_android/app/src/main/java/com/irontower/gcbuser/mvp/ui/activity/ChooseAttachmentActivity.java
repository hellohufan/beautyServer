package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.blankj.utilcode.util.FileUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.app.utils.DateUtil;
import com.irontower.gcbuser.di.component.DaggerChooseAttachmentComponent;
import com.irontower.gcbuser.di.module.ChooseAttachmentModule;
import com.irontower.gcbuser.mvp.contract.ChooseAttachmentContract.View;
import com.irontower.gcbuser.mvp.presenter.ChooseAttachmentPresenter;
import com.irontower.gcbuser.mvp.ui.adapter.FileAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.simple.eventbus.EventBus;


public class ChooseAttachmentActivity extends BaseActivity<ChooseAttachmentPresenter> implements
    View {


  @BindView(R.id.tvLeadingName)
  TextView tvLeadingName;
  @BindView(R.id.tvLeadingTime)
  TextView tvLeadingTime;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;

  @Inject
  List<File> fileList;
  @Inject
  LayoutManager mLayoutManager;
  @BindView(R.id.layBack)
  LinearLayout layBack;
  @Inject
  FileAdapter adapter;


  private File file = Environment.getExternalStorageDirectory();
  private File originalFile = file;
  private Comparator<File> fileComparator = new Comparator<File>() {
    @Override
    public int compare(File o1, File o2) {
      if (o1.isDirectory() && o2.isFile()) {
        return -1;
      }
      if (o1.isFile() && o2.isDirectory()) {
        return 1;
      }
      return o1.getName().compareTo(o2.getName());

    }


  };
  private FileFilter fileFilter = new FileFilter() {
    @Override
    public boolean accept(File pathname) {
      return !pathname.getName().startsWith(".");

    }
  };

  @Override
  public void setupActivityComponent(@NonNull AppComponent appComponent) {
    DaggerChooseAttachmentComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .chooseAttachmentModule(new ChooseAttachmentModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(@Nullable Bundle savedInstanceState) {
    return R.layout.activity_choose_attachment; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(@Nullable Bundle savedInstanceState) {

    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setHasFixedSize(true);

    recyclerView.setAdapter(adapter);
    filterAndSort();

    adapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, android.view.View view, int position) {
        if (position < adapter.getData().size()) {
          File file1 = (File) adapter.getData().get(position);
          if (file1.isDirectory()) {
            file = file1;

            filterAndSort();

          } else {
            Map<String, Object> map = new HashMap<>();
            map.put("name", file1.getName());
            map.put("url", file1.getAbsolutePath());

            EventBus.getDefault().post(map, EventBusTags.CHOOSEATTACHMENT);
            finish();
          }
        }

      }
    });


  }

  private void filterAndSort() {
    fileList.clear();
    List<File> files = FileUtils.listFilesInDirWithFilter(file, fileFilter);
    Collections.sort(files, fileComparator);
    fileList.addAll(files);
    adapter.notifyDataSetChanged();

    if (file.getParentFile() == null || file.getAbsolutePath()
        .equals(originalFile.getAbsolutePath())) {
      layBack.setVisibility(android.view.View.GONE);
    } else {
      layBack.setVisibility(android.view.View.VISIBLE);
    }
    tvLeadingName.setText(file.getName());
    tvLeadingTime.setText(DateUtil.format(DateUtil.DEFAULT_FMT, new Date(file.lastModified())));

  }

  @Override
  public void showLoading() {

  }

  @Override
  public void hideLoading() {

  }

  @Override
  public void showMessage(@NonNull String message) {
    checkNotNull(message);
    ArmsUtils.snackbarText(message);
  }

  @Override
  public void launchActivity(@NonNull Intent intent) {
    checkNotNull(intent);
    ArmsUtils.startActivity(intent);
  }

  @Override
  public void killMyself() {
    finish();
  }


  @OnClick(R.id.layBack)
  public void onViewClicked() {
    file = file.getParentFile();
    filterAndSort();
  }

  @Override
  public Activity getActivity() {
    return this;
  }


}
