package com.lvyangai.highopinion.ui.home.homefragment.home.third;

import com.lvyangai.highopinion.bean.PageItemBean;

import retrofit2.Response;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/16.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class ThirdContract {
    interface thirdView{
        void retrofitSuccess(Response<PageItemBean> response);

        void retrofitFailed(String log);
    }
    interface thirdPresenter{
        void getDataList(int pageIndex);
    }
}
