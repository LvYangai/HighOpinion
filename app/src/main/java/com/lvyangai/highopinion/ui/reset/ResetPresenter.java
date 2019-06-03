package com.lvyangai.highopinion.ui.reset;

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
 * 时间： 2019/5/24.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class ResetPresenter implements ResetContract.resetPresenter {
    ResetContract.resetView view;
    private Retrofit retrofit;
    private Api api;
    private static final OkHttpClient client = new OkHttpClient.Builder().
            connectTimeout(5, TimeUnit.SECONDS).
            readTimeout(5, TimeUnit.SECONDS).
            writeTimeout(5, TimeUnit.SECONDS).build();

    public ResetPresenter(ResetContract.resetView view) {
        this.view = view;
        retrofit = new Retrofit.Builder()
                .baseUrl(App.API_IP)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }


    @Override
    public void reset(String user, final String reset) {
        api.resetPassWord(user,reset).enqueue(new Callback<UserRegisterBean>() {
            @Override
            public void onResponse(Call<UserRegisterBean> call, Response<UserRegisterBean> response) {
                if (response.isSuccessful()){
                    view.resetSuccess(response.body().getMsg());
                }else {
                    view.resetFailed("重置密码失败");
                }
            }
            @Override
            public void onFailure(Call<UserRegisterBean> call, Throwable t) {
                view.resetFailed("请求出错");
            }
        });
    }
}
