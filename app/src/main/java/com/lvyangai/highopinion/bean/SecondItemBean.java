package com.lvyangai.highopinion.bean;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/12.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class SecondItemBean {
    private String title;
    private int id;

    public SecondItemBean(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
