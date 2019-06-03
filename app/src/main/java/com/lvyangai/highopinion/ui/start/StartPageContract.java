package com.lvyangai.highopinion.ui.start;

import com.lvyangai.highopinion.bean.UserLoginBean;

import retrofit2.Response;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/24.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class StartPageContract {
    interface startPageView{
        void retrofitSuccess(Response<UserLoginBean> response);
        void retrofitFailed(String msg);
    }
    interface startPagePresenter{
        void autoLogin(String user,String pass);
    }
}
