package com.engine.mnsfz.jsoup;

/**
 * Created by USER on 13-7-13.
 */
public class Page {
    private int pageIndex ;
    private String href ;

    public Page() {
    }

    @Override
    public String toString() {
        return pageIndex+"-->"+href;
    }

    public Page(int pageIndex, String href) {
        this.pageIndex = pageIndex;
        this.href = href;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }
}
