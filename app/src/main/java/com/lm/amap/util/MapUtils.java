package com.lm.amap.util;

import android.location.Location;

import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;

/**
 * @Author WWC
 * @Create 2019/3/12
 * @Description 地图工具类
 * Copyright(c) 2017, Zhejiang Yunbo Technology Co.,Ltd. All rights reserved
 */
public class MapUtils {
    //===============================
    // 转成LatLng
    //===============================
    public static LatLng toLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static LatLng toLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    public static LatLng toLatLng(NaviLatLng naviLatLng) {
        return new LatLng(naviLatLng.getLatitude(), naviLatLng.getLongitude());
    }

    //===============================
    // 转成NaviLatLng
    //===============================
    public static NaviLatLng toNaviLatLng(Location location) {
        return new NaviLatLng(location.getLatitude(), location.getLongitude());
    }

    public static NaviLatLng toNaviLatLng(LatLng latLng) {
        return new NaviLatLng(latLng.latitude, latLng.longitude);
    }

    public static NaviLatLng toNaviLatLng(LatLonPoint latLonPoint) {
        return new NaviLatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());

    }

}
