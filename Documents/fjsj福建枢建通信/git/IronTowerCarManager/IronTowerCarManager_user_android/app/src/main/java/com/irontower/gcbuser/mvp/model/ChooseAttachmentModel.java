package com.irontower.gcbuser.mvp.model;

import android.app.Application;
import com.google.gson.Gson;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;

import com.jess.arms.di.scope.ActivityScope;
import javax.inject.Inject;

import com.irontower.gcbuser.mvp.contract.ChooseAttachmentContract;


@ActivityScope
public class ChooseAttachmentModel extends BaseModel implements ChooseAttachmentContract.Model {

  @Inject
  Gson mGson;
  @Inject
  Application mApplication;

  @Inject
  public ChooseAttachmentModel(IRepositoryManager repositoryManager) {
    super(repositoryManager);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mGson = null;
    this.mApplication = null;
  }
}