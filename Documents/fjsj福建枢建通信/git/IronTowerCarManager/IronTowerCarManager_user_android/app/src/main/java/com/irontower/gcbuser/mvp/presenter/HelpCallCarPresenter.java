package com.irontower.gcbuser.mvp.presenter;

import android.Manifest.permission;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.mvp.contract.HelpCallCarContract;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
import java.util.List;
import javax.inject.Inject;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class HelpCallCarPresenter extends
    BasePresenter<HelpCallCarContract.Model, HelpCallCarContract.View> {

  private RxErrorHandler mErrorHandler;


  @Inject
  public HelpCallCarPresenter(HelpCallCarContract.Model model, HelpCallCarContract.View rootView
      , RxErrorHandler handler) {
    super(model, rootView);
    this.mErrorHandler = handler;

  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    this.mErrorHandler = null;

  }

  public void checkpermission() {

    PermissionUtil.requestPermission(new PermissionUtil.RequestPermission() {
                                       @Override
                                       public void onRequestPermissionSuccess() {
                                         //request permission success, do something.
                                         mRootView.jump();
                                       }

                                       @Override
                                       public void onRequestPermissionFailure(List<String> permissions) {
                                         ArmsUtils.makeText(mRootView.getActivity(),
                                             ArmsUtils.getString(mRootView.getActivity(), R.string.no_permission));


                                       }

                                       @Override
                                       public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                                         ArmsUtils.makeText(mRootView.getActivity(),
                                             ArmsUtils.getString(mRootView.getActivity(), R.string.no_permission_forerver));
                                       }
                                     }, mRootView.getRxPermissions(), mErrorHandler, permission.GET_ACCOUNTS,
        permission.READ_CONTACTS);

  }
}
