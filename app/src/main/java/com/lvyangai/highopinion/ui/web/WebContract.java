package com.lvyangai.highopinion.ui.web;

import com.lvyangai.highopinion.bean.ClickLikeBean;
import com.lvyangai.highopinion.bean.CommItemBean;

import retrofit2.Response;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/20.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class WebContract {
    interface webView{

        void initIsLike(boolean flag);

        void retrofitLikeSuccess(String log);

        void retrofitLikeFailed(String log);

        void retrofitDataSuccess(Response<CommItemBean> response);

        void retrofitDataFailed(String msg);

        void retrofitComm(String msg);

    }
    interface webPresenter{


        void getInitLike(int pageid,int uid);

        void setPageLike(String type,int userId,int pageId,int status);

        void getCommList(int pageid);

        void upComm(String type,int userId,int pageId,String commContent);
    }
}
