package com.lvyangai.highopinion.ui.home.homeactivity;

import com.lvyangai.highopinion.bean.UserLoginBean;

import retrofit2.Response;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2018/12/29.
 * 描述：主页面契约
 * 邮箱：1076977275@qq.com
 */

public class HomeContract {
    interface homeView {

        void accountIsNull();

        void homeSuccess();

        void homeFailed();

        void autoLogin(Response<UserLoginBean> response);

        void autoLoginFailed(String msg);

    }

    interface homepagePresenter{

        void homepage();

        void autoLogin(String user,String pass);


    }
}
