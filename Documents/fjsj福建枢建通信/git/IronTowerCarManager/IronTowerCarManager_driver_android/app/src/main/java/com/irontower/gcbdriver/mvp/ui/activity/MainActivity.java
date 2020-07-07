package com.irontower.gcbdriver.mvp.ui.activity;


import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.blankj.utilcode.util.SPUtils;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.app.EventBusTags;
import com.irontower.gcbdriver.app.utils.AppUtil;
import com.irontower.gcbdriver.app.utils.NotificationsUtils;
import com.irontower.gcbdriver.di.component.DaggerMainComponent;
import com.irontower.gcbdriver.di.module.MainModule;
import com.irontower.gcbdriver.mvp.contract.MainContract.View;
import com.irontower.gcbdriver.mvp.model.api.Api;
import com.irontower.gcbdriver.mvp.model.entity.User;
import com.irontower.gcbdriver.mvp.presenter.MainPresenter;
import com.irontower.gcbdriver.mvp.ui.fragment.MyTaskFragment;
import com.irontower.gcbdriver.mvp.ui.fragment.WaitingOrderFragment;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import de.hdodenhof.circleimageview.CircleImageView;
import javax.inject.Inject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class MainActivity extends BaseActivity<MainPresenter> implements View {

  @BindView(R.id.toolBar)
  Toolbar toolbar;
  @BindView(R.id.drawer_layout)
  DrawerLayout drawerLayout;

  @BindView(R.id.layMyTask)
  RelativeLayout layMyTask;
  @BindView(R.id.layWaitingOrder)
  RelativeLayout layWaitingOrder;
  @BindView(R.id.tvMyTask)
  TextView tvMyTask;
  @BindView(R.id.tvWaitingOrder)
  TextView tvWaitingOrder;
  @BindView(R.id.ivUserImage)
  CircleImageView ivUserImage;
  @BindView(R.id.tvName)
  TextView tvName;
  @Inject
  RxPermissions mRxPermissions;
  @Inject
  ImageLoader imageLoader;
  private FragmentManager fragmentManager;
  private int index = 1;
  private MaterialDialog exitAppDialog;
  private MaterialDialog chooseImageDialog;
  @Inject
  MaterialDialog materialDialog;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerMainComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .mainModule(new MainModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0


  }

  @Override
  public void initData(Bundle savedInstanceState) {
    mPresenter.getDicts();
    User user = AppUtil.getUser();
    if (user != null) {
      tvName.setText(user.getUserName());
      GlideArms.with(getActivity()).load(Api.APP_DOMAIN + user.getHeadUrl())
          .placeholder(R.mipmap.user_photo)
          .error(R.mipmap.user_photo).into(ivUserImage);
    }

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);

    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawerLayout, toolbar, R.string.start,
        R.string.end);
    toggle.syncState();
    toolbar.setNavigationIcon(R.mipmap.user_icon);
    drawerLayout.addDrawerListener(toggle);

    fragmentManager = getSupportFragmentManager();
    setTabSelection(1);
    mPresenter.checkAppUpdate();
    if (!NotificationsUtils.isNotificationEnabled(this)) {
      new MaterialDialog.Builder(
          getActivity())
          .title("提示").content("尚未开启通知栏权限,是否开启?")
          .positiveText("确认")
          .negativeText("取消")
          .negativeColor(ArmsUtils.getColor(getActivity(), R.color.empty_color))
          .onPositive(new SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog,
                @NonNull DialogAction which) {
              Intent localIntent = new Intent();
              localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              if (Build.VERSION.SDK_INT >= 9) {
                localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                localIntent
                    .setData(Uri.fromParts("package", getActivity().getPackageName(), null));
              } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.setAction(Intent.ACTION_VIEW);

                localIntent.setClassName("com.android.settings",
                    "com.android.settings.InstalledAppDetails");

                localIntent.putExtra("com.android.settings.ApplicationPkgName",
                    getActivity().getPackageName());
              }
              startActivity(localIntent);
            }
          })
          .show()
      ;


    }


  }

  private void setTabSelection(int i) {
    FragmentTransaction transaction = fragmentManager.beginTransaction();

    // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
    reset(transaction);
    switch (i) {
      case 1:
        Fragment myTaskFragment = getSupportFragmentManager().findFragmentByTag("MyTaskFragment");
        if (myTaskFragment == null) {
          myTaskFragment = MyTaskFragment.newInstance();
          transaction.add(R.id.framelayout, myTaskFragment, "MyTaskFragment");
        }
        tvMyTask.setTextColor(ArmsUtils.getColor(this, R.color.mainColor));
        tvWaitingOrder.setTextColor(ArmsUtils.getColor(this, R.color.white));
        layMyTask.setBackgroundResource(R.mipmap.tab_left_focus);
        layWaitingOrder.setBackgroundResource(R.mipmap.tab_right_unfocus);
        transaction.show(myTaskFragment);

        break;
      case 2:
        Fragment waitingOrderFragment = getSupportFragmentManager()
            .findFragmentByTag("WaitingOrderFragment");

        if (waitingOrderFragment == null) {
          waitingOrderFragment = WaitingOrderFragment.newInstance();
          transaction.add(R.id.framelayout, waitingOrderFragment, "WaitingOrderFragment");
        }
        tvMyTask.setTextColor(ArmsUtils.getColor(this, R.color.white));
        tvWaitingOrder.setTextColor(ArmsUtils.getColor(this, R.color.mainColor));
        layMyTask.setBackgroundResource(R.mipmap.tab_left_unfocus);
        layWaitingOrder.setBackgroundResource(R.mipmap.tab_right_focus);
        transaction.show(waitingOrderFragment);
        break;
      default:

        break;
    }
    transaction.commit();
  }

  private void reset(FragmentTransaction transaction) {
    Fragment myTaskFragment = getSupportFragmentManager().findFragmentByTag("MyTaskFragment");
    Fragment waitingOrderFragment = getSupportFragmentManager()
        .findFragmentByTag("WaitingOrderFragment");
    if (myTaskFragment != null) {
      transaction.hide(myTaskFragment);
    }
    if (waitingOrderFragment != null) {
      transaction.hide(waitingOrderFragment);
    }


  }


  @Override
  public void showLoading() {
    materialDialog.show();


  }

  @Override
  public void hideLoading() {
    materialDialog.dismiss();

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


  @Override
  public void onBackPressed() {

    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
      drawerLayout.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }


  @Override
  public Activity getActivity() {
    return this;
  }


  @OnClick({R.id.ivUserImage, R.id.layBasicInfo, R.id.layTravelRecords, R.id.layChartRecords,
      R.id.layExitApp, R.id.layMyTask, R.id.layWaitingOrder, R.id.layApplyBegOff,
      R.id.laySystemMessage, R.id.layApplyGoOut, R.id.layOrderFeeList})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.layOrderFeeList:
        ArmsUtils.startActivity(getActivity(), OrderFeeListActivity.class);

        break;
      case R.id.layMyTask:
        if (index == 1) {
          return;
        }
        setTabSelection(1);
        index = 1;
        break;
      case R.id.layWaitingOrder:
        if (index == 2) {
          return;
        }
        setTabSelection(2);
        index = 2;
        break;
      case R.id.ivUserImage:
        ArmsUtils.startActivity(getActivity(), BasicInfoActivity.class);
        break;
      case R.id.layBasicInfo:
        ArmsUtils.startActivity(this, BasicInfoActivity.class);

        break;
      case R.id.layTravelRecords:
        ArmsUtils.startActivity(this, OrderRecordActivity.class);
        break;
      case R.id.layChartRecords:
        ArmsUtils.startActivity(this, ChartStatisticsActivity.class);
        break;
      case R.id.layApplyBegOff:
        ArmsUtils.startActivity(this, ApplyBegOffActivity.class);
        break;
      case R.id.layApplyGoOut:
        ArmsUtils.startActivity(this, ApplyGoOutActivity.class);
        break;

      case R.id.layExitApp:
        mPresenter.logout();

        if (exitAppDialog == null) {
          exitAppDialog = new MaterialDialog.Builder(this)
              .customView(R.layout.dialog_get_order, false)
              .backgroundColor(AlertDialog.THEME_HOLO_LIGHT)
              .show();
          android.view.View customView = exitAppDialog.getCustomView();
          TextView textView = customView.findViewById(R.id.tvContent);
          RelativeLayout layCancel = customView.findViewById(R.id.layCancel);
          RelativeLayout layConfirm = customView.findViewById(R.id.layConfirm);
          textView.setText(R.string.exit_app_info);
          layCancel.setOnClickListener(view1 -> exitAppDialog.dismiss());
          layConfirm.setOnClickListener(view1 -> {
            exitAppDialog.dismiss();
            JPushInterface.resumePush(getApplicationContext());
            JPushInterface.deleteAlias(getActivity(), (int) System.currentTimeMillis());

            SPUtils.getInstance().clear();
            ArmsUtils.startActivity(getActivity(), LoginActivity.class);
            ArmsUtils.killAll();

          });

        } else {
          exitAppDialog.show();
        }

        break;
      case R.id.laySystemMessage:
        ArmsUtils.startActivity(getActivity(), MessageActivity.class);

      default:
    }
  }

  @Override
  public RxPermissions getRxPermissions() {
    return mRxPermissions;
  }

  @Override
  protected void onDestroy() {

    super.onDestroy();
    mRxPermissions = null;
    if (exitAppDialog != null) {
      exitAppDialog.dismiss();
      exitAppDialog = null;
    }

    if (chooseImageDialog != null) {
      chooseImageDialog.dismiss();
      chooseImageDialog = null;
    }

    if (materialDialog != null) {
      materialDialog.dismiss();
      materialDialog = null;

    }

  }

  @Subscriber(tag = EventBusTags.CHANGEHEADURL, mode = ThreadMode.MAIN)
  public void changeImage(String url) {
    GlideArms.with(getActivity()).load(Api.APP_DOMAIN + url)
        .placeholder(R.mipmap.user_photo)
        .error(R.mipmap.user_photo).into(ivUserImage);

  }

  @Subscriber(tag = EventBusTags.CHANGEUSERNAME, mode = ThreadMode.MAIN)
  public void changeUserName(String userName) {
    tvName.setText(userName);

  }


  @Override
  public Resources getResources() {
    Resources res = super.getResources();
    Configuration config = new Configuration();
    config.setToDefaults();
    res.updateConfiguration(config, res.getDisplayMetrics());
    return res;

  }


}
