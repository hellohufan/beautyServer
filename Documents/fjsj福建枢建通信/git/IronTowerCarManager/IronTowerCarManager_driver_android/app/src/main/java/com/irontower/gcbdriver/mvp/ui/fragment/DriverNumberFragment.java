package com.irontower.gcbdriver.mvp.ui.fragment;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import butterknife.BindView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment;
import com.github.mikephil.charting.components.Legend.LegendVerticalAlignment;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.LineDataSet.Mode;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.irontower.gcbdriver.R;
import com.irontower.gcbdriver.app.utils.AppUtil;
import com.irontower.gcbdriver.di.component.DaggerDriverNumberComponent;
import com.irontower.gcbdriver.di.module.DriverNumberModule;
import com.irontower.gcbdriver.mvp.contract.DriverNumberContract;
import com.irontower.gcbdriver.mvp.model.entity.ChartData;
import com.irontower.gcbdriver.mvp.presenter.DriverNumberPresenter;
import com.irontower.gcbdriver.mvp.ui.adapter.DriveNumberAdapter;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.analytics.MobclickAgent;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;


public class DriverNumberFragment extends BaseFragment<DriverNumberPresenter> implements
    DriverNumberContract.View, SwipeRefreshLayout.OnRefreshListener {

  @BindView(R.id.lineChart)
  LineChart lineChart;

  @BindView(R.id.scrollView)
  NestedScrollView scrollView;
  private View view;


  @BindView(R.id.recyclerView)
  RecyclerView recyclerView;
  @BindView(R.id.swipeRefreshLayout)
  SwipeRefreshLayout swipeRefreshLayout;
  @Inject
  RecyclerView.LayoutManager mLayoutManager;
  @Inject
  DriveNumberAdapter adapter;

  public static DriverNumberFragment newInstance() {
    DriverNumberFragment fragment = new DriverNumberFragment();
    return fragment;
  }

  @Override
  public void setupFragmentComponent(AppComponent appComponent) {
    DaggerDriverNumberComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .driverNumberModule(new DriverNumberModule(this))
        .build()
        .inject(this);
  }

  @Override
  public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.fragment_driver_number, container, false);
    return view;
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    scrollView.setFocusable(true);
    scrollView.setFocusableInTouchMode(true);
    scrollView.requestFocus();
    recyclerView.setFocusable(false);
    recyclerView.setNestedScrollingEnabled(false);

    lineChart.setTouchEnabled(false);
    lineChart.setDragEnabled(false);
    lineChart.setScaleEnabled(false);
    lineChart.getAxisRight().setEnabled(false);
    lineChart.getAxisLeft().setDrawAxisLine(false);
    lineChart.getAxisLeft().setDrawLabels(false);
    lineChart.getXAxis().setPosition(XAxisPosition.BOTTOM);
    lineChart.getDescription().setEnabled(false);
    lineChart.getXAxis().setDrawGridLines(false);

    lineChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
      @Override
      public String getFormattedValue(float value, AxisBase axis) {
        return value > 12 ? (int) (value - 12) + "月" : (int) value + "月";

      }
    });
    lineChart.getLegend().setVerticalAlignment(LegendVerticalAlignment.BOTTOM);
    lineChart.getLegend().setHorizontalAlignment(LegendHorizontalAlignment.CENTER);
    lineChart.getLegend().setForm(LegendForm.LINE);
    lineChart.getXAxis().setGranularity(1);
    lineChart.getAxisLeft().setAxisMinimum(0);
    swipeRefreshLayout.setOnRefreshListener(this);
    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(adapter);
    mPresenter.getChartData();


  }

  /**
   * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法, 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
   * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
   *
   * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onCreate还没执行 setData里却调用了presenter的方法时,是会报空的,因为dagger注入是在onCreated方法中执行的,然后才创建的presenter
   * 如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
   */

  @Override
  public void setData(Object data) {

  }


  @Override
  public void showLoading() {
    swipeRefreshLayout.setRefreshing(true);


  }

  @Override
  public void hideLoading() {
    swipeRefreshLayout.setRefreshing(false);


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

  }


  @Override
  public Fragment getFragment() {
    return this;
  }

  @Override
  public void onRefresh() {
    mPresenter.getChartData();
  }

  @Override
  public void toTop() {
    scrollView.fullScroll(ScrollView.FOCUS_UP);
  }

  @Override
  public void onHiddenChanged(boolean hidden) {
    super.onHiddenChanged(hidden);
    if (!hidden) {//当fragment从隐藏到出现的时候
      toTop();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    MobclickAgent.onPageStart(
        "DriverNumberFragment"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
  }

  @Override

  public void onPause() {
    super.onPause();
    MobclickAgent.onPageEnd(
        "DriverNumberFragment"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
  }

  @Override
  public void initChartData(List<ChartData> shortData, List<ChartData> longData) {
    ArrayList<Entry> shortEntry = new ArrayList<>();
    ArrayList<Entry> longEntry = new ArrayList<>();
    int min = 0;
    for (int i = 0; i < shortData.size(); i++) {
      ChartData chartData = shortData.get(i);
      if (i == 0) {
        min = AppUtil.getMonthIndex(chartData.getMonth());
        shortEntry.add(new Entry(min, chartData.getNum().intValue()));
      } else {
        shortEntry.add(new Entry(min + i, chartData.getNum().intValue()));

      }


    }
    lineChart.getXAxis().setAxisMinimum(min);

    for (int i = 0; i < longData.size(); i++) {
      ChartData chartData = longData.get(i);
      if (i == 0) {
        longEntry.add(new Entry(min, chartData.getNum().intValue()));

      } else {
        longEntry.add(new Entry(min + i, chartData.getNum().intValue()));

      }

    }

    LineDataSet set1;
    LineDataSet set2;

    if (lineChart.getData() != null &&
        lineChart.getData().getDataSetCount() > 0) {
      set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
      set1.setValues(shortEntry);
      set2 = (LineDataSet) lineChart.getData().getDataSetByIndex(1);
      set2.setValues(longEntry);
      lineChart.getData().notifyDataChanged();
      lineChart.notifyDataSetChanged();
      lineChart.animateXY(1000, 1000);

      lineChart.invalidate();
    } else {
      // create a dataset and give it a type
      set1 = new LineDataSet(shortEntry, "市区出车数");

      set1.setDrawIcons(false);

      // set the line to be drawn like this "- - - - - -"

      set1.setColor(ArmsUtils.getColor(view.getContext(), R.color.red_line));
      set1.setCircleColor(ArmsUtils.getColor(view.getContext(), R.color.red_line));
      set1.setLineWidth(2f);
      set1.setCircleRadius(3f);
      set1.setDrawCircleHole(true);
      set1.setValueTextSize(9f);
      set1.setFormLineWidth(1f);
      set1.setFormSize(15.f);
      set1.setValueFormatter(new IValueFormatter() {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex,
            ViewPortHandler viewPortHandler) {
          return (int) value + "";
        }
      });
//      set1.setDrawValues(false);
      set1.setMode(Mode.HORIZONTAL_BEZIER);

      set2 = new LineDataSet(longEntry, "长途出车数");

      set2.setDrawIcons(false);

      // set the line to be drawn like this "- - - - - -"

      set2.setColor(ArmsUtils.getColor(view.getContext(), R.color.blue_line));
      set2.setCircleColor(ArmsUtils.getColor(view.getContext(), R.color.blue_line));
      set2.setLineWidth(2f);
      set2.setCircleRadius(3f);
//      set2.setDrawValues(false);
      set2.setDrawCircleHole(true);
      set2.setValueTextSize(9f);
      set2.setFormLineWidth(1f);
      set2.setFormSize(15.f);
      set2.setMode(Mode.HORIZONTAL_BEZIER);
      set2.setValueFormatter(new IValueFormatter() {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex,
            ViewPortHandler viewPortHandler) {
          return (int) value + "";
        }
      });

      ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
      dataSets.add(set1); // add the datasets
      dataSets.add(set2); // add the datasets

      // create a data object with the datasets
      LineData data = new LineData(dataSets);

      // set data
      lineChart.setData(data);
      lineChart.animateXY(1000, 1000);

      lineChart.invalidate();


    }
  }
}
