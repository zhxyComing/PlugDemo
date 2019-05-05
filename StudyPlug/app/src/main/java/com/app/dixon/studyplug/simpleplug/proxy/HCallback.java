package com.app.dixon.studyplug.simpleplug.proxy;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.app.dixon.studyplug.simpleplug.helper.FieldUtil;
import com.app.dixon.studyplug.simpleplug.helper.HookHelper;

/**
 * 全路径：com.app.dixon.studyplug
 * 类描述：
 * 创建人：dixon.xu
 * 创建时间：2019/5/2 1:53 PM
 */

public class HCallback implements Handler.Callback {

    public static final int LAUNCH_ACTIVITY = 100;

    Handler mHandler;

    public HCallback(Handler handler) {
        mHandler = handler;
    }

    @Override
    public boolean handleMessage(Message msg) {
        //执行原handleMessage方法之前执行Hook的HandleMessage
        if (msg.what == LAUNCH_ACTIVITY) {
            Object r = msg.obj;
            try {
                //获取之前消息中的真实Intent
                Intent intent = (Intent) FieldUtil.getField(r.getClass(), r, "intent");
                Intent target = intent.getParcelableExtra(HookHelper.TARGET_INTENT);
                if (target != null) {
                    FieldUtil.setField(r.getClass(), r, "intent", target);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mHandler.handleMessage(msg);
        return true;
    }
}
