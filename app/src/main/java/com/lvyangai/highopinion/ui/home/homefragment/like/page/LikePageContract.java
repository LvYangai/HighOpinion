package com.lvyangai.highopinion.ui.home.homefragment.like.page;

import com.lvyangai.highopinion.bean.LikePageItemBean;

import retrofit2.Response;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/20.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class LikePageContract {
    interface likePageView{

        void retrofitSuccess(Response<LikePageItemBean> response);

        void retrofitFailed(String log);
    }
    interface likePagePresenter{

        void getDataList(String type,int userId);

    }
}
