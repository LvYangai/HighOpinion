package com.lvyangai.highopinion.ui.home.homefragment.like.media;

import com.lvyangai.highopinion.bean.VideoItemBean;

import retrofit2.Response;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/18.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class LikeMediaContract {
    interface likeMediaView{

        void retrofitSuccess(Response<VideoItemBean> response);

        void retrofitFailed(String log);
    }
    interface likeMediaPresenter{

        void getDataList(String type,int userId);

    }
}
