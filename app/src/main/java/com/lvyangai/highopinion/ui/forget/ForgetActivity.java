package com.lvyangai.highopinion.ui.forget;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseActivity;
import com.lvyangai.highopinion.databinding.ActivityForgetBinding;
import com.lvyangai.highopinion.ui.login.LoginActivity;
import com.lvyangai.highopinion.ui.register.RegisterActivity;
import com.lvyangai.highopinion.ui.reset.ResetActivity;
import com.lvyangai.highopinion.util.StatusBarUtil;
import com.lvyangai.highopinion.util.ToastUtil;


public class ForgetActivity extends BaseActivity implements ForgetContract.forgetView,View.OnClickListener{
    private final static String TAG = ForgetPresenter.class.getSimpleName();
    private Context context;
    private ForgetPresenter presenter;
    private ActivityForgetBinding binding;
    private String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$(?!.*\\s)";//密码验证
    String user;
    @Override
    protected void initView() {
        context = ForgetActivity.this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget);
    }

    @Override
    protected void initData() {
        presenter = new ForgetPresenter(this);
        binding.forgetBtnNext.setOnClickListener(this);

    }


    @Override
    protected void dataProcess() {
        binding.fotgetToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        StatusBarUtil.setPaddingSmart(this,binding.fotgetToolbar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forget_btn_next:
                if (submit()){
                    showProDialog();
                    user = binding.forgetUsername.getText().toString();
                    String phone = binding.forgetNumber.getText().toString();
                    presenter.forget(user,phone);
                }
                break;
        }
    }
    public boolean submit(){
        String user = binding.forgetUsername.getText().toString().trim();

        if (TextUtils.isEmpty(user)){
            ToastUtil.showShortToast("请输入注册时用户名");
            return false;
        }
        String phone = binding.forgetNumber.getText().toString().trim();
        if (TextUtils.isEmpty(phone)){
            ToastUtil.showShortToast("请输入注册时的手机号");
            return false;
        }
        return true;
    }

    @Override
    public void inputIsNull(String message) {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void forgetSuccess(String msg) {
        dismissDialog();
//        ToastUtil.showShortToast(msg);
        if ("可以修改密码".equals(msg)){
            MyApplication.setForgetUser(user);
            Intent intent = new Intent();
            intent.setClass(this, ResetActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }

    @Override
    public void forgetFailed(String msg) {
        dismissDialog();
        ToastUtil.showShortToast(msg);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (context != null)context = null;
        if (presenter != null){
            presenter = null;
        }
        if (binding != null) binding = null;
    }
}
