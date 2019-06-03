package com.lvyangai.highopinion.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lvyangai.highopinion.R;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/4/19.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    protected View mView;
    private AlertDialog dialog;

    protected abstract View initView(LayoutInflater inflater,ViewGroup container);

    protected abstract void initData();

    protected abstract void dataProcess();

    protected abstract void destroy();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = initView(inflater,container);
        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataProcess();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroy();
    }

    protected void showProDialog(Context context){

        dismissDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        View view = View.inflate(context, R.layout.dialog_loading, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    protected void dismissDialog(){

        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }
}
