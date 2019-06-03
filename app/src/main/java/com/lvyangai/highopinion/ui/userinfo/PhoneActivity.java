package com.lvyangai.highopinion.ui.userinfo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.activity.BaseActivity;
import com.lvyangai.highopinion.bean.ForgetBean;
import com.lvyangai.highopinion.data.Api;
import com.lvyangai.highopinion.data.App;
import com.lvyangai.highopinion.databinding.ActivityPhoneBinding;
import com.lvyangai.highopinion.databinding.ActivityResetBinding;
import com.lvyangai.highopinion.ui.reset.ResetPresenter;
import com.lvyangai.highopinion.util.SPUtils;
import com.lvyangai.highopinion.util.StatusBarUtil;
import com.lvyangai.highopinion.util.ToastUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class PhoneActivity extends BaseActivity {

    ActivityPhoneBinding binding;
    private String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$(?!.*\\s)";//密码验证
    private Context context;
    private Retrofit retrofit;
    private String phone;
    private String regphone="^[1](([3|5|8][\\d])|([4][5,6,7,8,9])|([6][5,6])|([7][3,4,5,6,7,8])|([9][8,9]))[\\d]{8}$";

    private Api api;
    private static final OkHttpClient client = new OkHttpClient.Builder().
            connectTimeout(5, TimeUnit.SECONDS).
            readTimeout(5, TimeUnit.SECONDS).
            writeTimeout(5, TimeUnit.SECONDS).build();
    @Override
    protected void initView() {
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone);
    }

    @Override
    protected void initData() {
        binding.resetPhoneToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        StatusBarUtil.setPaddingSmart(this,binding.resetPhoneToolbar);
    }

    @Override
    protected void dataProcess() {
        retrofit = new Retrofit.Builder()
                .baseUrl(App.API_IP)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);
        binding.userResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submit()){
                    showProDialog();
                    phone = binding.resetPhone.getText().toString();
                    retrofitHttp(phone);
                }
            }
        });
    }

    private void retrofitHttp(String phone) {
        api.registerPhone(phone, MyApplication.getUser()).enqueue(new Callback<ForgetBean>() {
            @Override
            public void onResponse(Call<ForgetBean> call, Response<ForgetBean> response) {
                if (response.isSuccessful()){
                    showResonse(response.body().getMsg());
                }else {
                    ToastUtil.showShortToast("请求失败");
                }
            }

            @Override
            public void onFailure(Call<ForgetBean> call, Throwable t) {
                ToastUtil.showShortToast("请求出错");
                dismissDialog();
            }
        });
    }

    private void showResonse(String msg) {
        dismissDialog();
        ToastUtil.showShortToast(msg);
        if ("手机修改成功".equals(msg)){
            ToastUtil.showShortToast(msg);
            MyApplication.setPhone(phone);
            SPUtils.put("phone",phone);
            finish();
        }
    }

    private boolean submit() {
        String phone = binding.resetPhone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showShortToast("请输入手机号");
            return false;
        }


        if(!phone.matches(regphone)){
            ToastUtil.showShortToast("手机号格式不正确");
            return false;
        }
        return true;
    }

}
