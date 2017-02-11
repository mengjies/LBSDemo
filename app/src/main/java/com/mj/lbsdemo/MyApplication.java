package com.mj.lbsdemo;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by MengJie on 2017/1/11.
 * 获取全局Context
 * 初始化地图SDK
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        //百度地图 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(context);
    }

    public static Context getContext() {
        return context;
    }
}
