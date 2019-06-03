package com.lvyangai.highopinion.ui.home.homefragment.like.media;

import android.util.Log;

import com.lvyangai.highopinion.bean.LikePageItemBean;
import com.lvyangai.highopinion.bean.VideoItemBean;
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
 * 时间： 2019/5/18.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class LikeMediaPresenter implements LikeMediaContract.likeMediaPresenter {
    private static final String TAG = "LikeMediaPresenter";
    LikeMediaContract.likeMediaView view;
    private Retrofit retrofit;
    private Api api;
    private static final OkHttpClient client = new OkHttpClient.Builder().
            connectTimeout(5, TimeUnit.SECONDS).
            readTimeout(5, TimeUnit.SECONDS).
            writeTimeout(5, TimeUnit.SECONDS).build();

    public LikeMediaPresenter(LikeMediaContract.likeMediaView view) {
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
    public void getDataList(String type, int userId) {
        api.getLikeVideo(type,userId).enqueue(new Callback<VideoItemBean>() {
            @Override
            public void onResponse(Call<VideoItemBean> call, Response<VideoItemBean> response) {
                if (response.isSuccessful()){
                    view.retrofitSuccess(response);
                }else {
                    view.retrofitFailed("未查到您喜欢的视频");
                }
            }

            @Override
            public void onFailure(Call<VideoItemBean> call, Throwable t) {
                view.retrofitFailed("请求出错");
            }
        });
    }
}
