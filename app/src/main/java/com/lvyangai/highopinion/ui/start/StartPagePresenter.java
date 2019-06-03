package com.lvyangai.highopinion.ui.start;

import com.lvyangai.highopinion.bean.UserLoginBean;
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
 * 时间： 2019/5/24.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class StartPagePresenter implements StartPageContract.startPagePresenter {
    StartPageContract.startPageView view;
    private Retrofit retrofit;
    private Api api;
    private static final OkHttpClient client = new OkHttpClient.Builder().
            connectTimeout(5, TimeUnit.SECONDS).
            readTimeout(5, TimeUnit.SECONDS).
            writeTimeout(5, TimeUnit.SECONDS).build();
    public StartPagePresenter(StartPageContract.startPageView view) {
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
    public void autoLogin(String user, String pass) {
        api.login(user,pass).enqueue(new Callback<UserLoginBean>() {
            @Override
            public void onResponse(Call<UserLoginBean> call, Response<UserLoginBean> response) {
                if (response.isSuccessful()){
                    view.retrofitSuccess(response);
                }else {
                    view.retrofitFailed("登录失败");
                }
            }

            @Override
            public void onFailure(Call<UserLoginBean> call, Throwable t) {
                view.retrofitFailed("请求出错");
            }
        });
    }
}
