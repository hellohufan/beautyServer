package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import butterknife.BindView;
import butterknife.OnClick;
import com.blankj.utilcode.util.StringUtils;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.di.component.DaggerHelpCallCarComponent;
import com.irontower.gcbuser.di.module.HelpCallCarModule;
import com.irontower.gcbuser.mvp.contract.HelpCallCarContract.View;
import com.irontower.gcbuser.mvp.presenter.HelpCallCarPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.simple.eventbus.EventBus;


public class HelpCallCarActivity extends BaseActivity<HelpCallCarPresenter> implements
    View {

  @BindView(R.id.etName)
  AppCompatEditText etName;

  @BindView(R.id.etMobile)
  AppCompatEditText etMobile;
  @Inject
  RxPermissions rxPermissions;
  private String name, phone;


  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerHelpCallCarComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .helpCallCarModule(new HelpCallCarModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_help_call_car; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {

    etName.setText(getIntent().getStringExtra("name"));

    etMobile.setText(getIntent().getStringExtra("mobile"));
    etName.setSelection(etName.getText().length());

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


  @OnClick({R.id.layName, R.id.laySubmit})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {

      case R.id.layName:
        mPresenter.checkpermission();
        break;
      case R.id.laySubmit:
        if (etName.getText().length() == 0) {
          ArmsUtils.makeText(getActivity(), getString(R.string.name_empty));
          return;
        }
        if (etMobile.getText().length() == 0) {
          ArmsUtils.makeText(getActivity(), getString(R.string.phone_empty));
          return;
        }
        if (etMobile.getText().toString().replaceAll("\\s*", "").length() != 11) {
          ArmsUtils.makeText(getActivity(), getString(R.string.phone_length));
          return;
        }
        Message message = new Message();
        Map<String, Object> map = new HashMap<>();
        map.put("name", etName.getText().toString().trim());
        map.put("mobile", etMobile.getText().toString().trim());
        message.obj = map;
        EventBus.getDefault().post(message, EventBusTags.MOBILE);
        finish();

        break;
      default:
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
      case 0:

        if (data == null) {
          return;
        }
//处理返回的data,获取选择的联系人信息
        Cursor c = managedQuery(data.getData(), null, null, null, null);
        if (c.moveToFirst()) {
          name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
          String hasPhone = c
              .getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
          String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
          if (hasPhone.equalsIgnoreCase("1")) {
            hasPhone = "true";
          } else {
            hasPhone = "false";
          }
          if (Boolean.parseBoolean(hasPhone)) {
            Cursor phones = getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                        + contactId,
                    null,
                    null);
            while (phones != null && phones.moveToNext()) {
              phone = phones
                  .getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            if (phones != null) {
              phones.close();
            }
          }


        }
        if (StringUtils.isEmpty(name)) {
          etName.setText(null);
        } else {
          etName.setText(name);
          etName.setSelection(name.length());
        }
        if (StringUtils.isEmpty(phone)) {
          etMobile.setText(null);
        } else {
          etMobile.setText(phone.trim());
          etMobile.setSelection(phone.trim().length());
        }

        break;
      default:

    }
    super.onActivityResult(requestCode, resultCode, data);


  }


  @Override
  public Activity getActivity() {
    return this;
  }

  @Override
  public RxPermissions getRxPermissions() {
    return rxPermissions;
  }

  @Override
  public void jump() {
    Uri uri = ContactsContract.Contacts.CONTENT_URI;

    Intent intent = new Intent(Intent.ACTION_PICK,
        uri);

    startActivityForResult(intent, 0);

  }
}
