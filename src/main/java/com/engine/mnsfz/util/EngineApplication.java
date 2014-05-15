package com.engine.mnsfz.util;

import android.app.Application;
import com.engine.mnsfz.DaoManager;

/**
 * Created by Administrator on 14-5-14.
 */
public class EngineApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DaoManager.getInstance().init(this);
    }
}
