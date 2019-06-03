package com.lvyangai.highopinion.ui.home.homefragment.home.third;

import com.lvyangai.highopinion.bean.PageItemBean;
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
 * 时间： 2019/5/16.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class ThirdPresenter implements ThirdContract.thirdPresenter {
    ThirdContract.thirdView view;
    private Retrofit retrofit;
    private Api api;
    private static final OkHttpClient client = new OkHttpClient.Builder().
            connectTimeout(5, TimeUnit.SECONDS).
            readTimeout(5, TimeUnit.SECONDS).
            writeTimeout(5, TimeUnit.SECONDS).build();
    public ThirdPresenter(ThirdContract.thirdView view) {
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
    public void getDataList(int pageIndex) {
        api.getPage(pageIndex).enqueue(new Callback<PageItemBean>() {
            @Override
            public void onResponse(Call<PageItemBean> call, Response<PageItemBean> response) {
                if (response.isSuccessful()){
                    view.retrofitSuccess(response);
                }else {
                    view.retrofitFailed("暂无数据");
                }
            }

            @Override
            public void onFailure(Call<PageItemBean> call, Throwable t) {
                view.retrofitFailed("请求错误");
            }
        });
    }
}
