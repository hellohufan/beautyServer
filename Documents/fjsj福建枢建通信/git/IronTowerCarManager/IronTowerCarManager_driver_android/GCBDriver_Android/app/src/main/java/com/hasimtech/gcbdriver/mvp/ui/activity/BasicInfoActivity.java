package com.irontower.gcbdriver.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.app.EventBusTags;
import com.irontower.gcbdriver.app.utils.AppUtil;
import com.irontower.gcbdriver.di.component.DaggerBasicInfoComponent;
import com.irontower.gcbdriver.di.module.BasicInfoModule;
import com.irontower.gcbdriver.mvp.contract.BasicInfoContract.View;
import com.irontower.gcbdriver.mvp.model.api.Api;
import com.irontower.gcbdriver.mvp.model.entity.User;
import com.irontower.gcbdriver.mvp.presenter.BasicInfoPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.GlideArms;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import de.hdodenhof.circleimageview.CircleImageView;
import javax.inject.Inject;
import org.simple.eventbus.EventBus;


public class BasicInfoActivity extends BaseActivity<BasicInfoPresenter> implements
    View {


  @BindView(R.id.ivUserImage)
  CircleImageView ivUserImage;
  @BindView(R.id.tvName)
  TextView tvName;
  @BindView(R.id.tvPhone)
  TextView tvPhone;
  @BindView(R.id.tvVersionName)
  TextView tvVersionName;
  @BindView(R.id.tvCompany)
  TextView tvCompany;
  @BindView(R.id.ivSound1)
  ImageView ivSound1;
  @BindView(R.id.ivSex)
  ImageView ivSex;
  @BindView(R.id.ivSound)
  ImageView ivSound;
  @BindView(R.id.laySound)
  RelativeLayout laySound;
  @BindView(R.id.ivNotice)
  ImageView ivNotice;
  @BindView(R.id.layNotice)
  RelativeLayout layNotice;
  @BindView(R.id.layModifyPwd)
  RelativeLayout layModifyPwd;
  @BindView(R.id.layContact)
  RelativeLayout layContact;
  @BindView(R.id.layUpdateApp)
  RelativeLayout layUpdateApp;
  @Inject
  RxPermissions rxPermissions;
  @Inject
  ImageLoader imageLoader;
  private MaterialDialog chooseImageDialog;
  @Inject
  MaterialDialog materialDialog;
  private User user;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerBasicInfoComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .basicInfoModule(new BasicInfoModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_basic_info; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {

    tvVersionName.setText("V" + AppUtils.getAppVersionName());
    if (SPUtils.getInstance().getBoolean("sound")) {
      ivSound.setImageResource(R.mipmap.open);
    } else {
      ivSound.setImageResource(R.mipmap.close);

    }
    if (JPushInterface.isPushStopped(getApplicationContext())) {
      ivNotice.setImageResource(R.mipmap.close);

    } else {
      ivNotice.setImageResource(R.mipmap.open);

    }
    user = AppUtil.getUser();
    mPresenter.getData(user.getUserNo(), user.getUserName(), user.getPhoneNo());
  }


  @Override
  public void showLoading() {
    if (!materialDialog.isShowing()) {
      materialDialog.show();

    }


  }

  @Override
  public void hideLoading() {
    if (materialDialog.isShowing()) {
      materialDialog.dismiss();
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


  @OnClick({R.id.laySound, R.id.layNotice, R.id.layModifyPwd,
      R.id.layContact, R.id.layUpdateApp, R.id.ivUserImage})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {
      case R.id.laySound:
        if (SPUtils.getInstance().getBoolean("sound")) {

          SPUtils.getInstance().put("sound", false);
          ivSound.setImageResource(R.mipmap.close);
        } else {
          SPUtils.getInstance().put("sound", true);
          ivSound.setImageResource(R.mipmap.open);
        }

        break;
      case R.id.layNotice:
        if (!JPushInterface.isPushStopped(getApplicationContext())) {
          ivNotice.setImageResource(R.mipmap.close);
          JPushInterface.stopPush(getApplicationContext());

        } else {
          ivNotice.setImageResource(R.mipmap.open);
          JPushInterface.resumePush(getApplicationContext());

        }
        break;

      case R.id.layModifyPwd:
        ArmsUtils.startActivity(getActivity(), ModifyPasswordActivity.class);
        break;
      case R.id.layContact:
        new MaterialDialog.Builder(this)
            .title("拨打客服电话")
            .content("059163509168")
            .contentColor(getResources().getColor(R.color.mainColor))
            .positiveText(R.string.confirm)
            .positiveColor(ArmsUtils.getColor(getActivity(), R.color.colorPrimary))
            .negativeColor(ArmsUtils.getColor(getActivity(), R.color.empty_color))
            .negativeText(R.string.cancel)
            .onPositive(new SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                mPresenter.callPhone();
              }
            })
            .show();

        break;
      case R.id.ivUserImage:
        if (chooseImageDialog == null) {
          chooseImageDialog = new MaterialDialog.Builder(this)
              .title("图片")
              .items(R.array.chooseImage)
              .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                @Override
                public boolean onSelection(MaterialDialog dialog, android.view.View itemView,
                    int which, CharSequence text) {
                  mPresenter.chooseImage(which, ivUserImage);
                  return false;
                }


              })
              .negativeText("取消")
              .show();
        } else {
          chooseImageDialog.show();
        }
        break;
      case R.id.layUpdateApp:
        mPresenter.checkAppUpdate();
        break;
      default:
    }
  }

  @Override
  public Activity getActivity() {
    return this;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (materialDialog != null) {
      materialDialog.dismiss();
      materialDialog = null;

    }
    if (chooseImageDialog != null) {
      chooseImageDialog = null;
    }

  }

  @Override
  public RxPermissions getRxPermissions() {
    return rxPermissions;
  }

  @Override
  public Resources getResources() {
    Resources res = super.getResources();
    Configuration config = new Configuration();
    config.setToDefaults();
    res.updateConfiguration(config, res.getDisplayMetrics());
    return res;

  }

  @Override
  public void init(User newUser) {
    tvName.setText(newUser.getUserName());
    tvPhone.setText(newUser.getPhoneNo());
    tvCompany.setText(newUser.getOrgName());
    if (!StringUtils.isEmpty(newUser.getSex())) {
      ivSex.setVisibility(android.view.View.VISIBLE);
      if ("男".equals(newUser.getSex())) {
        ivSex.setImageResource(R.mipmap.boy);
      } else {
        ivSex.setImageResource(R.mipmap.girl);

      }
    } else {
      ivSex.setVisibility(android.view.View.GONE);

    }
    GlideArms.with(getActivity()).load(Api.APP_DOMAIN + newUser.getHeadUrl())
        .placeholder(R.mipmap.user_photo)
        .error(R.mipmap.user_photo).into(ivUserImage);
    if (!StringUtils.equals(user.getUserName(), newUser.getUserName())) {
      EventBus.getDefault().post(newUser.getUserName(), EventBusTags.CHANGEUSERNAME);
    }

    if (!StringUtils.equals(user.getHeadUrl(), newUser.getHeadUrl())) {
      EventBus.getDefault().post(newUser.getHeadUrl(),
          EventBusTags.CHANGEHEADURL);
    }

  }
}
