package com.lm.amap.util;

import android.content.Context;

/**
 * @Author WWC
 * @Create 2019/3/12
 * @Description 工具类
 * Copyright(c) 2017, Zhejiang Yunbo Technology Co.,Ltd. All rights reserved
 */
public class Utils {
    private static Context context;

    private Utils() {

    }

    /**
     * 初始化Application Context
     *
     * @param ctx Application Context
     */
    public static void init(Context ctx) {
        context = ctx;
    }

    /**
     * 获取Application Context
     *
     * @return Application Context
     */
    public static Context getContext() {
        if (context == null) throw new IllegalStateException("工具类还未初始化");
        return context;
    }
}
