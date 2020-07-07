package com.irontower.gcbuser.mvp.model;

import com.blankj.utilcode.util.SPUtils;
import com.irontower.gcbuser.mvp.contract.ManageCarContract;
import com.irontower.gcbuser.mvp.model.api.service.CommonService;
import com.irontower.gcbuser.mvp.model.entity.BaseRowJson;
import com.irontower.gcbuser.mvp.model.entity.Car;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import io.reactivex.Observable;
import javax.inject.Inject;
import okhttp3.FormBody;


@ActivityScope
public class ManageCarModel extends BaseModel implements ManageCarContract.Model {


  @Inject
  public ManageCarModel(IRepositoryManager repositoryManager) {
    super(repositoryManager);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

  }

  @Override
  public Observable<BaseRowJson<Car>> getCars(int currentPage, int i, String b) {
    FormBody body = new FormBody.Builder()
        .add("page", String.valueOf(currentPage))
        .add("rows", String.valueOf(i))
        .add("carNo", b)

        .build();

    return mRepositoryManager.obtainRetrofitService(CommonService.class).getCars(
        SPUtils.getInstance().getString(
            CommonService.ACCESS_TOKEN), body);
  }
}