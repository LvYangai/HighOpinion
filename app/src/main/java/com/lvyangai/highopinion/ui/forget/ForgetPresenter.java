package com.lvyangai.highopinion.ui.forget;

import android.text.TextUtils;

import com.lvyangai.highopinion.bean.ForgetBean;
import com.lvyangai.highopinion.bean.UserRegisterBean;
import com.lvyangai.highopinion.data.Api;
import com.lvyangai.highopinion.data.App;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2018/12/24.
 * 描述：业务实现
 * 邮箱：1076977275@qq.com
 */

public class ForgetPresenter implements ForgetContract.forgetPresenter {
    ForgetContract.forgetView view;
    private Retrofit retrofit;
    private Api api;
    private static final OkHttpClient client = new OkHttpClient.Builder().
            connectTimeout(5, TimeUnit.SECONDS).
            readTimeout(5, TimeUnit.SECONDS).
            writeTimeout(5, TimeUnit.SECONDS).build();

    public ForgetPresenter(ForgetContract.forgetView view) {
        this.view = view;
        retrofit = new Retrofit.Builder()
                .baseUrl(App.API_IP)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }


    @Override
    public void forget(String user, String phone) {
        api.getForget(user,phone).enqueue(new Callback<ForgetBean>() {
            @Override
            public void onResponse(Call<ForgetBean> call, Response<ForgetBean> response) {
                if (response.isSuccessful()){
                    view.forgetSuccess(response.body().getMsg());
                }else {
                    view.forgetFailed("用户名或手机号有误");
                }
            }

            @Override
            public void onFailure(Call<ForgetBean> call, Throwable t) {
                view.forgetFailed("请求出错");
            }
        });
    }
}
