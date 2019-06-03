package com.lvyangai.highopinion.bean;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/22.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class UserLoginBean {


    /**
     * status : 200
     * code : 登陆成功
     * login : {"id":17,"user_name":"lvyangai","user_username":"好人","user_date":"2019-05-26 18:46:07","user_icon":"http://192.168.0.4:8088/highopinion/data/uploads/20190516/1dd013c8b5374a2f747a79aa8aba6090.jpg","user_jur":6,"user_mobile":"13270901799","user_status":0}
     * request_time : 2019-05-26 21:45:03
     */

    private int status;
    private String code;
    private LoginBean login;
    private String request_time;

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

    public LoginBean getLogin() {
        return login;
    }

    public void setLogin(LoginBean login) {
        this.login = login;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public static class LoginBean {
        /**
         * id : 17
         * user_name : lvyangai
         * user_username : 好人
         * user_date : 2019-05-26 18:46:07
         * user_icon : http://192.168.0.4:8088/highopinion/data/uploads/20190516/1dd013c8b5374a2f747a79aa8aba6090.jpg
         * user_jur : 6
         * user_mobile : 13270901799
         * user_status : 0
         */

        private int id;
        private String user_name;
        private String user_username;
        private String user_date;
        private String user_icon;
        private int user_jur;
        private String user_mobile;
        private int user_status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_username() {
            return user_username;
        }

        public void setUser_username(String user_username) {
            this.user_username = user_username;
        }

        public String getUser_date() {
            return user_date;
        }

        public void setUser_date(String user_date) {
            this.user_date = user_date;
        }

        public String getUser_icon() {
            return user_icon;
        }

        public void setUser_icon(String user_icon) {
            this.user_icon = user_icon;
        }

        public int getUser_jur() {
            return user_jur;
        }

        public void setUser_jur(int user_jur) {
            this.user_jur = user_jur;
        }

        public String getUser_mobile() {
            return user_mobile;
        }

        public void setUser_mobile(String user_mobile) {
            this.user_mobile = user_mobile;
        }

        public int getUser_status() {
            return user_status;
        }

        public void setUser_status(int user_status) {
            this.user_status = user_status;
        }
    }
}
