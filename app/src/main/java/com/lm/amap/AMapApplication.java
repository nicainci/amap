package com.lm.amap;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * @Author WWC
 * @Create 2019/3/6
 * @Description Application
 * Copyright(c) 2017, Zhejiang Yunbo Technology Co.,Ltd. All rights reserved
 */
public class AMapApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 解决方法数65535限制
        MultiDex.install(this);
    }

}
