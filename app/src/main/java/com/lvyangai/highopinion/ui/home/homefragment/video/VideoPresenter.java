package com.lvyangai.highopinion.ui.home.homefragment.video;

import android.util.Log;

import com.lvyangai.highopinion.bean.ClickLikeBean;
import com.lvyangai.highopinion.bean.CommItemBean;
import com.lvyangai.highopinion.bean.IsLikeBean;
import com.lvyangai.highopinion.bean.PageItemBean;
import com.lvyangai.highopinion.bean.VideoItemBean;
import com.lvyangai.highopinion.data.Api;
import com.lvyangai.highopinion.data.App;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/17.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class VideoPresenter implements VideoContract.videoPresenter{
    private static final String TAG = "VideoPresenter";
    VideoContract.videoView view;
    private Retrofit retrofit;
    private Api api;

    public VideoPresenter(VideoContract.videoView view) {
        this.view = view;
        retrofit = new Retrofit.Builder()
                .baseUrl(App.API_IP)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }


    @Override
    public void setPageLike(String type, int userId, int pageId, int status) {
        api.setLike(type,userId,pageId,status).enqueue(new Callback<ClickLikeBean>() {
            @Override
            public void onResponse(Call<ClickLikeBean> call, Response<ClickLikeBean> response) {
                if (response.isSuccessful()){
                    view.retrofitLikeMessage(response.body().getMsg());
                }else {
                    view.retrofitLikeMessage(response.body().getMsg());
                }
            }

            @Override
            public void onFailure(Call<ClickLikeBean> call, Throwable t) {
                view.retrofitLikeMessage("请求出错");
            }
        });
    }

    @Override
    public void getDataList(int userId) {
        api.getVideo(userId).enqueue(new Callback<VideoItemBean>() {
            @Override
            public void onResponse(Call<VideoItemBean> call, Response<VideoItemBean> response) {
                if (response.isSuccessful()){
                    view.retrofitSuccess(response);
                }else {
                    view.retrofitFailed("请求失败");
                }
            }

            @Override
            public void onFailure(Call<VideoItemBean> call, Throwable t) {
                view.retrofitFailed("请求出错");
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
