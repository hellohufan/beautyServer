package com.irontower.gcbuser.mvp.presenter;

import com.irontower.gcbuser.mvp.contract.CityPickerContract;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.mvp.IModel;
import javax.inject.Inject;


@ActivityScope
public class CityPickerPresenter extends
    BasePresenter<IModel, CityPickerContract.View> {


  @Inject
  public CityPickerPresenter(CityPickerContract.View rootView
  ) {
    super(rootView);

  }

  @Override
  public void onDestroy() {
    super.onDestroy();

  }

}
