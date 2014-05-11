package com.engine.mnsfz.util;

import android.util.Log;

/**
 * Created by USER on 13-7-9.
 */
public class LogUtil {
   public static  void  e(Class c,Object o){
       Log.e(c.getSimpleName(),o.toString()) ;
   }
    public static  void  v(Class c,Object o){
        Log.v(c.getSimpleName(),o.toString()) ;
    }
    public static  void  i(Class c,Object o){
        Log.e(c.getSimpleName(),o.toString()) ;
    }
    public static  void  d(Class c,Object o){
        Log.d(c.getSimpleName(),o.toString()) ;
    }
}
