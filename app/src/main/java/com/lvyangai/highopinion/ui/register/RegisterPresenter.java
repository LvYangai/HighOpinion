package com.lvyangai.highopinion.ui.register;

import android.text.TextUtils;
import android.util.Log;

import com.lvyangai.highopinion.bean.UserLoginBean;
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
 * 时间： 2018/12/19.
 * 描述：手机注册业务实现
 * 邮箱：1076977275@qq.com
 */

public class RegisterPresenter implements RegisterContract.registerPresenter {
    private static final String TAG = "RegisterPresenter";
    RegisterContract.registerView mView;
    private Retrofit retrofit;
    private Api api;
    private static final OkHttpClient client = new OkHttpClient.Builder().
            connectTimeout(5, TimeUnit.SECONDS).
            readTimeout(5, TimeUnit.SECONDS).
            writeTimeout(5, TimeUnit.SECONDS).build();

    public RegisterPresenter(RegisterContract.registerView view) {
        mView = view;
        retrofit = new Retrofit.Builder()
                .baseUrl(App.API_IP)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }


    @Override
    public void register(String user, String pass, String phone) {
        Log.e(TAG, "register: "+user+"   "+pass +"   "+phone);
        api.register(user,pass,phone).enqueue(new Callback<UserRegisterBean>() {
            @Override
            public void onResponse(Call<UserRegisterBean> call, Response<UserRegisterBean> response) {
                if (response.isSuccessful()){
                    mView.resisterSuccess(response.body().getMsg());
                }else {
                    mView.registerFailed("注册失败");
                }
            }

            @Override
            public void onFailure(Call<UserRegisterBean> call, Throwable t) {
                mView.registerFailed("请求超时");
            }
        });
    }
}
