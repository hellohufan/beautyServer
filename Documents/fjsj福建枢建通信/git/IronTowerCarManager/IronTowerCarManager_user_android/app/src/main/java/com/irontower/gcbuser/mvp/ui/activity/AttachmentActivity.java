package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.Builder;
import com.afollestad.materialdialogs.MaterialDialog.ListCallbackSingleChoice;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemChildClickListener;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.di.component.DaggerAttachmentComponent;
import com.irontower.gcbuser.di.module.AttachmentModule;
import com.irontower.gcbuser.mvp.contract.AttachmentContract.View;
import com.irontower.gcbuser.mvp.model.entity.Attach;
import com.irontower.gcbuser.mvp.presenter.AttachmentPresenter;
import com.irontower.gcbuser.mvp.ui.adapter.AttachmentAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class AttachmentActivity extends BaseActivity<AttachmentPresenter> implements
    View {


  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @Inject
  MaterialDialog loadingDialog;

  @Inject
  AttachmentAdapter adapter;
  @Inject
  List<Attach> attachList;
  @Inject
  LayoutManager mLayoutManager;
  @BindView(R.id.scrollView)
  NestedScrollView scrollView;
  private MaterialDialog materialDialog;
  int currentIndex = 0;


  private List<Attach> getAttachList = new ArrayList<>();
  private List<Attach> realAttachList = new ArrayList<>();


  @Override
  public void setupActivityComponent(@NonNull AppComponent appComponent) {
    DaggerAttachmentComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .attachmentModule(new AttachmentModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(@Nullable Bundle savedInstanceState) {
    return R.layout.activity_attachment; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(@Nullable Bundle savedInstanceState) {
    scrollView.setFocusable(true);
    scrollView.setFocusableInTouchMode(true);
    scrollView.requestFocus();
    recyclerView.setFocusable(false);
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setHasFixedSize(true);
    adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
      @Override
      public void onItemChildClick(BaseQuickAdapter adapter, android.view.View view, int position) {
        Attach attach = (Attach) adapter.getData().get(position);
        switch (view.getId()) {
          case R.id.ivReduceAttachment:
            if (attach.isAdded()) {
              for (Attach attach1 : attachList) {
                attach1.setAdded(false);
              }
              Attach tem = new Attach(true);
              attachList.add(tem);
            } else {
              attachList.remove(adapter.getData().get(position));
            }

            adapter.notifyDataSetChanged();
            break;
          case R.id.layChooseAttachment:
            currentIndex = position;
            showDialog();
            break;

          default:
            break;
        }
      }
    });

    recyclerView.setAdapter(adapter);
    getAttachList = getIntent().getParcelableArrayListExtra("attachList");
    if (getAttachList.size() == 0) {

      attachList.add(new Attach(true));
    } else {

      for (int i = 0; i < getAttachList.size(); i++) {
        Attach attach = getAttachList.get(i);
        if (i == getAttachList.size() - 1) {
          attach.setAdded(true);
        }
        attachList.add(attach);
      }

    }
    adapter.notifyDataSetChanged();
  }

  @Override
  public void showLoading() {
    if (loadingDialog != null) {
      loadingDialog.show();
    }

  }

  @Override
  public void hideLoading() {
    if (loadingDialog != null) {
      loadingDialog.dismiss();
    }
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


  @OnClick({R.id.laySubmit})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {

      case R.id.laySubmit:
        realAttachList.clear();
        Map<String, Object> fileMap = new HashMap<>();

        for (Attach attachment : attachList) {
          if (!StringUtils.isEmpty(attachment.getAttachName())) {
            realAttachList.add(attachment);
            if (attachment.getAttachId() == null && attachment.getAttachName() != null) {
              fileMap.put(attachment.getUrl(), attachment.getAttachName());
            }

          }

        }

        if (fileMap.isEmpty()) {
          if (realAttachList.size() > 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("attachMap", fileMap);
            map.put("attachList", new ArrayList<>());
            map.put("realAttachList", realAttachList);
            EventBus.getDefault().post(map, EventBusTags.UPLOAD_FILE_SUCCESS);
            ArmsUtils.makeText(getActivity(), "上传附件成功");
            killMyself();
          } else {
            ArmsUtils.makeText(getActivity(), "附件数量不能为空");
          }

        } else {

          mPresenter.uploadFile(fileMap, realAttachList);
        }

        break;
      default:
        break;
    }
  }

  @Override
  public Activity getActivity() {
    return this;
  }

  @Subscriber(tag = EventBusTags.CHOOSEATTACHMENT, mode = ThreadMode.MAIN)
  public void getRemark(Map<String, Object> map) {

    Attach attachment = attachList.get(currentIndex);
    attachment.setAttachName((String) map.get("name"));
    attachment.setUrl((String) map.get("url"));
    attachment.setAttachId(null);
    adapter.notifyDataSetChanged();


  }

  private void showDialog() {
    if (materialDialog == null) {
      materialDialog = new Builder(this)
          .title("上传文件方式")
          .items(R.array.chooseFile)
          .itemsCallbackSingleChoice(-1, new ListCallbackSingleChoice() {
            @Override
            public boolean onSelection(MaterialDialog dialog, android.view.View itemView,
                int which, CharSequence text) {
              switch (which) {
                case 0:
                  RxPaparazzo.single(getActivity())
                      .usingCamera()
                      .subscribeOn(Schedulers.io())
                      .observeOn(AndroidSchedulers.mainThread())
                      .subscribe(response -> {

                        // See response.resultCode() doc
                        if (response.resultCode() == Activity.RESULT_OK) {
                          String path =
                              response.data().getFile().getParent() + File.separator + response
                                  .data().getFilename();
                          FileUtils.rename(response.data().getFile().getAbsolutePath(),
                              response.data().getFilename());
                          File file = new File(path);
                          if (!file.exists()) {
                            file = response.data().getFile();
                          }
                          if (!file.exists()) {
                            ArmsUtils.makeText(getActivity(),
                                getActivity().getString(R.string.unknown_error));
                            return;
                          }
                          if (!ImageUtils.isImage(response.data().getFile().getAbsolutePath())) {
                            ArmsUtils.makeText(getActivity(),
                                getActivity().getString(R.string.not_img));
                            return;
                          }

                          Attach attachment = attachList.get(currentIndex);
                          attachment.setAttachName(file.getName());
                          attachment.setUrl(file.getAbsolutePath());
                          attachment.setAttachId(null);
                          adapter.notifyDataSetChanged();

                          //启动压缩

                        } else if (response.resultCode() == Activity.RESULT_CANCELED) {

                        } else {
                          ArmsUtils.makeText(getActivity(),
                              getActivity().getString(R.string.unknown_error));
                        }
                      });

                  break;
                case 1:
                  RxPaparazzo.single(getActivity())
                      .usingGallery()
                      .subscribeOn(Schedulers.io())
                      .observeOn(AndroidSchedulers.mainThread())
                      .subscribe(response -> {
                        // See response.resultCode() doc

                        if (response.resultCode() == Activity.RESULT_OK) {
                          String path =
                              response.data().getFile().getParent() + File.separator + response
                                  .data().getFilename();
                          FileUtils.rename(response.data().getFile().getAbsolutePath(),
                              response.data().getFilename());
                          File file = new File(path);
                          if (!file.exists()) {
                            file = response.data().getFile();
                          }
                          if (!file.exists()) {
                            ArmsUtils.makeText(getActivity(),
                                getActivity().getString(R.string.unknown_error));
                            return;
                          }

                          Attach attachment = attachList.get(currentIndex);
                          attachment.setAttachName(file.getName());
                          attachment.setUrl(file.getAbsolutePath());
                          attachment.setAttachId(null);
                          adapter.notifyDataSetChanged();


                        } else if (response.resultCode() == Activity.RESULT_CANCELED) {
                        } else {
                          ArmsUtils.makeText(getActivity(),
                              getActivity().getString(R.string.unknown_error));
                        }
                      });
                  break;
                case 2:
                  Intent intent = new Intent();

                  intent.setClass(getActivity(), ChooseAttachmentActivity.class);
                  startActivity(intent);
                  break;
                default:
                  break;

              }

              return false;
            }


          })
          .negativeText("取消")
          .show();
    } else {
      materialDialog.show();
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (loadingDialog != null) {
      loadingDialog.dismiss();
    }
  }
}
