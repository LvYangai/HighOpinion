package com.lvyangai.highopinion;

import android.app.Application;
import android.content.Context;

import com.bilibili.boxing.BoxingCrop;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.lvyangai.highopinion.util.BoxingPicassoLoader;
import com.lvyangai.highopinion.util.BoxingUcrop;
import com.lvyangai.highopinion.util.VideoLRUCacheUtil;

/**
 * Created by zhaoshuang on 2018/11/1.
 */

public class MyApplication extends Application {

    public static MyApplication mContext;
    public static int nowPageIndex = 1;
    public static String user = null;
    public static String username = null;
    public static String password = null;
    public static int userid = 0;
    public static boolean status = false;
    public static String iconUrl = "";
    public static String forgetUser = "";
    public static int jur = 0;
    public static String time;
    public static String phone;

    public static String getTime() {
        return time;
    }

    public static void setTime(String time) {
        MyApplication.time = time;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        MyApplication.phone = phone;
    }

    public static int getJur() {
        return jur;
    }

    public static void setJur(int jur) {
        MyApplication.jur = jur;
    }

    public static String getForgetUser() {
        return forgetUser;
    }

    public static void setForgetUser(String forgetUser) {
        MyApplication.forgetUser = forgetUser;
    }

    public static String getIconUrl() {
        return iconUrl;
    }

    public static void setIconUrl(String iconUrl) {
        MyApplication.iconUrl = iconUrl;
    }

    public static boolean getStatus() {
        return status;
    }

    public static void setStatus(boolean status) {
        MyApplication.status = status;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.mContext = this;
        IBoxingMediaLoader loader = new BoxingPicassoLoader();
        BoxingMediaLoader.getInstance().init(loader);
        BoxingCrop.getInstance().init(new BoxingUcrop());

        //清理超过大小和存储时间的视频缓存文件
        VideoLRUCacheUtil.checkCacheSize(mContext);
    }
    public static Context getAppContext()
    {
        return mContext;
    }

    public static Context getContext(){
        return mContext;
    }

    public static int getNowPageIndex(){
        return nowPageIndex;
    }

    public static void setNowPageIndex(int nowPageIndex) {
        MyApplication.nowPageIndex = nowPageIndex;
    }

    public static String getUser() {
        return user;
    }

    public static String getUsername() {
        return username;
    }


    public static int getUserid() {
        return userid;
    }

    public static void setUser(String user) {
        MyApplication.user = user;
    }

    public static void setUsername(String username) {
        MyApplication.username = username;
    }


    public static void setUserid(int userid) {
        MyApplication.userid = userid;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        MyApplication.password = password;
    }
}
