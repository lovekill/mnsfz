package com.engine.mnsfz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.engine.mnsfz.greendao.DaoMaster;
import com.engine.mnsfz.greendao.DaoSession;


/**
 * Created by Administrator on 14-4-23.
 */
public class DaoManager {
    private Context mContext ;
    private static DaoSession mDaoSession ;
    private static DaoManager instance;
    public static DaoManager getInstance(){
        if(instance==null){
            instance = new DaoManager() ;
        }
        return instance ;
    }
    private DaoManager(){
    }
    public void init(Context context){
        this.mContext = context ;
        SQLiteDatabase db = new DaoMaster.DevOpenHelper(context, "mnsfz", null).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession= daoMaster.newSession();
    }
    public static DaoSession getDaoSession(){
        return  mDaoSession ;
    }
}
