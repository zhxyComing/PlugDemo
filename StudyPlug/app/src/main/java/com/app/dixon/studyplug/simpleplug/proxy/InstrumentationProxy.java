package com.app.dixon.studyplug.simpleplug.proxy;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;

import com.app.dixon.studyplug.simpleplug.helper.ApkHelper;
import com.app.dixon.studyplug.simpleplug.helper.FieldUtil;

//如果插件用的style是android:Theme.Material.Light.DarkActionBar，则在启动前会加载宿主resources的ic_launcher，而因为我们把targetActivity的resources替换为了它apk本身的resources，导致俩者ic_launcher的id不一致，从而crash。
//临时解决：把插件的style修改为Theme.AppCompat.Light.NoActionBar--<item name="android:windowNoTitle">true</item>，不加载其topBar即可。

/**
 * Created by dixon.xu on 2019/2/15.
 */

public class InstrumentationProxy extends Instrumentation {

    private static final String TAG = InstrumentationProxy.class.getSimpleName();

    private Application mApplication;

    public InstrumentationProxy(Application application) {
        this.mApplication = application;
    }

    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        if (intent.getComponent() != null) {
            String apkPath = ApkHelper.tryToFindClass(className);
            Log.e(TAG, "ClassName is " + className + ", and apkPath is " + apkPath);
            if (!TextUtils.isEmpty(apkPath)) {
                Activity activity = super.newActivity(ApkHelper.getDexClassLoader(mApplication, apkPath), className, intent);
                //通过自定义的resources加载目标资源
                //这样TargetActivity使用的就是其Apk本身的资源
                try {
                    FieldUtil.setField(ContextThemeWrapper.class, activity, "mResources", ApkHelper.getPluginResources(mApplication, apkPath));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return activity;
            }
        }
        return super.newActivity(cl, className, intent);
    }
}

