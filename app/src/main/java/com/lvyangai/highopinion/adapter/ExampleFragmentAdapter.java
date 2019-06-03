package com.lvyangai.highopinion.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/7.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class ExampleFragmentAdapter extends FragmentPagerAdapter {
    private List<String> mDataList;
    private List<Fragment> fragmentList;
    public ExampleFragmentAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList, List<String> dataList) {
        super(fragmentManager);
        this.fragmentList = fragmentList;
        this.mDataList = dataList;
    }


    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDataList.get(position);
    }
}