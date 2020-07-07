package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.TimePickerView.OnTimeSelectListener;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.app.utils.AppUtil;
import com.irontower.gcbuser.di.component.DaggerTraceViewComponent;
import com.irontower.gcbuser.mvp.contract.TraceViewContract.View;
import com.irontower.gcbuser.mvp.model.dao.AppDatabase;
import com.irontower.gcbuser.mvp.model.entity.CarSearch;
import com.irontower.gcbuser.mvp.model.entity.MultipleCar;
import com.irontower.gcbuser.mvp.model.entity.Track;
import com.irontower.gcbuser.mvp.model.entity.User;
import com.irontower.gcbuser.mvp.presenter.TraceViewPresenter;
import com.irontower.gcbuser.mvp.ui.adapter.CarSearchAdapter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;


/**
 * ================================================ Description:
 * <p>
 * Created by MVPArmsTemplate on 06/12/2019 22:13
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
public class TraceViewActivity extends BaseActivity<TraceViewPresenter> implements
    View {

  @BindView(R.id.tvCarNo)
  TextView tvCarNo;
  @BindView(R.id.tvBeginTime)
  TextView tvBeginTime;
  @BindView(R.id.tvEndTime)
  TextView tvEndTime;
  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  private TimePickerView beginTimePV, endTimePV;
  private Calendar calendar = Calendar.getInstance();
  private MultipleCar multipleCar;
  private CarSearchAdapter carSearchAdapter;
  @Inject
  MaterialDialog materialDialog;
  private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
  private User user;
  public static List<Track> tracks = new ArrayList<>();


  @Override
  public void setupActivityComponent(@NonNull AppComponent appComponent) {
    DaggerTraceViewComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .view(this)
        .build()
        .inject(this);
  }

  @Override
  public int initView(@Nullable Bundle savedInstanceState) {
    return R.layout.activity_trace_view; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(@Nullable Bundle savedInstanceState) {
    tracks.clear();
    user = AppUtil.getUser();
    mPresenter.loadHistory(user.getUserNo());
    multipleCar = getIntent().getParcelableExtra("car");
    tvCarNo.setText(multipleCar.getCarNo());
    tvBeginTime.setText(new SimpleDateFormat("yyyy-MM-dd 00:00").format(new Date()));
    tvEndTime.setText(format.format(new Date()));
    carSearchAdapter = new CarSearchAdapter(R.layout.item_car_search,
        new ArrayList<>());
    carSearchAdapter.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(BaseQuickAdapter adapter, android.view.View view, int position) {
        CarSearch carSearch = (CarSearch) adapter.getData().get(position);
        mPresenter.getData(carSearch.getCarId(), carSearch.getCarNo(), carSearch.getBeginTime(),
            carSearch.getEndTime());
      }
    });
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(carSearchAdapter);
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


  @OnClick({R.id.layBeginTime, R.id.layEndTime, R.id.laySearch})
  public void onViewClicked(android.view.View view) {
    switch (view.getId()) {

      case R.id.layBeginTime:
        chooseBeginTime();

        break;
      case R.id.layEndTime:
        chooseEndTime();
        break;
      case R.id.laySearch:
        search();
        break;
      default:
        break;
    }
  }

  private void search() {

    try {
      long between = format.parse(tvEndTime.getText().toString()).getTime() - format
          .parse(tvBeginTime.getText().toString()).getTime();
      if (between <= 0) {
        ArmsUtils.makeText(getActivity(), "结束时间应大于开始时间");
        return;
      }
      if (between > 7L * 24 * 60 * 60 * 1000) {
        ArmsUtils.makeText(getActivity(), "一次查询范围不能超过7天");
        return;
      }

    } catch (Exception e) {
      return;
    }
    mPresenter.getData(multipleCar.getCarId(), multipleCar.getCarNo(), tvBeginTime.getText(),
        tvEndTime.getText());

  }

  private void chooseBeginTime() {
    if (beginTimePV == null) {
      Calendar startDate = Calendar.getInstance();
      startDate.set(2000, 0, 1);
      Calendar endDate = Calendar.getInstance();
      endDate.set(2050, 11, 31);
      beginTimePV = new TimePickerView.Builder(getActivity(), new OnTimeSelectListener() {
        @Override
        public void onTimeSelect(Date date, android.view.View v) {//选中事件回调
          tvBeginTime.setText(format.format(date));
        }
      }).setType(new boolean[]{true, true, true, true, true, false})
          .setTitleText("起始时间")

          .setTitleSize(17)
          .setCancelColor(ArmsUtils.getColor(getActivity(), R.color.text_hint))
          .setSubmitColor(ArmsUtils.getColor(getActivity(), R.color.colorPrimary))
          .setRangDate(startDate, endDate)//起始终止年月日设定
          .setCancelText("取消")//取消按钮文字
          .setSubmitText("确认")//确认按钮文字
          .setLabel("年", "月", "日", "时", "分", null)
          .build();
    }

    if (!StringUtils.isEmpty(tvBeginTime.getText().toString())) {

      try {
        calendar
            .setTime(format.parse(tvBeginTime.getText().toString()));
      } catch (ParseException e) {
        e.printStackTrace();
      }


    }
    beginTimePV.setDate(calendar);
    beginTimePV.show();
  }

  private void chooseEndTime() {
    if (endTimePV == null) {
      Calendar startDate = Calendar.getInstance();
      startDate.set(2000, 0, 1);
      Calendar endDate = Calendar.getInstance();
      endDate.set(2050, 11, 31);
      endTimePV = new TimePickerView.Builder(getActivity(), new OnTimeSelectListener() {
        @Override
        public void onTimeSelect(Date date, android.view.View v) {//选中事件回调
          tvEndTime.setText(format.format(date));
        }
      }).setType(new boolean[]{true, true, true, true, true, false})
          .setTitleText("结束时间")

          .setTitleSize(17)
          .setCancelColor(ArmsUtils.getColor(getActivity(), R.color.text_hint))
          .setSubmitColor(ArmsUtils.getColor(getActivity(), R.color.colorPrimary))
          .setRangDate(startDate, endDate)//起始终止年月日设定
          .setCancelText("取消")//取消按钮文字
          .setSubmitText("确认")//确认按钮文字
          .setLabel("年", "月", "日", "时", "分", null)
          .build();


    }
    Calendar calendar = Calendar.getInstance();
    if (!StringUtils.isEmpty(tvEndTime.getText().toString())) {

      try {
        calendar
            .setTime(format.parse(tvEndTime.getText().toString()));
      } catch (ParseException e) {
        e.printStackTrace();
      }


    }
    endTimePV.setDate(calendar);
    endTimePV.show();
  }

  @Override
  public Activity getActivity() {
    return this;
  }

  @Override
  public void init(List<Track> rows) {
    tracks = rows;
    if (rows.size() == 0) {
      ArmsUtils.makeText(getActivity(), "该时间段内无数据");
    } else {
      CarSearch carSearch = new CarSearch();
      carSearch.setBeginTime(tvBeginTime.getText().toString());
      carSearch.setCarId(multipleCar.getCarId());
      carSearch.setEndTime(tvEndTime.getText().toString());
      carSearch.setCarNo(multipleCar.getCarNo());
      carSearch.setUserId(user.getUserNo());
      AppDatabase.getInstance(getApplicationContext()).carSearchDao().insertHistory(carSearch);

      launchActivity(
          new Intent(getActivity(), TracePayBackActivity.class).putExtra("carSearch", carSearch));
      mPresenter.loadHistory(user.getUserNo());


    }
  }

  @Override
  public void refreshData(List<CarSearch> carSearches) {
    carSearchAdapter.setNewData(carSearches);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    tracks.clear();
  }
}
