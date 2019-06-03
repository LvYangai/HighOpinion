package com.lvyangai.highopinion.ui.login;

import android.content.Context;
import android.text.TextUtils;

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
 * Created by LYG on 2018/12/19.
 * 描述:登陆业务相关
 */

public class LoginPresenter implements LoginContract.loginPresenter{
    private LoginContract.loginView mView;

    private final static String TAG = LoginPresenter.class.getSimpleName();
    private Retrofit retrofit;
    private Api api;
    private static final OkHttpClient client = new OkHttpClient.Builder().
            connectTimeout(5, TimeUnit.SECONDS).
            readTimeout(5, TimeUnit.SECONDS).
            writeTimeout(5, TimeUnit.SECONDS).build();

    public LoginPresenter(LoginContract.loginView view) {
        mView = view;
        retrofit = new Retrofit.Builder()
                .baseUrl(App.API_IP)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }
    public void onDestroyView() {
        mView = null;
    }

    @Override
    public void login(String account, String password) {
        if (TextUtils.isEmpty(account)) {
            mView.accountIsNull();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mView.passWordIsNull();
            return;
        }
        mView.loginSuccess(account,password);
    }

    @Override
    public void userLogin(String user, String password) {
        //登陆
        api.login(user,password).enqueue(new Callback<UserLoginBean>() {
            @Override
            public void onResponse(Call<UserLoginBean> call, Response<UserLoginBean> response) {
                if (response.isSuccessful()){
                    mView.userLoginSuccess(response);
                }else {
                    mView.userLoginFailed("登录失败!用户名或密码错误");
                }
            }
            @Override
            public void onFailure(Call<UserLoginBean> call, Throwable t) {
                mView.userLoginFailed("请求出错");
            }
        });
    }

    //注册
    public void register(Context context) {

    }
    public void forget(Context context){

    }
}
