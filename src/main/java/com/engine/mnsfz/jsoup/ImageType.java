package com.engine.mnsfz.jsoup;

/**
 * Created by USER on 13-7-13.
 */
public enum ImageType {
   QINCUN(1),SUNMM(2), QIAOPI(3);
    private int value ;
    ImageType(int i) {
        this.value=i ;
    }

    @Override
    public String toString() {
        return  String.valueOf(value) ;
    }
}
