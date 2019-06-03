package com.lvyangai.highopinion.ui.forget;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2018/12/21.
 * 描述：契约
 * 邮箱：1076977275@qq.com
 */

public class ForgetContract {
    interface forgetView {

        void inputIsNull(String message);

        void forgetSuccess(String msg);

        void forgetFailed(String msg);

    }

    interface forgetPresenter{

        void forget(String user,String phone);

    }
}
