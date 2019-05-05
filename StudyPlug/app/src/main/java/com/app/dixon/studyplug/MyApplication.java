package com.app.dixon.studyplug;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.app.dixon.studyplug.simpleplug.SimplePlug;

/**
 * 全路径：com.app.dixon.studyplug
 * 类描述：
 * 创建人：dixon.xu
 * 创建时间：2019/5/2 1:21 PM
 */

public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            SimplePlug.hookInit(this);
        } catch (Exception e) {
            Log.e(MyApplication.class.getSimpleName(), "The reason for the error is " + e.toString());
            e.printStackTrace();
        }
    }
}
