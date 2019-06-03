package com.lvyangai.highopinion.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/8.
 * 描述：继承BaseLazyFragment用于嵌套的外层Fragment辅助内层Fragment显示与隐藏监听
 * 邮箱：1076977275@qq.com
 */

public class BaseParentFragment extends BaseLazyFragment {
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void dataProcess() {

    }

    @Override
    protected void destroy() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataProcess();
    }

    @Override
    protected void onFirstUserVisible() {

        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            for (Fragment f : fragments) {
                if (f instanceof BaseLazyFragment
                        && (((BaseLazyFragment) f).isUIVisible
                        && f.isResumed())) {
                    ((BaseLazyFragment) f).onFirstUserVisible();
                }
            }
        }
    }

    /**
     * 除第一外的布局可见时候
     */
    public void onUserVisible() {

        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (!fragments.isEmpty()) {
            for (Fragment f : fragments) {
                if (f instanceof BaseLazyFragment
                        && (((BaseLazyFragment) f).isUIVisible
                        && f.isResumed())) {
                    ((BaseLazyFragment) f).onUserVisible();
                }
            }
        }
    }

    /**
     * 除了第一次布局消失时
     */
    protected void onUserInvisible() {
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (!fragments.isEmpty()) {
            for (Fragment f : fragments) {
                if (f instanceof BaseLazyFragment
                        && (((BaseLazyFragment) f).isUIVisible
                        && f.isResumed())) {
                    ((BaseLazyFragment) f).onUserInvisible();
                }
            }
        }
    }
}
