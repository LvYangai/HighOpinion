package com.lvyangai.highopinion.ui.uppage;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/21.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class UpPageContract {
    interface upPageView{

        void retrofit(String response);

        void retrofitField();

    }
    interface upPagePresenter{
        void upPage(String title, String content, int userId, String keys, MultipartBody.Part[] parts);

    }
}
