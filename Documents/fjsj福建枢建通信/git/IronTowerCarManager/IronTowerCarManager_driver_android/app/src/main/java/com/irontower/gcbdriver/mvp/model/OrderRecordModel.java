package com.irontower.gcbdriver.mvp.model;

import com.blankj.utilcode.util.SPUtils;
import com.irontower.gcbdriver.mvp.contract.OrderRecordContract;
import com.irontower.gcbdriver.mvp.model.api.service.CommonService;
import com.irontower.gcbdriver.mvp.model.entity.BaseRowJson;
import com.irontower.gcbdriver.mvp.model.entity.Order;
import com.irontower.gcbdriver.mvp.model.entity.OrderState;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class OrderRecordModel extends BaseModel implements OrderRecordContract.Model {


  @Inject
  public OrderRecordModel(IRepositoryManager repositoryManager) {
    super(repositoryManager);

  }

  @Override
  public void onDestroy() {
    super.onDestroy();

  }

  @Override
  public Observable<BaseRowJson<Order>> getOrders(int page, int rows) {
    FormBody body = new FormBody.Builder()
        .add("state", OrderState.FINISHED)
        .add("page", String.valueOf(page))

        .add("rows", String.valueOf(rows))
        .add("orderCol", "beginTime")
        .add("orderBy", "desc")

        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).getOrders(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), body);
  }
}