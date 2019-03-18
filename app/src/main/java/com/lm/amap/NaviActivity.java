package com.lm.amap;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CrossOverlay;
import com.amap.api.maps.model.CrossOverlayOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapTrafficStatus;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.RouteOverlayOptions;
import com.amap.api.navi.view.TrafficProgressBar;
import com.autonavi.ae.gmap.gloverlay.GLCrossVector;
import com.lm.amap.view.CustomDriveWayView;
import com.lm.amap.view.CustomNextTurnTipView;
import com.lm.amap.view.CustomOverviewButtonView;
import com.lm.amap.view.CustomTrafficButtonView;
import com.lm.amap.view.CustomZoomInIntersectionView;

import java.io.InputStream;
import java.util.List;

/**
 * @Author LM
 * @Create 2019/3/6
 * @Description 导航Activity
 */
public class NaviActivity extends AppCompatActivity {

    /**
     * 导航View
     */
    private AMapNaviView naviView;
    /**
     * 自定义路口放大实景图View
     */
    private CustomZoomInIntersectionView zoomView;
    /**
     * 自定义导航光柱View
     */
    private TrafficProgressBar trafficProgressBar;
    /**
     * 自定义全览按钮
     */
    private CustomOverviewButtonView overviewButtonView;
    /**
     * 自定义路况按钮
     */
    private CustomTrafficButtonView trafficButtonView;
    /**
     * 自定义路名剩余距离转向图标示例
     */
    private CustomNextTurnTipView nextTurnTipView;
    /**
     * 自定义车道信息View
     */
    private CustomDriveWayView driveWayView;

