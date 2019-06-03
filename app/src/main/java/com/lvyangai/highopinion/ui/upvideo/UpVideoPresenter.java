package com.lvyangai.highopinion.ui.upvideo;

import com.lvyangai.highopinion.bean.UploadBean;
import com.lvyangai.highopinion.data.Api;
import com.lvyangai.highopinion.data.App;
import com.lvyangai.highopinion.ui.uppage.UpPageContract;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/21.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class UpVideoPresenter implements UpVideoContract.upPagePresenter {
    UpVideoContract.upVideoView view;
    private Retrofit retrofit;
    private Api api;
    public UpVideoPresenter(UpVideoContract.upVideoView view) {
        this.view = view;
        retrofit = new Retrofit.Builder()
                .baseUrl(App.API_UPLOAD)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }

    @Override
    public void upVideo(String title, int userId, MultipartBody.Part imageParts, MultipartBody.Part file) {
        api.uploadVideo(title,userId,imageParts,file).enqueue(new Callback<UploadBean>() {
            @Override
            public void onResponse(Call<UploadBean> call, Response<UploadBean> response) {
                if (response.isSuccessful()){
                    view.retrofit(response.body().getMsg());
                }else {
                    view.retrofitField();
                }
            }

            @Override
            public void onFailure(Call<UploadBean> call, Throwable t) {
                view.retrofitField();
            }
        });
    }
}
