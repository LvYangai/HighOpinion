package com.lvyangai.highopinion.bean;

import com.lvyangai.highopinion.data.App;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/17.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class VideoItemBean implements Serializable {


    /**
     * status : 200
     * code : 查询成功
     * video : [{"id":61,"post_is_like":false,"post_user":"好人","post_icon":"http://192.168.0.4:8088/highopinion/data/uploads/20190516/1dd013c8b5374a2f747a79aa8aba6090.jpg","video_title":"啦啦啦啦","video_url":"data/media/20190528/4bd1e15a15f8e1122008f1e0fb2ce450.mp4","video_thumb":"http://192.168.0.4:8088/highopinion/data/uploads/20190528/dcf25ad605fce13314c22bf427f7c2ec.png","video_date":"2小时前","video_like":0,"video_comment":0,"video_page_id":61},{"id":58,"post_is_like":false,"post_user":"admin1","post_icon":"http://192.168.0.4:8088/highopinion/data/uploads/20190424/202a274feff4548308795f46a8b12192.jpg","video_title":"哪款削皮刀好一点","video_url":"data/media/20190526/1558891079.mp4","video_thumb":"http://192.168.0.4:8088/highopinion/data/uploads/20190527/ca80c730283076c0335565effce41ec4.PNG","video_date":"昨天01:18","video_like":1,"video_comment":3,"video_page_id":58},{"id":57,"post_is_like":false,"post_user":"admin1","post_icon":"http://192.168.0.4:8088/highopinion/data/uploads/20190424/202a274feff4548308795f46a8b12192.jpg","video_title":"养乐多乳酸菌减肥吗","video_url":"data/media/20190526/1558891002.mp4","video_thumb":"http://192.168.0.4:8088/highopinion/data/uploads/20190527/c2483dd3f6a4ab9f94fe8b59caba88aa.PNG","video_date":"昨天01:16","video_like":1,"video_comment":0,"video_page_id":57},{"id":56,"post_is_like":false,"post_user":"admin1","post_icon":"http://192.168.0.4:8088/highopinion/data/uploads/20190424/202a274feff4548308795f46a8b12192.jpg","video_title":"哪款充电宝值得买","video_url":"data/media/20190526/1558890816.mp4","video_thumb":"http://192.168.0.4:8088/highopinion/data/uploads/20190527/6c526de827c70c185236c88acae85493.PNG","video_date":"昨天01:14","video_like":1,"video_comment":0,"video_page_id":56}]
     * request_time : 2019-05-28 02:21:31
     */

    private int status;
    private String code;
    private String request_time;
    private List<VideoBean> video;

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

    public List<VideoBean> getVideo() {
        return video;
    }

    public void setVideo(List<VideoBean> video) {
        this.video = video;
    }

    public static class VideoBean implements Serializable{
        /**
         * id : 61
         * post_is_like : false
         * post_user : 好人
         * post_icon : http://192.168.0.4:8088/highopinion/data/uploads/20190516/1dd013c8b5374a2f747a79aa8aba6090.jpg
         * video_title : 啦啦啦啦
         * video_url : data/media/20190528/4bd1e15a15f8e1122008f1e0fb2ce450.mp4
         * video_thumb : http://192.168.0.4:8088/highopinion/data/uploads/20190528/dcf25ad605fce13314c22bf427f7c2ec.png
         * video_date : 2小时前
         * video_like : 0
         * video_comment : 0
         * video_page_id : 61
         */

        private int id;
        private boolean post_is_like;
        private String post_user;
        private String post_icon;
        private String video_title;
        private String video_url;
        private String video_thumb;
        private String video_date;
        private int video_like;
        private int video_comment;
        private int video_page_id;

        public VideoBean(int id, boolean post_is_like, String post_user, String post_icon, String video_title, String video_url, String video_thumb, String video_date, int video_like, int video_comment, int video_page_id) {
            this.id = id;
            this.post_is_like = post_is_like;
            this.post_user = post_user;
            this.post_icon = post_icon;
            this.video_title = video_title;
            this.video_url = video_url;
            this.video_thumb = video_thumb;
            this.video_date = video_date;
            this.video_like = video_like;
            this.video_comment = video_comment;
            this.video_page_id = video_page_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isPost_is_like() {
            return post_is_like;
        }

        public void setPost_is_like(boolean post_is_like) {
            this.post_is_like = post_is_like;
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

        public String getVideo_title() {
            return video_title;
        }

        public void setVideo_title(String video_title) {
            this.video_title = video_title;
        }

        public String getVideo_url() {
            return App.API_VIDEO+video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public String getVideo_thumb() {
            return video_thumb;
        }

        public void setVideo_thumb(String video_thumb) {
            this.video_thumb = video_thumb;
        }

        public String getVideo_date() {
            return video_date;
        }

        public void setVideo_date(String video_date) {
            this.video_date = video_date;
        }

        public int getVideo_like() {
            return video_like;
        }

        public void setVideo_like(int video_like) {
            this.video_like = video_like;
        }

        public int getVideo_comment() {
            return video_comment;
        }

        public void setVideo_comment(int video_comment) {
            this.video_comment = video_comment;
        }

        public int getVideo_page_id() {
            return video_page_id;
        }

        public void setVideo_page_id(int video_page_id) {
            this.video_page_id = video_page_id;
        }
    }
}
