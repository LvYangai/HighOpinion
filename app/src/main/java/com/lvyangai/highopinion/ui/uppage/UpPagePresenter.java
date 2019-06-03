package com.lvyangai.highopinion.ui.uppage;

import android.util.Log;

import com.lvyangai.highopinion.bean.UploadBean;
import com.lvyangai.highopinion.data.Api;
import com.lvyangai.highopinion.data.App;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

public class UpPagePresenter implements UpPageContract.upPagePresenter{
    private static final String TAG = "UpPagePresenter";
    UpPageContract.upPageView view;
    private Retrofit retrofit;
    private Api api;
    public UpPagePresenter(UpPageContract.upPageView view) {
        this.view = view;
        retrofit = new Retrofit.Builder()
                .baseUrl(App.API_UPLOAD)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }


    @Override
    public void upPage(String title, String content, int userId, String keys, MultipartBody.Part[] parts) {
        api.uploadPage(title,content,userId,keys,parts).enqueue(new Callback<UploadBean>() {
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
