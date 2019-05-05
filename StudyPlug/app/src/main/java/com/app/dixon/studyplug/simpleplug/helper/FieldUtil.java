package com.app.dixon.studyplug.simpleplug.helper;

import java.lang.reflect.Field;

/**
 * 全路径：com.app.dixon.studyplug
 * 类描述：反射获取成员变量的工具类
 * 创建人：dixon.xu
 * 创建时间：2019/5/2 12:37 PM
 */

public class FieldUtil {

    /**
     * 获取Field对应的值
     *
     * @param clazz
     * @param target
     * @param name
     * @return
     * @throws Exception
     */
    public static Object getField(Class clazz, Object target, String name) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    /**
     * 获取Field
     *
     * @param clazz
     * @param name
     * @return
     * @throws Exception
     */
    public static Field getField(Class clazz, String name) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    /**
     * 给Field赋值
     *
     * @param clazz
     * @param target
     * @param name
     * @param value
     * @throws Exception
     */
    public static void setField(Class clazz, Object target, String name, Object value) throws Exception {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
