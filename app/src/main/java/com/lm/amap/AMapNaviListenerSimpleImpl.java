package com.lm.amap;

import android.util.Log;

import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.autonavi.tbt.TrafficFacilityInfo;

/**
 * @Author LM
 * @Create 2019/3/12
 * @Description AMapNaviListener
 * Copyright(c) 2017, Zhejiang Yunbo Technology Co.,Ltd. All rights reserved
 */
public class AMapNaviListenerSimpleImpl implements AMapNaviListener {

    public static final String TAG = "navi_listener";

    @Override
    public void onInitNaviFailure() {
        Log.i(TAG, "onInitNaviFailure: ");
    }

    @Override
    public void onInitNaviSuccess() {
        Log.i(TAG, "onInitNaviSuccess: ");
    }

    @Override
    public void onStartNavi(int i) {
        Log.i(TAG, "onStartNavi: ");
    }

    @Override
    public void onTrafficStatusUpdate() {
        Log.i(TAG, "onTrafficStatusUpdate: ");
    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        Log.i(TAG, "onLocationChange: ");
    }

    @Override
    public void onGetNavigationText(int i, String s) {
        Log.i(TAG, "onGetNavigationText: ");
    }

    @Override
    public void onGetNavigationText(String s) {
        Log.i(TAG, "onGetNavigationText: ");
    }

    @Override
    public void onEndEmulatorNavi() {
        Log.i(TAG, "onEndEmulatorNavi: ");
    }

    @Override
    public void onArriveDestination() {
        Log.i(TAG, "onArriveDestination: ");
    }

    @Override
    public void onCalculateRouteFailure(int i) {
        Log.i(TAG, "onCalculateRouteFailure: ");
    }

    @Override
    public void onReCalculateRouteForYaw() {
        Log.i(TAG, "onReCalculateRouteForYaw: ");
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        Log.i(TAG, "onReCalculateRouteForTrafficJam: ");
    }

    @Override
    public void onArrivedWayPoint(int i) {
        Log.i(TAG, "onArrivedWayPoint: ");
    }

    @Override
    public void onGpsOpenStatus(boolean b) {
        Log.i(TAG, "onGpsOpenStatus: ");
    }

    /**
     * 导航引导信息回调 naviinfo 是导航信息类。
     *
     * @param naviInfo 导航信息对象。
     */
    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        Log.i(TAG, "onNaviInfoUpdate: ");
    }

    /**
     * 导航引导信息回调 naviinfo 是导航信息类。
     * <p>
     * 已过时，请使用AMapNaviListener.onNaviInfoUpdate(NaviInfo)
     */
    @Deprecated
    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {
        Log.i(TAG, "onNaviInfoUpdated: ");
    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {
        Log.i(TAG, "updateCameraInfo: ");
    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {
        Log.i(TAG, "updateIntervalCameraInfo: ");
    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {
        Log.i(TAG, "onServiceAreaUpdate: ");
    }

    /**
     * 显示路口放大图回调(实景图)
     *
     * @param aMapNaviCross 路口放大图类，可以获得此路口放大图bitmap
     */
    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        Log.i(TAG, "showCross: ");
    }

    /**
     * 关闭路口放大图回调(实景图)。
     */
    @Override
    public void hideCross() {
        Log.i(TAG, "hideCross: ");
    }

    /**
     * 显示路口放大图回调(模型图)。
     *
     * @param aMapModelCross 模型图数据类,可以获取绘制模型图需要的矢量数据
     */
    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {
        Log.i(TAG, "showModeCross: ");
    }

    /**
     * 关闭路口放大图回调(模型图)。
     */
    @Override
    public void hideModeCross() {
        Log.i(TAG, "hideModeCross: ");
    }

    /**
     * 显示道路信息回调。
     * <p>
     * 建议使用AMapNaviListener.showLaneInfo(AMapLaneInfo) 方法替换
     */
    @Deprecated
    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {
        Log.i(TAG, "showLaneInfo: ");
    }

    /**
     * 显示道路信息回调。
     *
     * @param aMapLaneInfo 道路信息，可获得当前道路信息，可用于用户使用自己的素材完全自定义显示。
     */
    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {
        Log.i(TAG, "showLaneInfo: ");
    }

    @Override
    public void hideLaneInfo() {
        Log.i(TAG, "hideLaneInfo: ");
    }

    /**
     * 算路成功回调
     * <p>
     * 该方法在6.1.0版本废弃，但是还会正常回调，建议使用AMapNaviListener.onCalculateRouteSuccess(AMapCalcRouteResult) 方法替换
     */
    @Deprecated
    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        Log.i(TAG, "onCalculateRouteSuccess: ");
    }

    @Override
    public void notifyParallelRoad(int i) {
        Log.i(TAG, "notifyParallelRoad: ");
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        Log.i(TAG, "OnUpdateTrafficFacility: ");
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
        Log.i(TAG, "OnUpdateTrafficFacility: ");
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
        Log.i(TAG, "OnUpdateTrafficFacility: ");
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
        Log.i(TAG, "updateAimlessModeStatistics: ");
    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
        Log.i(TAG, "updateAimlessModeCongestionInfo: ");
    }

    @Override
    public void onPlayRing(int i) {
        Log.i(TAG, "onPlayRing: ");
    }

    /**
     * 路线规划成功回调，包括算路、导航中偏航、用户改变算路策略、行程点等触发的重算，具体算路结果可以通过AMapCalcRouteResult获取 可以通过CalcRouteResult获取算路错误码、算路类型以及路线id
     *
     * @param aMapCalcRouteResult AMapCalcRouteResult
     */
    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
        Log.i(TAG, "onCalculateRouteSuccess: ");
    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {
        Log.i(TAG, "onCalculateRouteFailure: ");
    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {
        Log.i(TAG, "onNaviRouteNotify: ");
    }
}
