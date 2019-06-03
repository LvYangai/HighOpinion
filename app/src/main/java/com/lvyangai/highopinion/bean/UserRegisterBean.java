package com.lvyangai.highopinion.bean;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/24.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class UserRegisterBean {

    /**
     * status : 500
     * code : 注册失败
     * msg : 该手机号已被注册
     * request_time : 2019-05-24 02:26:07
     */

    private int status;
    private String code;
    private String msg;
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }
}

