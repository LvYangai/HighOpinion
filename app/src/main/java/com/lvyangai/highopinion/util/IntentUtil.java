package com.lvyangai.highopinion.util;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lvyangai.highopinion.MyApplication;
import com.lvyangai.highopinion.activity.VideoDetailsActivity;
import com.lvyangai.highopinion.bean.VideoItemBean;
import com.lvyangai.highopinion.ui.web.WebActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoshuang on 2018/11/12.
 */

public class IntentUtil {

    public static final String INTENT_DATA_LIST = "intent_data_list";
    public static final String INTENT_PLAY_POSITION = "intent_play_position";
    public static final String INTENT_WEB_URL = "intent_web_url";
    public static final String INTENT_WEB_TITLE = "intent_web_title";

    public static void gotoVideoDetailsActivity(Activity activity, List<VideoItemBean.VideoBean> dataList, int playPosition, View animationView){

        Intent intent = new Intent(activity, VideoDetailsActivity.class);
        intent.putExtra(INTENT_DATA_LIST, (Serializable) dataList);
        intent.putExtra(INTENT_PLAY_POSITION, playPosition);

        ActivityOptionsCompat compat = ActivityOptionsCompat.makeClipRevealAnimation(animationView, 0, 0, animationView.getWidth(), animationView.getHeight());
        ActivityCompat.startActivity(activity, intent, compat.toBundle());
    }
    public static void gotoVideoDetailsContext(Context context, List<VideoItemBean.VideoBean> dataList, int playPosition, View animationView){

        Intent intent = new Intent(context, VideoDetailsActivity.class);
        intent.putExtra(INTENT_DATA_LIST, (Serializable) dataList);
        intent.putExtra(INTENT_PLAY_POSITION, playPosition);

        ActivityOptionsCompat compat = ActivityOptionsCompat.makeClipRevealAnimation(animationView, 0, 0, animationView.getWidth(), animationView.getHeight());
        ActivityCompat.startActivity(context, intent, compat.toBundle());
    }

    public static void gotoWebViewContext(Context context, String url, String title){

        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(INTENT_WEB_TITLE, title);
        intent.putExtra(INTENT_WEB_URL, url);
//        ActivityOptionsCompat compat = ActivityOptionsCompat.makeClipRevealAnimation(context, 0, 0, animationView.getWidth(), animationView.getHeight());
//        ActivityCompat.startActivity(context,intent, );

    }
}
