package com.lvyangai.highopinion.bean;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/20.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class IsLikeBean {

    /**
     * status : 200
     * code : 查询成功
     * msg : true
     * request_time : 2019-05-20 13:50:02
     */

    private int status;
    private String code;
    private boolean msg;
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

    public boolean isMsg() {
        return msg;
    }

    public void setMsg(boolean msg) {
        this.msg = msg;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }
}
