package com.lvyangai.highopinion.data;

import com.lvyangai.highopinion.bean.ClickLikeBean;
import com.lvyangai.highopinion.bean.CommItemBean;
import com.lvyangai.highopinion.bean.ForgetBean;
import com.lvyangai.highopinion.bean.IsLikeBean;
import com.lvyangai.highopinion.bean.LikePageItemBean;
import com.lvyangai.highopinion.bean.PageItemBean;
import com.lvyangai.highopinion.bean.UploadBean;
import com.lvyangai.highopinion.bean.UserLoginBean;
import com.lvyangai.highopinion.bean.UserRegisterBean;
import com.lvyangai.highopinion.bean.VideoItemBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/13.
 * 描述：Retrofit接口请求
 * 邮箱：1076977275@qq.com
 */

public interface Api {
    //登陆
    @FormUrlEncoded
    @POST("login")
    Call<UserLoginBean> login(@Field("username") String user, @Field("password") String pass);

    //注册
    @FormUrlEncoded
    @POST("register")
    Call<UserRegisterBean> register(@Field("username") String user, @Field("password") String pass,@Field("phone") String phone);

    //忘记密码
    @FormUrlEncoded
    @POST("getForget")
    Call<ForgetBean> getForget(@Field("user") String user, @Field("phone") String phone);

    //修改手机号
    @FormUrlEncoded
    @POST("registerPhone")
    Call<ForgetBean> registerPhone(@Field("phone") String phone, @Field("username") String user);

    //重置密码
    @FormUrlEncoded
    @POST("resetPassWord")
    Call<UserRegisterBean> resetPassWord(@Field("username") String user,@Field("resetpassword") String resetpass);

    //获取页面
    @FormUrlEncoded
    @POST("getPage")
    Call<PageItemBean> getPage(@Field("pageindex") int pageIndex);

    //获取视频
    @FormUrlEncoded
    @POST("getVideo")
    Call<VideoItemBean> getVideo(@Field("userid") int userId);

    //这个页面是否喜欢
    @FormUrlEncoded
    @POST("isLike")
    Call<IsLikeBean> isLike(@Field("pageuid") int userId,@Field("pageid") int pageId);

    //收藏
    @FormUrlEncoded
    @POST("like")
    Call<ClickLikeBean> setLike(@Field("pagetype") String type, @Field("pageuid") int userId,
                                @Field("pageid") int pageId, @Field("pagestatus") int status);
    //获取收藏的页面
    @FormUrlEncoded
    @POST("getLikePage")
    Call<LikePageItemBean> getLikePage(@Field("pagetype") String type, @Field("pageuid") int userId);

    //获取收藏的视频
    @FormUrlEncoded
    @POST("getLikePage")
    Call<VideoItemBean> getLikeVideo(@Field("pagetype") String type, @Field("pageuid") int userId);

    //上传页面
    @Multipart
    @POST("upPage")
    Call<UploadBean> uploadPage(@Part("pagetitle") String title, @Part("pagecontent") String content,
                                @Part("userid") int userid,@Part("pagekeys") String keys,
                                @Part MultipartBody.Part[] parts);

    //上传视频
    @Multipart
    @POST("upVideo")
    Call<UploadBean> uploadVideo(@Part("videotitle") String title,@Part("userid") int userId,
                                 @Part MultipartBody.Part image,@Part MultipartBody.Part video);

    //获取评论
    @FormUrlEncoded
    @POST("getComm")
    Call<CommItemBean> getComm(@Field("pageid") int pageId);

    //文章和视频评论
    @FormUrlEncoded
    @POST("comm")
    Call<ClickLikeBean> upComm(@Field("commtype") String type,@Field("commuid") int userId,
                                 @Field("commpageid") int pageId,@Field("commcontent") String commContent);

    //搜索
    @FormUrlEncoded
    @POST("getSearch")
    Call<PageItemBean> getSearch(@Field("keywords") String keywords,@Field("userid") int userId);

}
