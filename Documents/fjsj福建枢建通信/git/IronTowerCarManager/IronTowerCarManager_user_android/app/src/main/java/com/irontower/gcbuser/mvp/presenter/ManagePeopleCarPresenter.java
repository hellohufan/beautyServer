package com.irontower.gcbuser.mvp.presenter;

import com.irontower.gcbuser.mvp.contract.ManagePeopleCarContract;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.mvp.IModel;
import javax.inject.Inject;


@ActivityScope
public class ManagePeopleCarPresenter extends
    BasePresenter<IModel, ManagePeopleCarContract.View> {


  @Inject
  public ManagePeopleCarPresenter(
      ManagePeopleCarContract.View rootView
  ) {
    super(rootView);

  }

  @Override
  public void onDestroy() {
    super.onDestroy();

  }

}
