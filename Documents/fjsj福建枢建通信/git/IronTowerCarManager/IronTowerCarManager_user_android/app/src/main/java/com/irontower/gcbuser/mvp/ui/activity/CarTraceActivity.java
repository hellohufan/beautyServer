package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.di.component.DaggerCarTraceComponent;
import com.irontower.gcbuser.mvp.contract.CarTraceContract.View;
import com.irontower.gcbuser.mvp.model.entity.MultipleCar;
import com.irontower.gcbuser.mvp.model.entity.MultipleOrg;
import com.irontower.gcbuser.mvp.presenter.CarTracePresenter;
import com.irontower.gcbuser.mvp.ui.adapter.CarTraceAdapter;
import com.irontower.gcbuser.mvp.ui.adapter.CarTraceAdapter.OnLoadListener;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;


/**
 * ================================================ Description:
 * <p>
 * Created by MVPArmsTemplate on 06/12/2019 10:53
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class CarTraceActivity extends BaseActivity<CarTracePresenter> implements
    View, OnLoadListener {

  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;

  CarTraceAdapter adapter;
  @Inject
  MaterialDialog materialDialog;


  @Override
  public void setupActivityComponent(@NonNull AppComponent appComponent) {
    DaggerCarTraceComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .view(this)
        .build()
        .inject(this);
  }

  @Override
  public int initView(@Nullable Bundle savedInstanceState) {
    return R.layout.activity_car_trace; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(@Nullable Bundle savedInstanceState) {
    adapter = new CarTraceAdapter(new ArrayList<>(), this);
    recyclerView.setAdapter(adapter);
    // important! setLayoutManager should be called after setAdapter
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    mPresenter.getData();
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
  public void onLoad(MultipleOrg multipleOrg, int pos) {
    mPresenter.getChildren(multipleOrg, pos);
  }

  @Override
  public Activity getActivity() {
    return this;
  }

  @Override
  public void dealData(List<Map<String, Object>> origin) {
    List<MultiItemEntity> data = new ArrayList<>();

    for (Map<String, Object> map : origin) {
      Map<String, Object> attributes = (Map<String, Object>) map.get("attributes");
      if (!attributes.containsKey("carId")) {
        MultipleOrg multipleOrg = new MultipleOrg();
        multipleOrg.setId((String) map.get("id"));
        multipleOrg.setText((String) map.get("text"));
        multipleOrg.setParent((Boolean) attributes.get("isParent"));
        multipleOrg.setOnlineCount((int) (double) (Double) attributes.get("onlineCount"));
        multipleOrg.setTotalCount((int) (double) (Double) attributes.get("totalCount"));
        data.add(multipleOrg);
      } else {
        MultipleCar multipleCar = new MultipleCar();
        multipleCar.setId((String) map.get("id"));
        multipleCar.setCarId((String) map.get("carId"));
        multipleCar.setCarNo((String) map.get("carNo"));
        multipleCar.setDataId((String) map.get("dataId"));
        multipleCar.setText((String) map.get("text"));
        if (attributes.containsKey("dAttr")) {
          multipleCar.setdAttr((int) (double) (Double) attributes.get("dAttr"));
        }

        data.add(multipleCar);
      }

    }
    adapter.setNewData(data);


  }

  @Override
  public void dealChildren(List<Map<String, Object>> list, MultipleOrg parent, int pos) {

    List<MultiItemEntity> children = new ArrayList<>();

    for (Map<String, Object> map : list) {
      Map<String, Object> attributes = (Map<String, Object>) map.get("attributes");
      if (!attributes.containsKey("carId")) {
        MultipleOrg multipleOrg = new MultipleOrg();
        multipleOrg.setId((String) map.get("id"));
        multipleOrg.setText((String) map.get("text"));
        multipleOrg.setParent((Boolean) attributes.get("isParent"));
        multipleOrg.setOrgLevel(parent.getOrgLevel() + 1);
        multipleOrg.setOnlineCount((int) (double) (Double) attributes.get("onlineCount"));
        multipleOrg.setTotalCount((int) (double) (Double) attributes.get("totalCount"));
        children.add(multipleOrg);
      } else {
        MultipleCar multipleCar = new MultipleCar();
        multipleCar.setId((String) map.get("id"));
        multipleCar.setCarId(String.valueOf((int) (double) (Double) attributes.get("carId")));
        multipleCar.setCarNo((String) attributes.get("carNo"));
        multipleCar.setDataId(String.valueOf((int) (double) (Double) attributes.get("dataId")));
        if (attributes.containsKey("dAttr")) {
          multipleCar.setdAttr((int) (double) (Double) attributes.get("dAttr"));
        }
        multipleCar.setText((String) map.get("text"));
        multipleCar.setOrgLevel(parent.getOrgLevel());

        children.add(multipleCar);
      }


    }
    parent.setSubItems(children);
    if (children.size() != 0) {
      adapter.expand(pos);
    }
  }

  @Override
  public void jump(MultipleCar multipleCar) {
    launchActivity(
        new Intent(getActivity(), CarPositionMapActivity.class).putExtra("car", multipleCar));
  }
}
