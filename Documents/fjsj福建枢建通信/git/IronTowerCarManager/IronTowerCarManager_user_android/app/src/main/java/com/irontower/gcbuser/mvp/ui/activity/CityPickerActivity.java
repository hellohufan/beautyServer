package com.irontower.gcbuser.mvp.ui.activity;

import static com.jess.arms.utils.Preconditions.checkNotNull;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.SearchView.SearchAutoComplete;
import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import com.blankj.utilcode.util.StringUtils;
import com.irontower.gcbuser.R;
import com.irontower.gcbuser.di.component.DaggerCityPickerComponent;
import com.irontower.gcbuser.di.module.CityPickerModule;
import com.irontower.gcbuser.mvp.contract.CityPickerContract.View;
import com.irontower.gcbuser.mvp.presenter.CityPickerPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.lljjcoder.style.citylist.bean.CityInfoBean;
import com.lljjcoder.style.citylist.sortlistview.CharacterParser;
import com.lljjcoder.style.citylist.sortlistview.PinyinComparator;
import com.lljjcoder.style.citylist.sortlistview.SideBar;
import com.lljjcoder.style.citylist.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.lljjcoder.style.citylist.sortlistview.SortAdapter;
import com.lljjcoder.style.citylist.sortlistview.SortModel;
import com.lljjcoder.style.citylist.utils.CityListLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.simple.eventbus.EventBus;


public class CityPickerActivity extends BaseActivity<CityPickerPresenter> implements
    View {


  public SortAdapter adapter;
  @BindView(R.id.searchView)
  SearchView searchView;
  @BindView(R.id.currentCity)
  TextView currentCity;
  @BindView(R.id.listView)
  ListView listView;
  @BindView(R.id.tvHint)
  TextView tvHint;
  @BindView(R.id.sideBar)
  SideBar sideBar;


  /**
   * 汉字转换成拼音的类
   */
  private CharacterParser characterParser;

  private List<SortModel> sourceDateList;

  /**
   * 根据拼音来排列ListView里面的数据类
   */
  private PinyinComparator pinyinComparator;

  private List<CityInfoBean> cityListInfo = new ArrayList<>();

  private CityInfoBean cityInfoBean = new CityInfoBean();

  //startActivityForResult flag
  public static List<CityInfoBean> sCityInfoBeanList = new ArrayList<>();

  @Override
  public void setupActivityComponent(AppComponent appComponent) {
    DaggerCityPickerComponent //如找不到该类,请编译一下项目
        .builder()
        .appComponent(appComponent)
        .cityPickerModule(new CityPickerModule(this))
        .build()
        .inject(this);
  }

  @Override
  public int initView(Bundle savedInstanceState) {
    return R.layout.activity_city_picker; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
  }

  @Override
  public void initData(Bundle savedInstanceState) {
    currentCity.setText(getIntent().getStringExtra("city"));

    searchView.setIconified(false);
    searchView.onActionViewExpanded();
    searchView.setIconifiedByDefault(true);
    SearchAutoComplete mSearchAutoComplete = (SearchAutoComplete) searchView
        .findViewById(R.id.search_src_text);

    //设置输入框提示文字样式
    mSearchAutoComplete.setHintTextColor(ArmsUtils.getColor(this, R.color.white));//设置提示文字颜色
    mSearchAutoComplete.setTextColor(ArmsUtils.getColor(this, R.color.white));//设置内容文字颜色

    initList();

    setCityData(CityListLoader.getInstance().getCityListData());
    searchView.setOnQueryTextListener(new OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        filterData(newText);

        return false;
      }
    });
  }


  @Override
  public void showLoading() {

  }

  @Override
  public void hideLoading() {

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


  @Override
  public Activity getActivity() {
    return this;
  }

  private void setCityData(List<CityInfoBean> cityList) {
    cityListInfo = cityList;
    if (cityListInfo == null) {
      return;
    }
    int count = cityList.size();
    String[] list = new String[count];
    for (int i = 0; i < count; i++) {
      list[i] = cityList.get(i).getName();
    }

    sourceDateList.addAll(filledData(cityList));
    // 根据a-z进行排序源数据
    Collections.sort(sourceDateList, pinyinComparator);
    adapter.notifyDataSetChanged();
  }

  private void initList() {
    sourceDateList = new ArrayList<SortModel>();
    adapter = new SortAdapter(getActivity(), sourceDateList);
    listView.setAdapter(adapter);

    //实例化汉字转拼音类
    characterParser = CharacterParser.getInstance();
    pinyinComparator = new PinyinComparator();
    sideBar.setTextView(tvHint);
    //设置右侧触摸监听
    sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

      @Override
      public void onTouchingLetterChanged(String s) {
        //该字母首次出现的位置
        int position = adapter.getPositionForSection(s.charAt(0));
        if (position != -1) {
          listView.setSelection(position);
        }
      }
    });

    listView.setOnItemClickListener(new OnItemClickListener() {

      @Override
      public void onItemClick(AdapterView<?> parent, android.view.View view, int position,
          long id) {
        String cityName = ((SortModel) adapter.getItem(position)).getName();
        if (!StringUtils.isEmpty(getIntent().getStringExtra("type"))) {
          EventBus.getDefault().post(cityName, getIntent().getStringExtra("type"));
        }

        finish();
      }
    });

    //根据输入框输入值的改变来过滤搜索

  }

  private void filterData(String filterStr) {
    List<SortModel> filterDateList = new ArrayList<SortModel>();

    if (TextUtils.isEmpty(filterStr)) {
      filterDateList = sourceDateList;
    } else {
      filterDateList.clear();
      for (SortModel sortModel : sourceDateList) {
        String name = sortModel.getName();
        if (name.contains(filterStr) || characterParser.getSelling(name).startsWith(filterStr)) {
          filterDateList.add(sortModel);
        }
      }
    }

    // 根据a-z进行排序
    Collections.sort(filterDateList, pinyinComparator);
    adapter.updateListView(filterDateList);
  }

  private List<SortModel> filledData(List<CityInfoBean> cityList) {
    List<SortModel> mSortList = new ArrayList<SortModel>();

    for (int i = 0; i < cityList.size(); i++) {

      CityInfoBean result = cityList.get(i);

      if (result != null) {

        SortModel sortModel = new SortModel();

        String cityName = result.getName();
        //汉字转换成拼音
        String pinyin = result.getPinYin();

        if (!TextUtils.isEmpty(cityName) && !TextUtils.isEmpty(pinyin)) {

          sortModel.setName(cityName);

          String sortString = pinyin.substring(0, 1).toUpperCase();

          // 正则表达式，判断首字母是否是英文字母
          if (sortString.matches("[A-Z]")) {
            sortModel.setSortLetters(sortString.toUpperCase());
          } else {
            sortModel.setSortLetters("#");
          }

          mSortList.add(sortModel);
        } else {
        }

      }
    }
    return mSortList;
  }

}
