package com.lvyangai.highopinion.ui.home.homefragment.home;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseFragment;
import com.lvyangai.highopinion.activity.BaseParentFragment;
import com.lvyangai.highopinion.adapter.ExampleFragmentAdapter;
import com.lvyangai.highopinion.databinding.FragmentHomeBinding;
import com.lvyangai.highopinion.magicindicator.MagicIndicator;
import com.lvyangai.highopinion.magicindicator.ViewPagerHelper;
import com.lvyangai.highopinion.magicindicator.buildins.UIUtil;
import com.lvyangai.highopinion.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.lvyangai.highopinion.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.lvyangai.highopinion.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.lvyangai.highopinion.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.lvyangai.highopinion.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import com.lvyangai.highopinion.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import com.lvyangai.highopinion.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import com.lvyangai.highopinion.magicindicator.example.ExamplePagerAdapter;
import com.lvyangai.highopinion.magicindicator.ext.titles.ScaleTransitionPagerTitleView;
import com.lvyangai.highopinion.ui.home.homefragment.home.first.FirstFragment;
import com.lvyangai.highopinion.ui.home.homefragment.home.second.SecondFragment;
import com.lvyangai.highopinion.ui.home.homefragment.home.third.ThirdFragment;
import com.lvyangai.highopinion.ui.home.search.SearchActivity;
import com.lvyangai.highopinion.ui.web.WebActivity;
import com.lvyangai.highopinion.util.StatusBarUtil;
import com.lvyangai.highopinion.util.ToastUtil;
import com.wyt.searchbox.SearchFragment;
import com.wyt.searchbox.custom.IOnSearchClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseParentFragment implements View.OnClickListener {

    private FragmentHomeBinding binding;
    private static final String TAG = "HomeFragment";
    private View view;
    private Context context;
    private List<Fragment> fragmentList;
    private static final String[] CHANNELS = new String[]{"推荐", "近期热门", "美食","数码","家居","美妆","宠物","电影","其他","最新"};
    private List<String> mDataList;
    private SearchFragment searchFragment;
    private ExampleFragmentAdapter homeAdapter;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        if (view == null){
            binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
            view = binding.getRoot();
        }
        return view;
    }

    @Override
    protected void initData() {
        context = getActivity();
        fragmentList = new ArrayList<>();
        fragmentList.add(new FirstFragment());
        fragmentList.add(new SecondFragment(1,"近期热门"));
        fragmentList.add(new SecondFragment(2,CHANNELS[2]));
        fragmentList.add(new SecondFragment(3,CHANNELS[3]));
        fragmentList.add(new ThirdFragment(4,CHANNELS[4]));
        fragmentList.add(new ThirdFragment(5,CHANNELS[5]));
        fragmentList.add(new SecondFragment(6,CHANNELS[6]));
        fragmentList.add(new ThirdFragment(7,CHANNELS[7]));
        fragmentList.add(new SecondFragment(8,CHANNELS[8]));
        fragmentList.add(new SecondFragment(9,CHANNELS[9]));
        mDataList = Arrays.asList(CHANNELS);
    }

    @Override
    protected void dataProcess() {
        homeAdapter = new ExampleFragmentAdapter(getChildFragmentManager(),fragmentList,mDataList);
        binding.fragHomeViewPager.setAdapter(homeAdapter);
        binding.fragHomeViewPager.setOffscreenPageLimit(10);
        searchFragment = SearchFragment.newInstance();
        binding.homeSearch.setOnClickListener(this);
        binding.searchClick.setOnClickListener(this);
        searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                if (TextUtils.isEmpty(keyword.trim())){
                    ToastUtil.showShortToast("请输入关键字");
                }else {
                    Intent intent = new Intent(context, SearchActivity.class);
                    intent.putExtra("INTENT_KEY_WORD",keyword);
                    startActivity(intent);
                }

            }
        });
        initMagicIndicator();
    }

    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setTextSize(18);
                simplePagerTitleView.setNormalColor(Color.parseColor("#88ffffff"));
                simplePagerTitleView.setSelectedColor(Color.WHITE);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.fragHomeViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(Color.parseColor("#40c4ff"));
                return indicator;
            }
        });
        binding.fragHomeMagicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerPadding(UIUtil.dip2px(context, 15));
        titleContainer.setDividerDrawable(getResources().getDrawable(R.drawable.simple_right_splitter));
        ViewPagerHelper.bind(binding.fragHomeMagicIndicator, binding.fragHomeViewPager);
    }

    @Override
    protected void destroy() {

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
//            showToast("onPause");
            onUserInvisible();
        }else {
//            showToast("onRename");
            onUserVisible();
        }
    }

    private void showToast(String str) {
        Toast.makeText(getActivity(),""+str,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_search:
                    searchFragment.showFragment(getChildFragmentManager(), SearchFragment.TAG);
                break;
            case R.id.search_click:
                searchFragment.showFragment(getChildFragmentManager(), SearchFragment.TAG);
                break;
        }
    }

}
