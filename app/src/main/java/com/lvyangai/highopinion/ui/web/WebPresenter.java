package com.lvyangai.highopinion.ui.web;

import android.util.Log;

import com.lvyangai.highopinion.bean.ClickLikeBean;
import com.lvyangai.highopinion.bean.CommItemBean;
import com.lvyangai.highopinion.bean.IsLikeBean;
import com.lvyangai.highopinion.data.Api;
import com.lvyangai.highopinion.data.App;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/20.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class WebPresenter implements WebContract.webPresenter {
    WebContract.webView view;
    private static final String TAG = "WebPresenter";
    private Retrofit retrofit;
    private Api api;

    public WebPresenter(WebContract.webView view) {
        this.view = view;
        retrofit = new Retrofit.Builder()
                .baseUrl(App.API_IP)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }

    @Override
    public void getInitLike(int pageid, int uid) {
        api.isLike(uid,pageid).enqueue(new Callback<IsLikeBean>() {
            @Override
            public void onResponse(Call<IsLikeBean> call, Response<IsLikeBean> response) {
                if (response.isSuccessful()){
                    Log.e(TAG, "onResponse: "+response.body().isMsg() );
                    view.initIsLike(response.body().isMsg());
                }else {
                    view.initIsLike(false);
                }
            }

            @Override
            public void onFailure(Call<IsLikeBean> call, Throwable t) {
                view.initIsLike(false);
            }
        });
    }

    @Override
    public void setPageLike(String type, int userId, int pageId, int status) {
        api.setLike(type,userId,pageId,status).enqueue(new Callback<ClickLikeBean>() {
            @Override
            public void onResponse(Call<ClickLikeBean> call, Response<ClickLikeBean> response) {
                if (response.isSuccessful()){
                    view.retrofitLikeSuccess(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<ClickLikeBean> call, Throwable t) {
                view.retrofitLikeFailed("请求出错");
            }
        });
    }

    @Override
    public void getCommList(int pageid) {
        api.getComm(pageid).enqueue(new Callback<CommItemBean>() {
            @Override
            public void onResponse(Call<CommItemBean> call, Response<CommItemBean> response) {
                if (response.isSuccessful()){
                    view.retrofitDataSuccess(response);
                }else {
                    view.retrofitDataFailed("暂无评论");
                }
            }

            @Override
            public void onFailure(Call<CommItemBean> call, Throwable t) {
                view.retrofitDataFailed("请求出错");
            }
        });
    }

    @Override
    public void upComm(String type, int userId, int pageId, String commContent) {
        api.upComm(type,userId,pageId,commContent).enqueue(new Callback<ClickLikeBean>() {
            @Override
            public void onResponse(Call<ClickLikeBean> call, Response<ClickLikeBean> response) {
                if (response.isSuccessful()){
                    view.retrofitComm(response.body().getMsg());
                }else {
                    view.retrofitComm(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<ClickLikeBean> call, Throwable t) {
                view.retrofitComm("请求出错");
            }
        });
    }
}
