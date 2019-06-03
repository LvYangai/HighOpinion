package com.lvyangai.highopinion.ui.home.homefragment.like.page;

import com.lvyangai.highopinion.bean.LikePageItemBean;
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
 * 时间： 2019/5/20.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class LikePagePresenter implements LikePageContract.likePagePresenter{
    LikePageContract.likePageView view;
    private Retrofit retrofit;
    private Api api;

    public LikePagePresenter(LikePageContract.likePageView view) {
        this.view = view;
        retrofit = new Retrofit.Builder()
                .baseUrl(App.API_IP)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }

    @Override
    public void getDataList(String type, int userId) {
        api.getLikePage(type,userId).enqueue(new Callback<LikePageItemBean>() {
            @Override
            public void onResponse(Call<LikePageItemBean> call, Response<LikePageItemBean> response) {
                if (response.isSuccessful()){
                    view.retrofitSuccess(response);
                }else {
                    view.retrofitFailed("未查到您收藏的页面");
                }
            }

            @Override
            public void onFailure(Call<LikePageItemBean> call, Throwable t) {
                view.retrofitFailed("请求出错");
            }
        });
    }
}
