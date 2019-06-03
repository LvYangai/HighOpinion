package com.lvyangai.highopinion.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/8.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public abstract class BaseChildFragment extends BaseLazyFragment {
    private static final String TAG = "BaseChildFragment";

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


    private boolean isVisible = true;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroy();
    }

    @Override
    protected void onFirstUserVisible() {
        if (getParentFragment() != null
                && (getParentFragment() instanceof BaseLazyFragment)
                && !((BaseLazyFragment) getParentFragment()).isUIVisible) {
            if (!((BaseLazyFragment) getParentFragment()).isLazyIn) {
                isVisible = true;
                onVisible();
            }
        } else {
            //为了解决三级嵌套，不支持更多级
            if (getParentFragment() != null
                    && getParentFragment().getParentFragment() != null
                    && getParentFragment() instanceof BaseLazyFragment) {
                if (((BaseLazyFragment) getParentFragment().getParentFragment()).isUIVisible
                        && ((BaseLazyFragment) getParentFragment().getParentFragment()).isResumed()) {
                    isVisible = true;
                    onVisible();
                }
            } else {
                isVisible = true;
                onVisible();
            }
        }
    }

    /**
     * 除第一外的布局可见时候
     */
    public void onUserVisible() {

        if (getParentFragment() != null
                && getParentFragment() instanceof BaseParentFragment
                && !((((BaseParentFragment) getParentFragment()).isUIVisible
                && getParentFragment().isResumed()))) {
            if (!((BaseLazyFragment) getParentFragment()).isLazyIn) {
                isVisible = true;
                onVisible();
            }
        } else {
            isVisible = true;
            onVisible();
        }
    }

    /**
     * 除了第一次布局消失时
     */
    protected void onUserInvisible() {
        isVisible = false;
        onInvisible();
    }

    private void onVisible() {
        doSomeThingVisible();

    }

    protected abstract void doSomeThingVisible();

    private void onInvisible() {
        doSomeThingInvisible();

    }

    protected abstract void doSomeThingInvisible();


}
