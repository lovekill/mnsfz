package com.engine.mnsfz.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table IMAGE_BEAN.
 */
public class ImageBean {

    private String title;
    private String href;
    private String src;
    private Long time;

    public ImageBean() {
    }

    public ImageBean(String href) {
        this.href = href;
    }

    public ImageBean(String title, String href, String src, Long time) {
        this.title = title;
        this.href = href;
        this.src = src;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

}