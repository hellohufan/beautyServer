package com.irontower.gcbuser.mvp.presenter;

import com.irontower.gcbuser.mvp.contract.ChoosePositionContract;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.mvp.IModel;
import javax.inject.Inject;


@ActivityScope
public class ChoosePositionPresenter extends
    BasePresenter<IModel, ChoosePositionContract.View> {


  @Inject
  public ChoosePositionPresenter(
      ChoosePositionContract.View rootView
  ) {
    super(rootView);

  }

  @Override
  public void onDestroy() {
    super.onDestroy();

  }

}
