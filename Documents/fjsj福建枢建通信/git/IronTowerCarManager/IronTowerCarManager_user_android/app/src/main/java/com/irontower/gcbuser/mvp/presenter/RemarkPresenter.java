package com.irontower.gcbuser.mvp.presenter;

import com.irontower.gcbuser.mvp.contract.RemarkContract;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.mvp.IModel;
import javax.inject.Inject;


@ActivityScope
public class RemarkPresenter extends BasePresenter<IModel, RemarkContract.View> {


  @Inject
  public RemarkPresenter(RemarkContract.View rootView) {
    super(rootView);

  }

  @Override
  public void onDestroy() {
    super.onDestroy();

  }

}
