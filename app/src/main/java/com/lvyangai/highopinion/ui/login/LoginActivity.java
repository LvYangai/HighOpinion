package com.lvyangai.highopinion.ui.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseActivity;
import com.lvyangai.highopinion.bean.UserLoginBean;
import com.lvyangai.highopinion.databinding.ActivityLoginBinding;
import com.lvyangai.highopinion.ui.forget.ForgetActivity;
import com.lvyangai.highopinion.ui.home.homeactivity.HomeActivity;
import com.lvyangai.highopinion.ui.register.RegisterActivity;
import com.lvyangai.highopinion.util.SPUtils;
import com.lvyangai.highopinion.util.ToastUtil;

import retrofit2.Response;

public class LoginActivity extends BaseActivity implements LoginContract.loginView,View.OnClickListener {
    private final static String TAG = LoginActivity.class.getSimpleName();
    private Context mContext;
    private LoginPresenter mLoginPresenter;
    private ActivityLoginBinding mBinding;
    private String password;
    @Override
    protected void initView() {
        mContext = LoginActivity.this;
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);
    }

    @Override
    protected void initData() {
        mLoginPresenter = new LoginPresenter(this);
        mBinding.loginBtn.setOnClickListener(this);
        mBinding.loginForget.setOnClickListener(this);
        mBinding.loginRegister.setOnClickListener(this);
    }

    @Override
    protected void dataProcess() {
        mBinding.loginAccount.setText(SPUtils.get("user",""));
        mBinding.loginPassword.setText(SPUtils.get("password",""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                    mLoginPresenter.login(
                            mBinding.loginAccount.getText().toString(),
                            mBinding.loginPassword.getText().toString());
                break;
            case R.id.login_register:
                registerSuccess();
                break;
            case R.id.login_forget:
                forgetSuccess();
                break;
            default:
                break;
        }
    }


    @Override
    public void accountIsNull() {
        Toast.makeText(mContext, "请输入您的账户", Toast.LENGTH_LONG).show();
    }

    @Override
    public void passWordIsNull() {
        Toast.makeText(mContext, "请输入您的密码", Toast.LENGTH_LONG).show();
    }

    @Override
    public void loginSuccess(String user, String pass) {
        showProDialog();
        password = pass;
        mLoginPresenter.userLogin(user,pass);
    }


    @Override
    public void registerSuccess() {

        Intent intentRegister = new Intent();
        intentRegister.setClass(LoginActivity.this, RegisterActivity.class);
        startActivity(intentRegister);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void forgetSuccess() {
        Intent intentRegister = new Intent();
        intentRegister.setClass(LoginActivity.this, ForgetActivity.class);
        startActivity(intentRegister);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void loginFailed() {
        Toast.makeText(mContext, "登录失败", Toast.LENGTH_LONG).show();
    }

    @Override
    public void userLoginSuccess(Response<UserLoginBean> response) {
        if (response != null){
            UserLoginBean.LoginBean loginBean = response.body().getLogin();
            if (1 == loginBean.getUser_status()){
                String user = loginBean.getUser_name();
                String userName = loginBean.getUser_username();
                String createTime = loginBean.getUser_date();
                String iconUrl = loginBean.getUser_icon();
                int jur = loginBean.getUser_jur();
                int id = loginBean.getId();
                String phone = loginBean.getUser_mobile();
                SPUtils.put("userid",id);
                SPUtils.put("user",user);
                SPUtils.put("username",userName);
                SPUtils.put("password",password);
                SPUtils.put("iconUrl",iconUrl);
                SPUtils.put("status",true);
                SPUtils.put("userjur",jur);
                SPUtils.put("createTime",createTime);
                SPUtils.put("phone",phone);

                ToastUtil.showShortToast("登录成功");

                Intent intentRegister = new Intent();
                intentRegister.setClass(LoginActivity.this, HomeActivity.class);
                startActivity(intentRegister);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }else {
                ToastUtil.showShortToast("您的账号已被封禁，禁止登录！");
            }
        }
        dismissDialog();
    }

    @Override
    public void userLoginFailed(String msg) {
        ToastUtil.showShortToast(msg);
        dismissDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.onDestroyView();
    }
}
