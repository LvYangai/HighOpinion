package com.lvyangai.highopinion.bean;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/25.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class ForgetBean {

    /**
     * status : 200
     * code : 请求成功
     * msg : 可以修改密码
     * request_time : 2019-05-25 00:51:37
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
