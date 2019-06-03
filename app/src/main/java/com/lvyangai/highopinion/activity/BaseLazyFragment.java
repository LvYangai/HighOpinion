package com.lvyangai.highopinion.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lvyangai.highopinion.util.StatusBarUtil;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/8.
 * 描述：实现懒加载
 * 邮箱：1076977275@qq.com
 */
public abstract class BaseLazyFragment extends Fragment {

    private static final String TAG = "BaseLazyFragment";
    private boolean isFirstVisible  = false;
    public boolean isLazyIn = false;
    public boolean isUIVisible = false;
    private boolean isViewCreated = false;
    private boolean isFirstInvisible = false;

    protected View view;
    protected abstract View initView(LayoutInflater inflater,ViewGroup container);

    protected abstract void initData();

    protected abstract void dataProcess();

    protected abstract void destroy();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        view = initView(inflater,container);

        isViewCreated = true;
        isFirstVisible = true;
        lazyLoad();
        return view;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstVisible  = false;//视图销毁将变量置为false
        destroy();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isLazyIn = true;
        if (isVisibleToUser) {
            isUIVisible = true; //当前fragment可见
            if (isFirstVisible) {
                //如果是第一次可见，则进行懒加载
                isFirstVisible = false;
                lazyLoad();
            } else {
                //不是第一次可见，则调用onUserVisible()
                onUserVisible();
            }
        } else {
            isUIVisible = false;
            if (isFirstInvisible) {
                isFirstInvisible = false;
                //第一次不可见,不需要执行操作，因为，缓存中的Fragment,最先会执行到这里，
                // 不可见早于可见，都没可见，所以第一次不可见意义不存在
            } else {
                //非第一次不可见
                onUserInvisible();
            }
        }

    }
    private void lazyLoad() {
        //需要进行双重判断，避免因为setUserVisibleHint先于onViewCreaetd调用时，出现空指针
        if (isViewCreated && isUIVisible) {
            onFirstUserVisible();  //进行初次可见时的加载
        }
    }

    protected abstract void onFirstUserVisible();

    /**
     * 除第一外的布局可见时候
     */
    public abstract void onUserVisible();

    /**
     * 除了布局消失时
     */
    protected abstract void onUserInvisible();
}
