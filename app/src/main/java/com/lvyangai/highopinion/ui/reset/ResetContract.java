package com.lvyangai.highopinion.ui.reset;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/24.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class ResetContract {
    interface resetView {

        void inputIsNull(String message);

        void resetSuccess(String msg);

        void resetFailed(String msg);

    }

    interface resetPresenter{

        void reset(String user,String reset);

    }
}
