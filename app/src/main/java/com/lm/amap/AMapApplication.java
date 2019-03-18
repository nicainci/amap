package com.lm.amap;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.lm.amap.util.Utils;

/**
 * @Author WWC
 * @Create 2019/3/6
 * @Description Application
 */
public class AMapApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 解决方法数65535限制
        MultiDex.install(this);
        // 工具类初始化
        Utils.init(this);
    }

}
