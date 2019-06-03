package com.lvyangai.highopinion.ui.login;

import com.lvyangai.highopinion.bean.UserLoginBean;

import retrofit2.Response;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2018/12/19.
 * 描述：契约类,连接P层和V层。使接口一目了然
 * 邮箱：1076977275@qq.com
 */

public class LoginContract  {
    interface loginView {

        void accountIsNull();

        void passWordIsNull();

        void loginSuccess(String user,String pass);

        void registerSuccess();

        void forgetSuccess();

        void loginFailed();

        void userLoginSuccess(Response<UserLoginBean> response);

        void userLoginFailed(String msg);

    }

    interface loginPresenter{

        void login(String account, String password);

        void userLogin(String user,String password);

    }
}
