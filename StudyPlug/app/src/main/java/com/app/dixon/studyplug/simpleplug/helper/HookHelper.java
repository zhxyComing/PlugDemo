package com.app.dixon.studyplug.simpleplug.helper;

import android.app.Application;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.app.dixon.studyplug.simpleplug.proxy.HCallback;
import com.app.dixon.studyplug.simpleplug.proxy.IActivityManagerProxy;
import com.app.dixon.studyplug.simpleplug.proxy.InstrumentationProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

/**
 * 全路径：com.app.dixon.studyplug
 * 类描述：
 * 创建人：dixon.xu
 * 创建时间：2019/5/2 12:59 PM
 */

public class HookHelper {

    public static final String TARGET_INTENT = "target_intent";
    public static final String TAG = HookHelper.class.getSimpleName();

    /**
     * Hook IActivityManager 由于IActivityManagerSingleton是静态成员变量 所以是全局Hook
     *
     * @throws Exception
     */
    public static void hookAMS() throws Exception {
        Object defaultSingleton = null;
        if (Build.VERSION.SDK_INT >= 26) {//版本号 > 8.0
            Class<?> activityManagerClazz = Class.forName("android.app.ActivityManager");
            //获取Singleton<IActivityManager>
            defaultSingleton = FieldUtil.getField(activityManagerClazz, null, "IActivityManagerSingleton");
        } else {
            Class<?> activityManagerNativeClazz = Class.forName("android.app.ActivityManagerNative");
            //获取ActivityManagerNative中的gDefault字段
            defaultSingleton = FieldUtil.getField(activityManagerNativeClazz, null, "gDefault");
        }
        //替换Singleton中的值
        //1.获取class，找到其属性
        Class<?> singletonClazz = Class.forName("android.util.Singleton");
        Field mInstanceField = FieldUtil.getField(singletonClazz, "mInstance");
        //2.获取IActivityManager
        Object iActivityManager = mInstanceField.get(defaultSingleton);
        //3.获取IActivityManager的Proxy代理类
        Class<?> iActivityManagerClazz = Class.forName("android.app.IActivityManager"); //IActivityManager的全路径
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{iActivityManagerClazz},
                new IActivityManagerProxy(iActivityManager));
        //4.替换
        mInstanceField.set(defaultSingleton, proxy);
        Log.e(TAG, "Hook Finish");
    }

    /**
     * 目标是对 ActivityThread 的 mH.callback 进行替换，而 ActivityThread 单进程只有一个，所以是全局替换
     *
     * @throws Exception
     */
    public static void hookHandler() throws Exception {
        //获取ActivityThread.mH
        Class activityThreadClazz = Class.forName("android.app.ActivityThread");
        Object currentActivityThread = FieldUtil.getField(activityThreadClazz, null, "sCurrentActivityThread");
        Field mHField = FieldUtil.getField(activityThreadClazz, "mH");
        Handler mH = (Handler) mHField.get(currentActivityThread);
        //替换H.mCallback
        FieldUtil.setField(Handler.class, mH, "mCallback", new HCallback(mH));
    }

    /**
     * Hook newActivity 使其不加载系统、而加载自定义ClassLoader中的Activity
     *
     * @throws Exception
     */
    public static void hookInstrumentation(Application application) throws Exception {
        Class activityThreadClazz = Class.forName("android.app.ActivityThread");
        Object activityThread = FieldUtil.getField(activityThreadClazz, null, "sCurrentActivityThread");
        FieldUtil.setField(activityThreadClazz, activityThread, "mInstrumentation", new InstrumentationProxy(application));
    }
}
