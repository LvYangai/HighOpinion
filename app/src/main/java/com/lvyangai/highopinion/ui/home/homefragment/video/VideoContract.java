package com.lvyangai.highopinion.ui.home.homefragment.video;


import com.lvyangai.highopinion.bean.CommItemBean;
import com.lvyangai.highopinion.bean.VideoItemBean;

import retrofit2.Response;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/17.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class VideoContract {
    public interface videoView{

        void retrofitSuccess(Response<VideoItemBean> response);

        void retrofitFailed(String log);

        void retrofitLikeMessage(String log);

        void retrofitDataSuccess(Response<CommItemBean> response);

        void retrofitDataFailed(String msg);

        void retrofitComm(String msg);

    }
    public interface videoPresenter{

        void setPageLike(String type,int userId,int pageId,int status);

        void getDataList(int userId);

        void getCommList(int pageid);

        void upComm(String type,int userId,int pageId,String commContent);

    }
}