    /**
     * 导航工具
     */
    private AMapNavi navi;
    /**
     * 导航工具回调
     */
    private AMapNaviListenerSimpleImpl naviListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi);
        // 返回键
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        // 路口放大实景图View
        zoomView = findViewById(R.id.zoom_view);
        // 导航光柱View
        trafficProgressBar = findViewById(R.id.traffic_progress_bar);
        // 全览按钮View
        overviewButtonView = findViewById(R.id.overview_button_view);
        // 自定义路况按钮
        trafficButtonView = findViewById(R.id.traffic_button_view);
        // 自定义路名剩余距离转向图标
        nextTurnTipView = findViewById(R.id.next_turn_tip_view);
        // 自定义车道信息View
        driveWayView = findViewById(R.id.drive_way_view);

        // 导航View
        naviView = findViewById(R.id.navi_view);
        // 导航View生命周期 onCreate
        naviView.onCreate(savedInstanceState);

        // 导航工具
        navi = AMapNavi.getInstance(getApplicationContext());

        // 开始自定义导航界面选项
        enableCustomNaviViewOptions(naviView);

        // 设置自定义路线纹理
        setRouteOverlayOptions(naviView);

        // 设置自定义光柱
        setTrafficProgressBar(navi, trafficProgressBar);

        // 设置自定义路口放大实景图View
        setZoomInIntersectionView(naviView, navi, zoomView);

        // 设置自定义路口放大模型图
        setModelCrossView(naviView, navi);

        // 设置自定义全局预览按钮
        setOverviewButtonView(naviView, navi, overviewButtonView);

        // 设置自定义路况按钮
        setTrafficButtonView(naviView, trafficButtonView);

        // 设置自定义路名剩余距离转向图标示例
        setNextTurnTipView(navi, nextTurnTipView);

        // 自定义车道信息View
        setDriveWayView(navi, driveWayView);

        // 开始导航
        startNavi();
    }

    private CrossOverlay crossOverlay;

    private void setModelCrossView(AMapNaviView naviView, AMapNavi navi) {
        // 设置默认的模型放大图不显示
        AMapNaviViewOptions aMapNaviViewOptions = naviView.getViewOptions();
        aMapNaviViewOptions.setModeCrossDisplayShow(false);
        naviView.setViewOptions(aMapNaviViewOptions);

        navi.addAMapNaviListener(new AMapNaviListenerSimpleImpl() {
            @Override
            public void showModeCross(AMapModelCross aMapModelCross) {
                try {
                    GLCrossVector.AVectorCrossAttr attr = new GLCrossVector.AVectorCrossAttr();
                    // 设置显示区域
                    attr.stAreaRect = new Rect(0, dp2px(getApplicationContext(), 60),
                            getScreenWidth(getApplicationContext()), dp2px(getApplicationContext(), 240));


                    //        attr.stAreaRect = new Rect(0, dp2px(48), nWidth, dp2px(290));
                    attr.stAreaColor = Color.argb(217, 95, 95, 95);/* 背景颜色 */
                    attr.fArrowBorderWidth = dp2px(getApplicationContext(), 22);/* 箭头边线宽度 */
                    attr.stArrowBorderColor = Color.argb(0, 0, 50, 20);/* 箭头边线颜色 */
                    attr.fArrowLineWidth = dp2px(getApplicationContext(), 18);/* 箭头内部宽度 */
                    attr.stArrowLineColor = Color.argb(255, 255, 253, 65);/* 箭头内部颜色 */
                    attr.dayMode = false;
                    attr.fArrowLineWidth = 18;/* 箭头内部宽度 */
                    attr.stArrowLineColor = Color.argb(255, 255, 253, 65);/* 箭头内部颜色 */
                    attr.dayMode = true;


                    InputStream inputStream = getResources().getAssets().open("vector3d_arrow_in.png");
                    crossOverlay = naviView.getMap().addCrossOverlay(new CrossOverlayOptions().setAttribute(attr).setRes(BitmapFactory.decodeStream(inputStream)));

                    crossOverlay.setData(aMapModelCross.getPicBuf1());
                    crossOverlay.setVisible(true);
                    inputStream.close();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void hideModeCross() {
                if (crossOverlay != null) {
                    crossOverlay.setVisible(false);
                }
            }
        });
    }

    /**
     * 自定义车道信息View
     */
    private void setDriveWayView(AMapNavi navi, CustomDriveWayView driveWayView) {
        navi.addAMapNaviListener(new AMapNaviListenerSimpleImpl() {
            @Override
            public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {
                Log.i(TAG, "showLaneInfo: ");
                driveWayView.setVisibility(View.VISIBLE);
                driveWayView.buildDriveWay(aMapLaneInfo);
            }

            @Override
            public void hideLaneInfo() {
                Log.i(TAG, "hideLaneInfo: ");
                driveWayView.hide();
            }
        });
    }

    /**
     * 设置自定义路名剩余距离转向图标
     */
    private void setNextTurnTipView(AMapNavi navi, CustomNextTurnTipView nextTurnTipView) {
        navi.addAMapNaviListener(new AMapNaviListenerSimpleImpl() {
            @Override
            public void onNaviInfoUpdate(NaviInfo naviInfo) {
                nextTurnTipView.onNaviInfoUpdate(naviInfo);
            }
        });
    }

    /**
     * 设置自定义路况按钮
     */
    private void setTrafficButtonView(AMapNaviView naviView, CustomTrafficButtonView trafficButtonView) {
        trafficButtonView.setOnClickListener(v -> naviView.setTrafficLine(!naviView.isTrafficLine()));
    }


    /**
     * 是否是锁车模式
     * <p>
     * 开始导航时默认锁车模式
     */
    private boolean isLockMode;

    /**
     * 设置自定义全局预览按钮
     */
    private void setOverviewButtonView(AMapNaviView naviView, AMapNavi navi, CustomOverviewButtonView overviewButtonView) {
        overviewButtonView.setOnClickListener(v -> {
            if (isLockMode) { //是否是锁车模式
                // 展示全览
                naviView.displayOverview();
            } else {
                // 恢复锁车状态
                naviView.recoverLockMode();
            }
        });
        naviView.setAMapNaviViewListener(new AMapNaviViewListenerSimpleImpl() {
            @Override
            public void onLockMap(boolean isLock) {
                isLockMode = isLock;
                if (isLockMode) {
                    overviewButtonView.setText("预览");
                } else {
                    overviewButtonView.setText("取消");
                }
            }
        });
    }

    /**
     * 设置自定义路口放大实景图View
     */
    private void setZoomInIntersectionView(AMapNaviView naviView, AMapNavi navi, CustomZoomInIntersectionView zoomView) {
        // 设置默认的实景放大图不显示
        AMapNaviViewOptions aMapNaviViewOptions = naviView.getViewOptions();
        aMapNaviViewOptions.setRealCrossDisplayShow(false);
        naviView.setViewOptions(aMapNaviViewOptions);

        // 监听导航时回调
        navi.addAMapNaviListener(new AMapNaviListenerSimpleImpl() {

            @Override
            public void showCross(AMapNaviCross aMapNaviCross) {
                // 设置自定实景放大图
                zoomView.setIntersectionBitMap(aMapNaviCross);
                // 显示自定实景放大图
                zoomView.setVisibility(View.VISIBLE);
            }

            @Override
            public void hideCross() {
                // 隐藏自定实景放大图
                zoomView.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 设置自定义光柱
     */
    private void setTrafficProgressBar(AMapNavi navi, TrafficProgressBar trafficProgressBar) {
        trafficProgressBar.setVisibility(View.VISIBLE);
        trafficProgressBar.setUnknownTrafficColor(ContextCompat.getColor(this, R.color.white));
        trafficProgressBar.setSmoothTrafficColor(ContextCompat.getColor(this, R.color.green));
        trafficProgressBar.setSlowTrafficColor(ContextCompat.getColor(this, R.color.yellow));
        trafficProgressBar.setJamTrafficColor(ContextCompat.getColor(this, R.color.red));
        trafficProgressBar.setVeryJamTrafficColor(ContextCompat.getColor(this, R.color.red_dark));
        navi.addAMapNaviListener(new AMapNaviListenerSimpleImpl() {
            @Override
            public void onNaviInfoUpdate(NaviInfo naviInfo) {
                Log.i(TAG, "onNaviInfoUpdate: 自定义光柱");
                // 自定义光柱进度更新
                int allLength = navi.getNaviPath().getAllLength();
                List<AMapTrafficStatus> trafficStatuses = navi.getTrafficStatuses(0, 0);
                trafficProgressBar.update(allLength, naviInfo.getPathRetainDistance(), trafficStatuses);
            }
        });
    }

    /**
     * 开启自定义导航View选项
     * 隐藏默认的Layout和光柱
     *
     * @param naviView
     */
    private void enableCustomNaviViewOptions(AMapNaviView naviView) {
        // 获取AMapNaviViewOptions
        AMapNaviViewOptions aMapNaviViewOptions = naviView.getViewOptions();
        // 设置导航UI是否显示
        aMapNaviViewOptions.setLayoutVisible(false);
        // 设置路况光柱条是否显示（只适用于驾车导航，需要联网）。
        aMapNaviViewOptions.setTrafficBarEnabled(false);
        // 设置AMapNaviViewOptions
        naviView.setViewOptions(aMapNaviViewOptions);
    }

    /**
     * 设置自定义路线纹理
     *
     * @param naviView 导航View
     */
    private void setRouteOverlayOptions(AMapNaviView naviView) {
        BitmapDescriptor arrowOnTrafficRoute = BitmapDescriptorFactory.fromResource(R.drawable.custtexture_aolr);
        BitmapDescriptor normalRoute = BitmapDescriptorFactory.fromResource(R.drawable.lbs_custtexture_dott_gray);
        BitmapDescriptor smoothTraffic = BitmapDescriptorFactory.fromResource(R.drawable.custtexture_green);
        BitmapDescriptor unknownTraffic = BitmapDescriptorFactory.fromResource(R.drawable.custtexture_no);
        BitmapDescriptor slowTraffic = BitmapDescriptorFactory.fromResource(R.drawable.custtexture_slow);
        BitmapDescriptor jamTraffic = BitmapDescriptorFactory.fromResource(R.drawable.custtexture_bad);
        BitmapDescriptor veryJamTraffic = BitmapDescriptorFactory.fromResource(R.drawable.custtexture_grayred);

        // 获取AMapNaviViewOptions
        AMapNaviViewOptions aMapNaviViewOptions = naviView.getViewOptions();

        // 设置导航界面UI是否显示
        aMapNaviViewOptions.setLayoutVisible(false);

        // 设置是否绘制显示交通路况的线路（彩虹线），拥堵-红色，畅通-绿色，缓慢-黄色，未知-蓝色。
        aMapNaviViewOptions.setTrafficLine(true);

        // RouteOverlayOptions
        RouteOverlayOptions routeOverlayOptions = new RouteOverlayOptions();
        // 转弯箭头颜色
        routeOverlayOptions.setArrowColor(ContextCompat.getColor(this, R.color.blue));
        // 设置3D箭头侧面颜色，只有显示 3D箭头情况加才有效
        routeOverlayOptions.setArrowSideColor(ContextCompat.getColor(this, R.color.blue));
        // 设置是否显示3D箭头，默认显示
        routeOverlayOptions.setTurnArrowIs3D(true);
        // 设置浮于道路上的『小箭头』图标的纹理位图
        routeOverlayOptions.setArrowOnTrafficRoute(arrowOnTrafficRoute.getBitmap());
        // 设置路线的图标
        routeOverlayOptions.setNormalRoute(normalRoute.getBitmap());
        // 设置交通状况情况良好下的纹理位图
        routeOverlayOptions.setSmoothTraffic(smoothTraffic.getBitmap());
        // 设置交通状况未知下的纹理位图
        routeOverlayOptions.setUnknownTraffic(unknownTraffic.getBitmap());
        // 设置交通状况迟缓下的纹理位图
        routeOverlayOptions.setSlowTraffic(slowTraffic.getBitmap());
        // 设置交通状况拥堵下的纹理位图
        routeOverlayOptions.setJamTraffic(jamTraffic.getBitmap());
        // 设置交通状况非常拥堵下的纹理位图
        routeOverlayOptions.setVeryJamTraffic(veryJamTraffic.getBitmap());
        // 设置RouteOverlay的配置选项类
        aMapNaviViewOptions.setRouteOverlayOptions(routeOverlayOptions);

        // 设置AMapNaviViewOptions
        naviView.setViewOptions(aMapNaviViewOptions);
    }

    /**
     * 设置导航配置
     *
     * @param navi 导航工具
     */
    private void setNaviSettings(AMapNavi navi) {
        // 导航工具回调
        naviListener = new AMapNaviListenerSimpleImpl() {
            @Override
            public void onInitNaviFailure() {
                super.onInitNaviFailure();
                Toast.makeText(NaviActivity.this, "导航初始化失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void showCross(AMapNaviCross aMapNaviCross) {
                super.showCross(aMapNaviCross);
                // 自定义路口放大图（实景图）显示
                zoomView.setIntersectionBitMap(aMapNaviCross);
                zoomView.setVisibility(View.VISIBLE);
            }

            @Override
            public void hideCross() {
                super.hideCross();
                // 自定义路口放大图（实景图）隐藏
                zoomView.setVisibility(View.GONE);
            }

            @Override
            public void onNaviInfoUpdate(NaviInfo naviInfo) {
                super.onNaviInfoUpdate(naviInfo);
                // 自定义光柱进度更新
                int allLength = navi.getNaviPath().getAllLength();
                List<AMapTrafficStatus> trafficStatuses = navi.getTrafficStatuses(0, 0);
                trafficProgressBar.update(allLength, naviInfo.getPathRetainDistance(), trafficStatuses);
            }

            @Override
            public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
                super.onCalculateRouteSuccess(aMapCalcRouteResult);
//                if (0 == aMapCalcRouteResult.getErrorCode()) {// 计算路径成功
//                    // 路径id数组
//                    int[] routeIdArray = aMapCalcRouteResult.getRouteid();
//                    if (routeIdArray.length > 0) {
//                        AMapNaviPath path = navi.getNaviPaths().get(routeIdArray[0]);
//                        setNaviRoute(naviView, path);
//                        startNavi();
//                    }
//                } else {
//                    Toast.makeText(NaviActivity.this, aMapCalcRouteResult.getErrorDetail(), Toast.LENGTH_SHORT).show();
//                }
            }
        };
        this.navi.addAMapNaviListener(naviListener);

        // 设置使用内部语音播报, 默认为false, 为true时，用户设置AMapNaviListener.onGetNavigationText(int, java.lang.String) 方法将不再回调
        this.navi.setUseInnerVoice(false);
    }

    /**
     * 开始导航
     */
    private void startNavi() {
        if (BuildConfig.DEBUG) {// DEBUG模式下模拟导航
            // 设置模拟导航速度
            // 驾车默认速度为60km/h,设置的模拟值区间应该在10-120之间.
            // 步行默认速度为20km/h,设置的模拟值区间应该在10-30之间.
            // 骑行默认速度为35km/h,设置的模拟值区间应该在10-50之间.
            navi.setEmulatorNaviSpeed(120);
            // 开始模拟导航
            navi.startNavi(NaviType.EMULATOR);
        } else {// RELEASE模式下正常导航
            // 开始导航
            navi.startNavi(NaviType.GPS);
        }
        // 开始导航默认锁车模式
        isLockMode = true;
    }

    /**
     * 设置导航路径
     *
     * @param naviView 导航View
     * @param naviPath 导航路径
     */
    private void setNaviRoute(AMapNaviView naviView, AMapNaviPath naviPath) {
        // 获取图片文件
        BitmapDescriptor carBitmap = BitmapDescriptorFactory.fromResource(R.drawable.car_arrow_icon);
        BitmapDescriptor startPointBitmap = BitmapDescriptorFactory.fromResource(R.drawable.start);
        BitmapDescriptor endPointBitmap = BitmapDescriptorFactory.fromResource(R.drawable.end);
        BitmapDescriptor wayPointBitmap = BitmapDescriptorFactory.fromResource(R.drawable.way);

        // 获取AMapNaviViewOptions
        AMapNaviViewOptions aMapNaviViewOptions = naviView.getViewOptions();

        // 通过路线是否自动置灰，仅支持驾车导航，可以使用RouteOverlayOptions.setPassRoute(Bitmap)改变纹理
        aMapNaviViewOptions.setAfterRouteAutoGray(true);

        // 设置是否自动改变缩放等级
        aMapNaviViewOptions.setAutoChangeZoom(true);

        // 设置是否自动画路
        aMapNaviViewOptions.setAutoDrawRoute(true);

        // 设置是否开启自动黑夜模式切换，默认为false，不自动切换
        aMapNaviViewOptions.setAutoNaviViewNightMode(false);

        // ======================================================================================
        // 摄像头气泡
        // ======================================================================================
        // 设置路线上的摄像头气泡是否显示
        aMapNaviViewOptions.setCameraBubbleShow(true);
        // 设置摄像头播报是否打开（只适用于驾车导航）。
        aMapNaviViewOptions.setCameraInfoUpdateEnabled(true);
        // 设置导航路线上的摄像头监控图标（只适用于驾车导航）。
//        aMapNaviViewOptions.setMonitorCameraBitmap(Bitmap icon);

        // 步行和骑行导航过程中自车图标是否使用陀螺仪方向，只在骑行导航和步行导航下有效。
        aMapNaviViewOptions.setSensorEnable(false);

        // 设置指南针图标否在导航界面显示，默认显示。true，显示；false，隐藏。
        aMapNaviViewOptions.setCompassEnabled(false);

        // 设置是否显示道路信息view
        aMapNaviViewOptions.setLaneInfoShow(true);

        // 设置导航界面UI是否显示。
        aMapNaviViewOptions.setLayoutVisible(false);

        // 设置是否绘制牵引线（当前位置到目的地的指引线）。
        aMapNaviViewOptions.setLeaderLineEnabled(ContextCompat.getColor(NaviActivity.this, R.color.red));

        // 设置是否显示路口放大图(模型图)
        aMapNaviViewOptions.setModeCrossDisplayShow(true);
        // 设置路口放大模型图的显示位置 第一个参数：横屏路口放大图显示位置 第二个参数： 竖屏路口放大图显示位置
//        aMapNaviViewOptions.setCrossLocation(new Rect(0, 30, 260, 300), new Rect(60, 10, 320, 200));

        // 设置是否显示路口放大图(实景图)
        aMapNaviViewOptions.setRealCrossDisplayShow(true);

        // 设置路线转向箭头隐藏和显示
        aMapNaviViewOptions.setNaviArrowVisible(true);

        //  设置自车位置锁定在屏幕中x,y轴的位置，[0,1]百分比
        aMapNaviViewOptions.setPointToCenter(0.5, 0.5);


        // 设置路况光柱条是否显示（只适用于驾车导航，需要联网）。
        aMapNaviViewOptions.setTrafficBarEnabled(false);

        // 设置[实时交通图层开关按钮]是否显示（只适用于驾车导航，需要联网）。
        aMapNaviViewOptions.setTrafficLayerEnabled(false);

        // 设置导航界面是否显示路线全览按钮。
        aMapNaviViewOptions.setRouteListButtonShow(false);

        // 设置起点位图，须在画路前设置
        aMapNaviViewOptions.setStartPointBitmap(startPointBitmap.getBitmap());

        // 设置终点位图，须在画路前设置
        aMapNaviViewOptions.setEndPointBitmap(endPointBitmap.getBitmap());

        // 设置导航过程中的途经点位图，须在画路前设置
        aMapNaviViewOptions.setWayPointBitmap(wayPointBitmap.getBitmap());

        //
        aMapNaviViewOptions.setCarBitmap(carBitmap.getBitmap());

        // 设置导航状态下屏幕是否一直开启。
        aMapNaviViewOptions.setScreenAlwaysBright(true);

        // 偏航时是否重新计算路径(计算路径需要联网）。
        aMapNaviViewOptions.setReCalculateRouteForYaw(true);

        // 设置菜单按钮是否在导航界面显示。
        aMapNaviViewOptions.setSettingMenuEnabled(false);

        // 设置倾角,取值范围[0-60] 倾角为0时地图模式是2D模式。
        aMapNaviViewOptions.setTilt(0);

        // 设置交通播报是否打开（只适用于驾车导航，需要联网）。
        aMapNaviViewOptions.setTrafficInfoUpdateEnabled(true);

        // 设置是否绘制显示交通路况的线路（彩虹线），拥堵-红色，畅通-绿色，缓慢-黄色，未知-蓝色。
        aMapNaviViewOptions.setTrafficLine(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 导航View生命周期 onResume
        naviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 导航View生命周期 onPause
        naviView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 导航View生命周期 onDestroy
        naviView.onDestroy();
        zoomView.recycleResource();
        navi.stopNavi();
        navi.removeAMapNaviListener(naviListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 导航View生命周期 onSaveInstanceState
        naviView.onSaveInstanceState(outState);
    }

    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    private int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        return width;
    }
}
