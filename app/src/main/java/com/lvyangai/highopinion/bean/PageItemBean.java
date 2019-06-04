package com.lvyangai.highopinion.bean;

import java.util.List;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/12.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class PageItemBean {


    /**
     * status : 200
     * code : 查询成功
     * page : [{"id":39,"post_user":"好人","post_icon":"http://192.168.0.4:8088/highopinion/data/uploads/20190516/1dd013c8b5374a2f747a79aa8aba6090.jpg","page_title":"一加7 Pro新晋机皇 没什么好说的 这很一加","page_isLike":false,"page_author":17,"page_click":98,"page_url":"index.php/article/39.html","page_thumb":"http://img.cnmo-img.com.cn/1635_600x1000/1634868.jpg","page_date":"2019-05-22 22:41:20","page_like":3,"page_keyword":"","page_comment":4,"page_top":0,"page_recommended":1},{"id":38,"post_user":"好人","post_icon":"http://192.168.0.4:8088/highopinion/data/uploads/20190516/1dd013c8b5374a2f747a79aa8aba6090.jpg","page_title":"跟风老爹鞋，不必买假也能穿上","page_isLike":false,"page_author":17,"page_click":53,"page_url":"index.php/article/38.html","page_thumb":"http://192.168.0.4:8088/highopinion/data/uploads/20190516/90712ee9f84054e258ab5e01724801df.jpg","page_date":"2019-05-22 21:53:13","page_like":1,"page_keyword":"老爹鞋,潮鞋,Nike","page_comment":0,"page_top":1,"page_recommended":1},{"id":37,"post_user":"admin1","post_icon":"http://192.168.0.4:8088/highopinion/data/uploads/20190424/202a274feff4548308795f46a8b12192.jpg","page_title":"置顶加推荐页面","page_isLike":false,"page_author":1,"page_click":6,"page_url":"index.php/article/37.html","page_thumb":"http://192.168.0.4:8088/highopinion/data/uploads/20190514/eeef926720fb8dd7846471cd765a2e1b.png","page_date":"2019-05-21 16:19:24","page_like":0,"page_keyword":"","page_comment":0,"page_top":1,"page_recommended":1},{"id":27,"post_user":"admin1","post_icon":"http://192.168.0.4:8088/highopinion/data/uploads/20190424/202a274feff4548308795f46a8b12192.jpg","page_title":"点点滴滴的","page_isLike":false,"page_author":1,"page_click":15,"page_url":"index.php/article/27.html","page_thumb":"http://192.168.0.4:8088/HighOpinion/data/uploads/20190518/97fcaf96e3d6c5bbb5e7671ad75cbd57.jpg","page_date":"2019-04-24 23:06:37","page_like":4,"page_keyword":"","page_comment":0,"page_top":0,"page_recommended":1}]
     * page_top : [{"top_image":"http://192.168.0.4:8088/highopinion/data/uploads/20190516/90712ee9f84054e258ab5e01724801df.jpg","top_title":"跟风老爹鞋，不必买假也能穿上","top_url":"/index.php/article/38.html","top_click":53,"top_pageId":38},{"top_image":"http://192.168.0.4:8088/highopinion/data/uploads/20190514/eeef926720fb8dd7846471cd765a2e1b.png","top_title":"置顶加推荐页面","top_url":"/index.php/article/37.html","top_click":6,"top_pageId":37}]
     * request_time : 2019-05-22 22:55:28
     */

    private int status;
    private String code;
    private String request_time;
    private List<PageBean> page;
    private List<PageTopBean> page_top;

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

    public List<PageBean> getPage() {
        return page;
    }

    public void setPage(List<PageBean> page) {
        this.page = page;
    }

    public List<PageTopBean> getPage_top() {
        return page_top;
    }

    public void setPage_top(List<PageTopBean> page_top) {
        this.page_top = page_top;
    }

    public static class PageBean {
        /**
         * id : 39
         * post_user : 好人
         * post_icon : http://192.168.0.4:8088/highopinion/data/uploads/20190516/1dd013c8b5374a2f747a79aa8aba6090.jpg
         * page_title : 一加7 Pro新晋机皇 没什么好说的 这很一加
         * page_isLike : false
         * page_author : 17
         * page_click : 98
         * page_url : index.php/article/39.html
         * page_thumb : http://img.cnmo-img.com.cn/1635_600x1000/1634868.jpg
         * page_date : 2019-05-22 22:41:20
         * page_like : 3
         * page_keyword :
         * page_comment : 4
         * page_top : 0
         * page_recommended : 1
         */

        private int id;
        private String post_user;
        private String post_icon;
        private String page_title;
        private boolean page_isLike;
        private int page_author;
        private int page_click;
        private String page_url;
        private String page_thumb;
        private String page_date;
        private int page_like;
        private String page_keyword;
        private int page_comment;
        private int page_top;
        private int page_recommended;
        private String page_type;

        public String getPage_type() {
            return page_type;
        }


        public void setPage_type(String page_type) {
            this.page_type = page_type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPost_user() {
            return post_user;
        }

        public void setPost_user(String post_user) {
            this.post_user = post_user;
        }

        public String getPost_icon() {
            return post_icon;
        }

        public void setPost_icon(String post_icon) {
            this.post_icon = post_icon;
        }

        public String getPage_title() {
            return page_title;
        }

        public void setPage_title(String page_title) {
            this.page_title = page_title;
        }

        public boolean getPage_isLike() {
            return page_isLike;
        }

        public void setPage_isLike(boolean page_isLike) {
            this.page_isLike = page_isLike;
        }

        public int getPage_author() {
            return page_author;
        }

        public void setPage_author(int page_author) {
            this.page_author = page_author;
        }

        public int getPage_click() {
            return page_click;
        }

        public void setPage_click(int page_click) {
            this.page_click = page_click;
        }

        public String getPage_url() {
            return page_url;
        }

        public void setPage_url(String page_url) {
            this.page_url = page_url;
        }

        public String getPage_thumb() {
            return page_thumb;
        }

        public void setPage_thumb(String page_thumb) {
            this.page_thumb = page_thumb;
        }

        public String getPage_date() {
            return page_date;
        }

        public void setPage_date(String page_date) {
            this.page_date = page_date;
        }

        public int getPage_like() {
            return page_like;
        }

        public void setPage_like(int page_like) {
            this.page_like = page_like;
        }

        public String getPage_keyword() {
            return page_keyword;
        }

        public void setPage_keyword(String page_keyword) {
            this.page_keyword = page_keyword;
        }

        public int getPage_comment() {
            return page_comment;
        }

        public void setPage_comment(int page_comment) {
            this.page_comment = page_comment;
        }

        public int getPage_top() {
            return page_top;
        }

        public void setPage_top(int page_top) {
            this.page_top = page_top;
        }

        public int getPage_recommended() {
            return page_recommended;
        }

        public void setPage_recommended(int page_recommended) {
            this.page_recommended = page_recommended;
        }
    }

    public static class PageTopBean {
        /**
         * top_image : http://192.168.0.4:8088/highopinion/data/uploads/20190516/90712ee9f84054e258ab5e01724801df.jpg
         * top_title : 跟风老爹鞋，不必买假也能穿上
         * top_url : /index.php/article/38.html
         * top_click : 53
         * top_pageId : 38
         */

        public String top_image;
        public String top_title;
        public String top_url;
        public int top_click;
        public int top_pageId;

        public String getTop_image() {
            return top_image;
        }

        public void setTop_image(String top_image) {
            this.top_image = top_image;
        }

        public String getTop_title() {
            return top_title;
        }

        public void setTop_title(String top_title) {
            this.top_title = top_title;
        }

        public String getTop_url() {
            return top_url;
        }

        public void setTop_url(String top_url) {
            this.top_url = top_url;
        }

        public int getTop_click() {
            return top_click;
        }

        public void setTop_click(int top_click) {
            this.top_click = top_click;
        }

        public int getTop_pageId() {
            return top_pageId;
        }

        public void setTop_pageId(int top_pageId) {
            this.top_pageId = top_pageId;
        }
    }
}
