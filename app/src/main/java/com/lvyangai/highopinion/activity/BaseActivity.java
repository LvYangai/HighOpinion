package com.lvyangai.highopinion.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.util.StatusBarUtil;

/**
 * Created by zhaoshuang on 2018/6/1.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected BaseActivity mContext;
    private AlertDialog dialog;

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void dataProcess();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        StatusBarUtil.immersive(mContext,Color.parseColor("#3F3C51"),0f);
//        StatusBarUtil.setColor(mContext, ContextCompat.getColor(mContext, R.color.my_black), false);
        initView();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        StatusBarUtil.setColor(this, Color.WHITE, true);
        dataProcess();
    }

    protected void showProDialog(){

        dismissDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        View view = View.inflate(mContext, R.layout.dialog_loading, null);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }
    protected void showProDialog(String content){

        dismissDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(false);
        View view = View.inflate(mContext, R.layout.dialog_loading, null);
        TextView textView = view.findViewById(R.id.load_hint);
        textView.setText(""+content);
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
