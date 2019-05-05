package com.app.dixon.studyplug.simpleplug.proxy;

import android.content.Intent;

import com.app.dixon.studyplug.simpleplug.helper.HookHelper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 全路径：com.app.dixon.studyplug
 * 类描述：
 * 创建人：dixon.xu
 * 创建时间：2019/5/2 12:52 PM
 */

public class IActivityManagerProxy implements InvocationHandler {

    private Object mActivityManager;

    public IActivityManagerProxy(Object activityManager) {
        this.mActivityManager = activityManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //执行原方法之前，先执行代理方法
        if ("startActivity".equals(method.getName())) {
            Intent intent = null;
            int index = 0;
            //找到intent参数
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }
            intent = (Intent) args[index];
            Intent subIntent = new Intent();
            String packageName = "com.app.dixon.studyplug";//替换 TargetIntent 的参数
            subIntent.setClassName(packageName, packageName + ".SubActivity");//替换 TargetIntent 的参数
            subIntent.putExtra(HookHelper.TARGET_INTENT, intent);
            args[index] = subIntent;
        }
        return method.invoke(mActivityManager, args);
    }
}
