package com.app.dixon.studyplug.simpleplug;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.app.dixon.studyplug.simpleplug.helper.ApkHelper;
import com.app.dixon.studyplug.simpleplug.helper.HookHelper;

/**
 * 全路径：com.app.dixon.studyplug.simpleplug
 * 类描述：插件化演示程序 实际场景还有很多地方需要兼容
 * 创建人：dixon.xu
 * 创建时间：2019/5/5 10:05 AM
 */

public class SimplePlug {

    public static void hookInit(Application application) throws Exception {
        HookHelper.hookAMS();
        HookHelper.hookHandler();
        HookHelper.hookInstrumentation(application);
    }

    public static void startPlugActivity(Context context, String apkPath, String activityClass) throws ClassNotFoundException {
        ClassLoader loader = ApkHelper.getDexClassLoader(context, apkPath);
        Class<?> targetClass = loader.loadClass(activityClass);
        Intent intent = new Intent(context, targetClass);
        context.startActivity(intent);
    }
}
