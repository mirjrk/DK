package com.zesium.android.betting.model;

/**
 * Class that defines one navigation drawer item.
 * Created by Ivan Panic on 12/16/2015.
 */
public class NavigationDrawerItem {
    private boolean showNotify;
    private String title;
    private int icon;

    public NavigationDrawerItem() {

    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}