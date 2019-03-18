package com.lm.amap.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.navi.model.NaviInfo;
import com.lm.amap.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * @Author LM
 * @Create 2019/3/18
 * @Description 自定义路名剩余距离转向图标示例
 * Copyright(c) 2017, Zhejiang Yunbo Technology Co.,Ltd. All rights reserved
 */
public class CustomNextTurnTipView extends FrameLayout {

    public static final String TAG = "CustomNextTurnTipView";

    private ImageView imageView;
    private TextView textView;

    public CustomNextTurnTipView(@NonNull Context context) {
        this(context, null);
    }

    public CustomNextTurnTipView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomNextTurnTipView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addView(LayoutInflater.from(context).inflate(R.layout.custom_next_turn_tip_view, this, false));
        imageView = findViewById(R.id.image_view);
        textView = findViewById(R.id.text_view);
    }

    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        // 获取导航转向图标
        int iconType = naviInfo.getIconType();
        // 获取转向图标bitmap
        Bitmap iconBitmap = naviInfo.getIconBitmap();
        Log.i(TAG, "onNaviInfoUpdate: iconType=" + iconType + " iconBitmap=" + iconBitmap);
        // 设置导航转向图标
        if (iconBitmap != null) imageView.setImageBitmap(iconBitmap);

        StringBuilder builder = new StringBuilder();
        builder.append("当前路线名称：").append(naviInfo.getCurrentRoadName()).append("\n");
        builder.append("当前的车速：").append(naviInfo.getCurrentSpeed()).append("公里/小时\n");
//        builder.append("当前路段剩余距离：").append(convertMeter2KM(naviInfo.getCurStepRetainDistance())).append("\n");
//        builder.append("当前路段剩余时间：").append(convertSec2MinSp(naviInfo.getCurStepRetainTime())).append("\n");
//        builder.append("下条路名：").append(naviInfo.getNextRoadName()).append("\n");
        builder.append("路线剩余距离：").append(convertMeter2KM(naviInfo.getPathRetainDistance())).append("\n");
        builder.append("路线剩余时间：").append(convertSec2MinSp(naviInfo.getPathRetainTime())).append("\n");

        textView.setText(builder.toString());
    }

    public static String convertSec2MinSp(int sec) {
        String result = "";
        int min = sec / 60;
        int hour = 0;
        if (min < 60) {
            result = min + "分钟";
        } else {
            hour = min / 60;
            min = min % 60;
            result = hour + "小时" + min + "分钟";
        }
        return result;
    }

    public static String convertMeter2KM(float meter) {
        if (meter >= 1000) {
            double dis = 0;
            dis = Math.round(meter / 100d) / 10d;
            DecimalFormat decimalFormat = new DecimalFormat("#0.0");//构造方法的字符格式这里如果小数不足2位,会以0补足.

            String distanceString = decimalFormat.format(dis);//format 返回的是字符串
            return distanceString + "公里";
        } else {
            long dis = 0;
            dis = Math.round(meter);
            return dis + "米";
        }
    }


}
