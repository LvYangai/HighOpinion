package com.lvyangai.highopinion.ui.home.homeactivity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.R;
import com.lvyangai.highopinion.bean.UserLoginBean;
import com.lvyangai.highopinion.data.Api;
import com.lvyangai.highopinion.data.App;
import com.lvyangai.highopinion.ui.menu.MyPopupWindow;
import com.lvyangai.highopinion.ui.uppage.UpPageActivity;
import com.lvyangai.highopinion.ui.upvideo.UpVideoActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2018/12/29.
 * 描述：主页业务
 * 邮箱：1076977275@qq.com
 */

public class HomePresenter implements HomeContract.homepagePresenter {
    private static final String TAG = "HomePresenter";
    HomeContract.homeView view;
    private Fragment mCurFragment = new Fragment();
    private Retrofit retrofit;
    private Api api;
    public HomePresenter(HomeContract.homeView view) {
        this.view = view;
        retrofit = new Retrofit.Builder()
                .baseUrl(App.API_IP)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }


    @Override
    public void homepage() {

    }

    @Override
    public void autoLogin(String user, String pass) {
        api.login(user,pass).enqueue(new Callback<UserLoginBean>() {
            @Override
            public void onResponse(Call<UserLoginBean> call, Response<UserLoginBean> response) {
                if (response.isSuccessful()){
                    view.autoLogin(response);
                }else {
                    view.autoLoginFailed("登陆失败");
                }
            }

            @Override
            public void onFailure(Call<UserLoginBean> call, Throwable t) {
                    view.autoLoginFailed("请求出错");
            }
        });
    }


    public void showMenu(final Activity activity){
        final MyPopupWindow mPopupWindow =new MyPopupWindow(activity, new MyPopupWindow.OnPopWindowClickListener() {
            @Override
            public void onPopWindowClickListener(View v) {
                switch (v.getId()){
                    case R.id.iv_push_photo:
                        activity.startActivity(new Intent(activity, UpPageActivity.class));
                        break;
                    case R.id.iv_push_resale:
                        activity.startActivity(new Intent(activity, UpVideoActivity.class));
                        break;
                }

            }
        });
        mPopupWindow.show();


    }
}
