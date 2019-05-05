package com.app.dixon.studyplug;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.app.dixon.studyplug.simpleplug.SimplePlug;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start(View view) {
        startActivity(new Intent(MainActivity.this, TestActivity.class));
    }

    public void startOtherApp(View view) {
        try {
            SimplePlug.startPlugActivity(this, "/storage/emulated/0/app-debug.apk", "com.app.dixon.plugin.MainActivity");
        } catch (ClassNotFoundException e) {
            //!!! classNotFound 注意有可能是6.0动态权限问题
            e.printStackTrace();
        }
    }

    public void startSecondPlugApp(View view) {
        try {
            SimplePlug.startPlugActivity(this, "/storage/emulated/0/app-debug1.apk", "com.app.dixon.secondplugin.MainActivity");
        } catch (ClassNotFoundException e) {
            //!!! classNotFound 注意有可能是6.0动态权限问题
            e.printStackTrace();
        }
    }
}
