package com.lvyangai.highopinion.ui.home.search;

import com.lvyangai.highopinion.bean.PageItemBean;

import retrofit2.Response;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/6/4.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class SearchContract {
    interface searchView{
        void retrofitSuccess(Response<PageItemBean> response);

        void retrofitFailed(String log);
    }
    interface searchPresenter{

        void getDataList(String key,int userid);

    }
}
