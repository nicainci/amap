package com.lm.amap.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.amap.api.navi.model.AMapNaviCross;

/**
 * @Author WWC
 * @Create 2019/3/14
 * @Description 自定义路口放大图（实景图）view
 * Copyright(c) 2017, Zhejiang Yunbo Technology Co.,Ltd. All rights reserved
 */
public class CustomZoomInIntersectionView extends AppCompatImageView {

    public CustomZoomInIntersectionView(Context context) {
        super(context);
    }

    public CustomZoomInIntersectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomZoomInIntersectionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 路口放大图原图
     */
    private Bitmap zoomInBitmap = null;

    /**
     * 将路口放大图类中的bitmap输入
     *
     * @param cross 路口放大图类
     */
    public void setIntersectionBitMap(AMapNaviCross cross) {
        try {
            zoomInBitmap = cross.getBitmap();
            setImageBitmap(zoomInBitmap);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * @param event
     * @return
     * @exclude
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    /**
     * 资源回收
     */
    public void recycleResource() {
        try {
            if (zoomInBitmap != null) {
                zoomInBitmap.recycle();
                zoomInBitmap = null;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
