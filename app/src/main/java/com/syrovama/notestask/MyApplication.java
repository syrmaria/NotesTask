package com.syrovama.notestask;

import android.app.Application;

public class MyApplication extends Application {
    private DBHelper mDBHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        mDBHelper = new DBHelper(this);
    }

    public DBHelper getDBHelper() {
        return mDBHelper;
    }
}
