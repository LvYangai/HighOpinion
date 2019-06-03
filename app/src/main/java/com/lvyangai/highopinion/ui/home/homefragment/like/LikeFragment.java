package com.lvyangai.highopinion.ui.home.homefragment.like;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseFragment;
import com.lvyangai.highopinion.activity.BaseParentFragment;
import com.lvyangai.highopinion.adapter.ExampleFragmentAdapter;
import com.lvyangai.highopinion.databinding.FragmentLikeBinding;
import com.lvyangai.highopinion.magicindicator.ViewPagerHelper;
import com.lvyangai.highopinion.magicindicator.buildins.commonnavigator.CommonNavigator;
import com.lvyangai.highopinion.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import com.lvyangai.highopinion.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import com.lvyangai.highopinion.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import com.lvyangai.highopinion.magicindicator.buildins.commonnavigator.indicators.BezierPagerIndicator;
import com.lvyangai.highopinion.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import com.lvyangai.highopinion.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import com.lvyangai.highopinion.magicindicator.example.ExamplePagerAdapter;
import com.lvyangai.highopinion.magicindicator.ext.titles.ScaleTransitionPagerTitleView;
import com.lvyangai.highopinion.ui.home.homefragment.like.media.LikeMediaFragment;
import com.lvyangai.highopinion.ui.home.homefragment.like.page.LikePageFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LikeFragment extends BaseParentFragment {


    private FragmentLikeBinding binding;
    private List<Fragment> fragmentList;
    private ExampleFragmentAdapter likeAdapter;
    private static final String[] CHANNELS = new String[]{"文章", "视频"};
    private List<String> mDataList;
    private View mView;
    private Context context;

    public LikeFragment() {
        // Required empty public constructor
    }


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        if (mView == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_like, container, false);
            mView = binding.getRoot();
        }
        ViewGroup group = (ViewGroup) mView.getParent();
        if (group != null) {
            group.removeView(mView);
        }
        return mView;
    }

    @Override
    protected void initData() {
        context = getActivity();
        fragmentList = new ArrayList<>();
        fragmentList.add(new LikePageFragment());
        fragmentList.add(new LikeMediaFragment());
        mDataList = Arrays.asList(CHANNELS);
    }

    @Override
    protected void dataProcess() {
        likeAdapter = new ExampleFragmentAdapter(getChildFragmentManager(),fragmentList,mDataList);
        binding.fragLikeViewPager.setAdapter(likeAdapter);
        binding.fragLikeViewPager.setOffscreenPageLimit(2);
        initMagicIndicator();
    }

    private void initMagicIndicator() {
        binding.fragLikeMagicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.GRAY);
                simplePagerTitleView.setSelectedColor(Color.BLACK);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.fragLikeViewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                BezierPagerIndicator indicator = new BezierPagerIndicator(context);
                indicator.setColors(Color.parseColor("#ff4a42"), Color.parseColor("#ff4a42"));
                return indicator;
            }
        });
        binding.fragLikeMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(binding.fragLikeMagicIndicator, binding.fragLikeViewPager);
    }

    @Override
    protected void destroy() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden){
            onUserInvisible();
        }else {
            onUserVisible();
        }
    }
}
