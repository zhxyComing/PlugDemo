package com.app.dixon.studyplug.simpleplug.helper;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;

/**
 * 全路径：com.app.dixon.studyplug
 * 类描述：
 * 创建人：dixon.xu
 * 创建时间：2019/5/2 3:27 PM
 */

public class ApkHelper {

    private static final Map<String, ClassLoader> classLoaderList = new HashMap<>();
    private static final Map<String, Resources> resourceList = new HashMap<>();

    private static final String TAG = ApkHelper.class.getSimpleName();

    /**
     * @param appPath
     * @return 得到对应插件的ClassLoader对象
     */
    public static ClassLoader getDexClassLoader(Context context, String appPath) {
        if (classLoaderList.containsKey(appPath)) {
            return classLoaderList.get(appPath);
        }
        String dexOutFilePath = context.getCacheDir().getAbsolutePath();
        Log.e(TAG, "apk path is " + appPath + ", and dexOutFilePath is" + dexOutFilePath);
        DexClassLoader classLoader = new DexClassLoader(appPath, dexOutFilePath, null, context.getClassLoader());
        classLoaderList.put(appPath, classLoader);
        return classLoader;
    }


    /**
     * @param appPath
     * @return 得到对应插件的Resource对象
     */
    public static Resources getPluginResources(Context context, String appPath) {
        if (resourceList.containsKey(appPath)) {
            return resourceList.get(appPath);
        }
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            //反射调用方法addAssetPath(String path)
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            //将未安装的Apk文件的添加进AssetManager中,第二个参数是apk的路径
            addAssetPath.invoke(assetManager, appPath);
            Resources superRes = context.getResources();
            Resources mResources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
            resourceList.put(appPath, mResources);
            return mResources;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 尝试遍历classLoaderList 找到符合要求的Class
     * 反射调用findClass 不委托双亲查找
     *
     * @param className
     * @return
     */
    public static String tryToFindClass(String className) {
        for (String apkPath : classLoaderList.keySet()) {
            try {
                Method findClass = BaseDexClassLoader.class.getDeclaredMethod("findClass", String.class);
                findClass.setAccessible(true);
                findClass.invoke(classLoaderList.get(apkPath), className);
                return apkPath;
            } catch (Exception e) {
                //NotFound next
                e.printStackTrace();
            }
        }
        return null;
    }
}
