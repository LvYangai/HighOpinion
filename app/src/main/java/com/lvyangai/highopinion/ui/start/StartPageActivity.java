package com.lvyangai.highopinion.ui.start;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseActivity;
import com.lvyangai.highopinion.bean.UserLoginBean;
import com.lvyangai.highopinion.databinding.ActivityStartPageBinding;
import com.lvyangai.highopinion.ui.home.homeactivity.HomeActivity;
import com.lvyangai.highopinion.ui.login.LoginActivity;
import com.lvyangai.highopinion.util.SPUtils;
import com.lvyangai.highopinion.util.StatusBarUtil;
import com.lvyangai.highopinion.util.ToastUtil;

import retrofit2.Response;

public class StartPageActivity extends BaseActivity implements StartPageContract.startPageView {
    private static final String TAG = "StartPageActivity";
    private StartPagePresenter presenter;
    private ActivityStartPageBinding binding;
    private String user;
    private String pass;
    @Override
    protected void initView() {
        binding = DataBindingUtil.setContentView(this,R.layout.activity_start_page);
    }

    @Override
    protected void initData() {
        presenter = new StartPagePresenter(this);
    }

    @Override
    protected void dataProcess() {
        StatusBarUtil.setPaddingSmart(this,binding.startPage);
        boolean frist = SPUtils.get ("fristaction", false);
        if (!frist) {
            SPUtils.put("fristaction", true);
            Intent intent = new Intent (StartPageActivity.this,
                    LoginActivity.class);
            startActivity (intent);
            finish ();
        } else {
            user = SPUtils.get("user","");
            pass = SPUtils.get("password","");
            if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(StartPageActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in, R.anim.out);
                    }
                }, 3000);
            }else {
                presenter.autoLogin(user,pass);
            }

        }
    }

    @Override
    public void retrofitSuccess(Response<UserLoginBean> response) {
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
                SPUtils.put("password",pass);
                SPUtils.put("iconUrl",iconUrl);
                SPUtils.put("status",true);
                SPUtils.put("userjur",jur);
                SPUtils.put("createTime",createTime);
                SPUtils.put("phone",phone);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(StartPageActivity.this,
                                HomeActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.in, R.anim.out);
                    }
                }, 3000);
            }else {
                retrofitFailed("您账号已被封禁，请尝试重新登录");
            }
        }else {
            retrofitFailed("请求失败!");
        }

    }

    @Override
    public void retrofitFailed(String msg) {
        if ("请求失败!".equals(msg)){
            ToastUtil.showShortToast("请求失败，请检查网络");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StartPageActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.in, R.anim.out);
                }
            }, 3000);
        }else if ("登录失败".equals(msg)){
            ToastUtil.showShortToast("登录失败!用户名或密码错误");
            SPUtils.clear(this);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StartPageActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.in, R.anim.out);
                }
            }, 3000);
        }else {
            ToastUtil.showShortToast(msg);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StartPageActivity.this,
                            LoginActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.in, R.anim.out);
                }
            }, 3000);
        }
    }
}
