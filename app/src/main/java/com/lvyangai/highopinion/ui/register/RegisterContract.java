package com.lvyangai.highopinion.ui.register;

import retrofit2.Response;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2018/12/19.
 * 描述：契约类
 * 邮箱：1076977275@qq.com
 */

public class RegisterContract {
    interface registerView {

        void resisterSuccess(String msg);

        void registerFailed(String msg);

    }

    interface registerPresenter{

        void register(String user,String pass,String phone);

    }
}
