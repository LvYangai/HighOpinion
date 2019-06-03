package com.lvyangai.highopinion.ui.upvideo;

import okhttp3.MultipartBody;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/21.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class UpVideoContract {

    interface upVideoView{
        void retrofit(String response);

        void retrofitField();

    }
    interface upPagePresenter{
        void upVideo(String title,int userId, MultipartBody.Part imageParts, MultipartBody.Part file);

    }
}
