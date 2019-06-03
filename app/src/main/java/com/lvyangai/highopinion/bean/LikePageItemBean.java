package com.lvyangai.highopinion.bean;

import java.util.List;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/20.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class LikePageItemBean {

    /**
     * status : 200
     * code : 查询成功
     * like : [{"id":22,"like_uid":1,"like_user_name":"好人","like_user_icon":"http://192.168.0.4:8088/highopinion/data/uploads/20190516/1dd013c8b5374a2f747a79aa8aba6090.jpg","like_click":67,"like_thumb":"http://img.cnmo-img.com.cn/1635_600x1000/1634868.jpg","like_title":"一加7 Pro新晋机皇 没什么好说的 这很一加","like_url":"index/Index/article/id/39.html","like_type":"text","like_pageId":39,"like_date":"2019-05-20 15:21:22","like_keywords":""},{"id":20,"like_uid":1,"like_user_name":"好人","like_user_icon":"http://192.168.0.4:8088/highopinion/data/uploads/20190516/1dd013c8b5374a2f747a79aa8aba6090.jpg","like_click":40,"like_thumb":"http://192.168.0.4:8088/highopinion/data/uploads/20190516/90712ee9f84054e258ab5e01724801df.jpg","like_title":"跟风老爹鞋，不必买假也能穿上","like_url":"index/Index/article/id/38.html","like_type":"text","like_pageId":38,"like_date":"2019-05-20 14:35:03","like_keywords":"老爹鞋,潮鞋,Nike"},{"id":2,"like_uid":1,"like_user_name":"admin1","like_user_icon":"http://192.168.0.4:8088/highopinion/data/uploads/20190424/202a274feff4548308795f46a8b12192.jpg","like_click":64,"like_thumb":"http://192.168.0.4:8088/highopinion/data/uploads/20190514/eeef926720fb8dd7846471cd765a2e1b.png","like_title":"d'sa'd","like_url":"index/Index/article/id/3.html","like_type":"text","like_pageId":3,"like_date":"2019-04-09 19:12:13","like_keywords":""},{"id":1,"like_uid":1,"like_user_name":"admin1","like_user_icon":"http://192.168.0.4:8088/highopinion/data/uploads/20190424/202a274feff4548308795f46a8b12192.jpg","like_click":71,"like_thumb":"https://lvyangai-1258941789.cos.ap-shanghai.myqcloud.com/images/image_header1.jpg","like_title":"世界，您好！","like_url":"index/Index/article/id/2.html","like_type":"text","like_pageId":2,"like_date":"2019-04-09 18:44:40","like_keywords":""}]
     * request_time : 2019-05-20 19:18:58
     */

    private int status;
    private String code;
    private String request_time;
    private List<LikeBean> like;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public List<LikeBean> getLike() {
        return like;
    }

    public void setLike(List<LikeBean> like) {
        this.like = like;
    }

    public static class LikeBean {
        /**
         * id : 22
         * like_uid : 1
         * like_user_name : 好人
         * like_user_icon : http://192.168.0.4:8088/highopinion/data/uploads/20190516/1dd013c8b5374a2f747a79aa8aba6090.jpg
         * like_click : 67
         * like_thumb : http://img.cnmo-img.com.cn/1635_600x1000/1634868.jpg
         * like_title : 一加7 Pro新晋机皇 没什么好说的 这很一加
         * like_url : index/Index/article/id/39.html
         * like_type : text
         * like_pageId : 39
         * like_date : 2019-05-20 15:21:22
         * like_keywords :
         */

        private int id;
        private int like_uid;
        private String like_user_name;
        private String like_user_icon;
        private int like_click;
        private String like_thumb;
        private String like_title;
        private String like_url;
        private String like_type;
        private int like_pageId;
        private String like_date;
        private String like_keywords;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLike_uid() {
            return like_uid;
        }

        public void setLike_uid(int like_uid) {
            this.like_uid = like_uid;
        }

        public String getLike_user_name() {
            return like_user_name;
        }

        public void setLike_user_name(String like_user_name) {
            this.like_user_name = like_user_name;
        }

        public String getLike_user_icon() {
            return like_user_icon;
        }

        public void setLike_user_icon(String like_user_icon) {
            this.like_user_icon = like_user_icon;
        }

        public int getLike_click() {
            return like_click;
        }

        public void setLike_click(int like_click) {
            this.like_click = like_click;
        }

        public String getLike_thumb() {
            return like_thumb;
        }

        public void setLike_thumb(String like_thumb) {
            this.like_thumb = like_thumb;
        }

        public String getLike_title() {
            return like_title;
        }

        public void setLike_title(String like_title) {
            this.like_title = like_title;
        }

        public String getLike_url() {
            return like_url;
        }

        public void setLike_url(String like_url) {
            this.like_url = like_url;
        }

        public String getLike_type() {
            return like_type;
        }

        public void setLike_type(String like_type) {
            this.like_type = like_type;
        }

        public int getLike_pageId() {
            return like_pageId;
        }

        public void setLike_pageId(int like_pageId) {
            this.like_pageId = like_pageId;
        }

        public String getLike_date() {
            return like_date;
        }

        public void setLike_date(String like_date) {
            this.like_date = like_date;
        }

        public String getLike_keywords() {
            return like_keywords;
        }

        public void setLike_keywords(String like_keywords) {
            this.like_keywords = like_keywords;
        }
    }
}
