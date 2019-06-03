package com.lvyangai.highopinion.ui.userinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseActivity;
import com.lvyangai.highopinion.ui.reset.ResetActivity;
import com.lvyangai.highopinion.util.StatusBarUtil;
import com.lvyangai.highopinion.util.ToastUtil;

public class UserResetActivity extends BaseActivity {


    private RelativeLayout activityForget;
    private LinearLayout resetTop;
    private Toolbar userResetToolbar;
    private LinearLayout resetTitle;
    private TextInputEditText resetPassword;
    private Button userResetBtn;



    @Override
    protected void initView() {
        setContentView(R.layout.activity_user_reset);
        activityForget = (RelativeLayout) findViewById(R.id.activity_forget);
        resetTop = (LinearLayout) findViewById(R.id.reset_top);
        userResetToolbar = (Toolbar) findViewById(R.id.user_reset_toolbar);
        resetTitle = (LinearLayout) findViewById(R.id.reset_title);
        resetPassword = (TextInputEditText) findViewById(R.id.reset_password);
        userResetBtn = (Button) findViewById(R.id.user_reset_btn);
    }

    @Override
    protected void initData() {
        StatusBarUtil.setPaddingSmart(this,userResetToolbar);
    }

    @Override
    protected void dataProcess() {
        userResetToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass1 = resetPassword.getText().toString();
                if (TextUtils.isEmpty(pass1.trim())){
                    ToastUtil.showShortToast("请输入旧密码");
                }else {
                    if (pass1.equals(MyApplication.getPassword())){
                        MyApplication.setForgetUser(pass1);
                        Intent intent = new Intent();
                        intent.setClass(UserResetActivity.this, ResetActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }else {
                        ToastUtil.showShortToast("原密码不正确");
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) and run LayoutCreator again
    }
}
