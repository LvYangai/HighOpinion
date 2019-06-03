package com.lvyangai.highopinion.bean;

/**
 * 作者：Created by 吕言盖 (LYG-Pro)
 * 时间： 2019/5/13.
 * 描述：
 * 邮箱：1076977275@qq.com
 */

public class UserItemBean{
    private String itemName;
    private int icon;

    public UserItemBean(String itemName, int icon) {
        this.itemName = itemName;
        this.icon = icon;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
