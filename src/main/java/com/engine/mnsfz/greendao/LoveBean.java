package com.engine.mnsfz.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table LOVE_BEAN.
 */
public class LoveBean {

    private String title;
    private String url;
    private Long time;

    public LoveBean() {
    }

    public LoveBean(String title, String url, Long time) {
        this.title = title;
        this.url = url;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

}
