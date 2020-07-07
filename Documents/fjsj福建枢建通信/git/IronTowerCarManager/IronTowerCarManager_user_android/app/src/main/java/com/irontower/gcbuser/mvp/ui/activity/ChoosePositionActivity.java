package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.EventBusTags;
import com.irontower.gcbuser.di.component.DaggerChoosePositionComponent;
import com.irontower.gcbuser.di.module.ChoosePositionModule;
import com.irontower.gcbuser.mvp.contract.ChoosePositionContract.View;
import com.irontower.gcbuser.mvp.presenter.ChoosePositionPresenter;
import com.irontower.gcbuser.mvp.ui.adapter.AddressAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;


public class ChoosePositionActivity extends BaseActivity<ChoosePositionPresenter> implements
    View {


  @BindView(R.id.tvCity)
  TextView tvCity;
  @BindView(R.id.layCity)
  LinearLayout layCity;
  @BindView(R.id.etSearch)
  AppCompatEditText etSearch;
  @BindView(R.id.layCancel)
  LinearLayout layCancel;
  @BindView(R.id.tvCancel)
  TextView tvCancel;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  private PoiSearch poiSearch;

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerChoosePositionComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .choosePositionModule(new ChoosePositionModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_choose_position; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    LayoutManager layoutManager = new LinearLayoutManager(getActivity());
    tvCity.setText(getIntent().getStringExtra("city"));
    AddressAdapter addressAdapter = new AddressAdapter(R.layout.item_address,
        new ArrayList<>());

    addressAdapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, android.view.View view,
          int position) {
        PoiInfo poiInfo = addressAdapter.getData().get(position);
        Map<String, Object> map = new HashMap<>();
        map.put("city", poiInfo.city);
        map.put("latLng", poiInfo.location);
        map.put("address", poiInfo.name);
        EventBus.getDefault().post(map, EventBusTags.ENDCITY3);
        finish();


      }
    });
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setHasFixedSize(true);

    recyclerView.setAdapter(addressAdapter);
    poiSearch = PoiSearch.newInstance();
    poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
      @Override
      public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult.getAllPoi() != null) {
          addressAdapter.setNewData(poiResult.getAllPoi());
        } else {
          addressAdapter.setNewData(new ArrayList<>());
        }

      }

      @Override
      public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

      }

      @Override
      public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

      }
    });
    etSearch.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() == 0) {
          tvCancel.setText("取消");
        } else {
          tvCancel.setText("确定");
        }
        poiSearch.searchInCity((new PoiCitySearchOption())
            .city(tvCity.getText().toString())
            .keyword(charSequence.toString())
            .isReturnAddr(true));


      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });


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


  @OnClick({R.id.layCity, R.id.layCancel})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {

      case R.id.layCity:
        Intent intent = new Intent();
        intent.putExtra("type", EventBusTags.ENDCITY2);
        intent.putExtra("city", tvCity.getText().toString());
        intent.setClass(getActivity(), CityPickerActivity.class);
        startActivity(intent);
        break;

      case R.id.layCancel:
        if (tvCancel.getText().toString().equals("确定")) {
          Map<String, Object> map = new HashMap<>();
          map.put("city", tvCity.getText().toString());
          map.put("latLng", null);
          map.put("address", etSearch.getText().toString());
          EventBus.getDefault().post(map, EventBusTags.ENDCITY3);
        }
        finish();
        break;
      default:

    }
  }

  @Override
  public Activity getActivity() {
    return this;
  }

  @Subscriber(tag = EventBusTags.ENDCITY2, mode = ThreadMode.MAIN)
  public void endCity(String city) {
    tvCity.setText(city);


  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    poiSearch.destroy();
  }
}
