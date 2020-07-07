package com.irontower.gcbuser.mvp.presenter;

import com.irontower.gcbuser.mvp.contract.ChooseStartPositionContract;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.mvp.IModel;
import javax.inject.Inject;


@ActivityScope
public class ChooseStartPositionPresenter extends
    BasePresenter<IModel, ChooseStartPositionContract.View> {


  @Inject
  public ChooseStartPositionPresenter(
      ChooseStartPositionContract.View rootView
  ) {
    super(rootView);

  }

  @Override
  public void onDestroy() {
    super.onDestroy();

  }

}
