package com.lvyangai.highopinion.bean;

import java.util.List;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/22.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class CommItemBean {

    /**
     * status : 200
     * code : 查询成功
     * comm : [{"id":7,"comm_postId":39,"comm_userId":1,"comm_username":"admin1","comm_user_icon":"http://192.168.0.4:8088/highopinion/data/uploads/20190424/202a274feff4548308795f46a8b12192.jpg","comm_content":"fgggg","comm_date":"2019-05-22 17:26:22"},{"id":14,"comm_postId":39,"comm_userId":1,"comm_username":"admin1","comm_user_icon":"http://192.168.0.4:8088/highopinion/data/uploads/20190424/202a274feff4548308795f46a8b12192.jpg","comm_content":"对不起","comm_date":"2019-05-19 19:01:42"},{"id":13,"comm_postId":39,"comm_userId":1,"comm_username":"admin1","comm_user_icon":"http://192.168.0.4:8088/highopinion/data/uploads/20190424/202a274feff4548308795f46a8b12192.jpg","comm_content":"对不起","comm_date":"2019-05-19 17:28:45"}]
     * request_time : 2019-05-22 17:27:26
     */

    private int status;
    private String code;
    private String request_time;
    private List<CommBean> comm;

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

    public List<CommBean> getComm() {
        return comm;
    }

    public void setComm(List<CommBean> comm) {
        this.comm = comm;
    }

    public static class CommBean {
        /**
         * id : 7
         * comm_postId : 39
         * comm_userId : 1
         * comm_username : admin1
         * comm_user_icon : http://192.168.0.4:8088/highopinion/data/uploads/20190424/202a274feff4548308795f46a8b12192.jpg
         * comm_content : fgggg
         * comm_date : 2019-05-22 17:26:22
         */

        private int id;
        private int comm_postId;
        private int comm_userId;
        private String comm_username;
        private String comm_user_icon;
        private String comm_content;
        private String comm_date;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getComm_postId() {
            return comm_postId;
        }

        public void setComm_postId(int comm_postId) {
            this.comm_postId = comm_postId;
        }

        public int getComm_userId() {
            return comm_userId;
        }

        public void setComm_userId(int comm_userId) {
            this.comm_userId = comm_userId;
        }

        public String getComm_username() {
            return comm_username;
        }

        public void setComm_username(String comm_username) {
            this.comm_username = comm_username;
        }

        public String getComm_user_icon() {
            return comm_user_icon;
        }

        public void setComm_user_icon(String comm_user_icon) {
            this.comm_user_icon = comm_user_icon;
        }

        public String getComm_content() {
            return comm_content;
        }

        public void setComm_content(String comm_content) {
            this.comm_content = comm_content;
        }

        public String getComm_date() {
            return comm_date;
        }

        public void setComm_date(String comm_date) {
            this.comm_date = comm_date;
        }
    }
}
