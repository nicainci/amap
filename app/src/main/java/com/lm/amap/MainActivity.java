package com.lm.amap;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.model.RouteOverlayOptions;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.help.Tip;
import com.lm.amap.util.KeyboardUtils;
import com.lm.amap.util.MapUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * @Author LM
 * @Create 2019/3/6
 * @Description MainActivity
 */
public class MainActivity extends AppCompatActivity {

    /**
     * 搜索输入View
     */
    private AutoCompleteTextView etSearch;
    /**
     * 导航相关操作按钮父View
     */
    private ConstraintLayout clNaviBtn;
    /**
     * 地图View
     */
    private TextureMapView textureMapView;
    /**
     * 地图工具
     */
    private AMap map;
    /**
     * 导航工具
     */
    private AMapNavi navi;
    /**
     * 终点Marker
     */
    private Marker endMarker;
    /**
     * 保存当前算好的路线
     */
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<>();
    /**
     * 导航工具回调
     */
    private AMapNaviListenerSimpleImpl naviListener;

    /**
     * 需要的权限数组
     */
    private String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    /**
     * 检查权限时的 REQUEST CODE
     */
    private int REQUEST_CODE_PERMISSON = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 导航相关操作按钮父View
        clNaviBtn = findViewById(R.id.cl_navi_btn);
        // 搜索输入框
        etSearch = findViewById(R.id.et_search);
        // 地图View
        textureMapView = findViewById(R.id.texture_map_view);
        textureMapView.onCreate(savedInstanceState);

        // 获取导航工具
        navi = AMapNavi.getInstance(getApplicationContext());

        // 设置搜索输入框
        setSearchView();
        // 设置按钮事件
        setBtnEvent();

        if (checkPermissions(needPermissions)) {
            // 设置地图View
            setMapView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSON) {
            if (verifyPermissions(grantResults)) {
                // 设置地图View
                setMapView();
            } else {
                Toast.makeText(this, "权限不足", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        textureMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        textureMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        routeOverlays.clear();
        navi.removeAMapNaviListener(naviListener);
        navi.destroy();
        textureMapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        textureMapView.onSaveInstanceState(outState);
    }

    /**
     * 设置按钮事件
     */
    private void setBtnEvent() {
        findViewById(R.id.btn_change_route).setOnClickListener(v -> changeRoute());
        findViewById(R.id.btn_start_navi).setOnClickListener(v -> startNavi());
    }

    /**
     * 设置搜索输入框
     */
    private void setSearchView() {
        etSearch.setAdapter(new SearchAdapter(this));
        etSearch.setOnClickListener(v -> {
            etSearch.setFocusable(true);
            etSearch.setFocusableInTouchMode(true);
        });
        etSearch.setOnItemClickListener((parent, view, position, id) -> {

            // 清除路径
            clearRoute();
            // 隐藏导航相关按钮
            clNaviBtn.setVisibility(View.GONE);

            Tip tip = (Tip) etSearch.getAdapter().getItem(position);
            if (TextUtils.isEmpty(tip.getPoiID()) && null == tip.getPoint()) {
                // Tip 的 getPoiID() 返回空，并且 getPoint() 也返回空时，表示该提示词不是一个真实存在的 POI，这时区域、经纬度参数都是空的，此时可根据该提示词进行POI关键词搜索

            } else if (!TextUtils.isEmpty(tip.getPoiID()) && null == tip.getPoint()) {
                // Tip 的 getPoiID() 返回不为空，但 getPoint() 返回空时，表示该提示词是一个公交线路名称，此时用这个id进行公交线路查询。

            } else if (!TextUtils.isEmpty(tip.getPoiID()) && null != tip.getPoint()) {
                // Tip 的 getPoiID() 返回不为空，且 getPoint() 也不为空时，表示该提示词一个真实存在的POI，可直接显示在地图上。

                // 设置终点
                setEndPoint(map, tip);
            }
            etSearch.setText(tip.getName());

            etSearch.setFocusable(false);
            etSearch.setFocusableInTouchMode(false);
            KeyboardUtils.hideSoftInput(etSearch);
        });
    }

    /**
     * 设置路径终点
     *
     * @param map 地图工具
     * @param tip 终点Tip
     */
    private void setEndPoint(AMap map, Tip tip) {
        // Marker覆盖物选项
        MarkerOptions markerOptions = new MarkerOptions();
        // 设置Marker覆盖物的图标。相同图案的 icon 的 Marker 最好使用同一个 BitmapDescriptor 对象以节省内存空间。
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.end));
        // 设置多少帧刷新一次图片资源，Marker动画的间隔时间，值越小动画越快。
        markerOptions.period(20);
        // 设置是否开启 marker 覆盖物近大远小效果，默认开启。在marker初始化时使用，当地图倾斜时，远方的标记变小，附近的标记变大。
        markerOptions.perspective(false);
        // 设置Marker覆盖物的位置坐标。Marker经纬度坐标不能为Null，坐标无默认值。
        markerOptions.position(MapUtils.toLatLng(tip.getPoint()));
        // 设置Marker覆盖物的InfoWindow是否允许显示,默认为true
        markerOptions.infoWindowEnable(true);
        // 设置 Marker覆盖物 的标题
        markerOptions.title(tip.getName());
        // 设置 Marker覆盖物的 文字描述
        markerOptions.snippet(null);
        // 设置Marker覆盖物是否平贴地图。
        markerOptions.setFlat(true);
        // 设置Marker覆盖物的透明度，透明度范围[0,1] 1为不透明
        markerOptions.alpha(1);
        // 设置Marker覆盖物的图片旋转角度，从正北开始，逆时针计算。
        markerOptions.rotateAngle(0);
        // 设置Marker覆盖物的坐标是否是Gps，默认为false。
        markerOptions.setGps(false);
        // 设置Marker覆盖物是否可见。
        markerOptions.visible(true);
        // 设置Marker覆盖物是否可拖拽。
        markerOptions.draggable(false);

        // 如果存在一个终点Marker，移除终点Marker
        if (endMarker != null) endMarker.remove();

        // 添加终点Marker
        endMarker = map.addMarker(markerOptions);

        // 经纬度划分的一个矩形区域
        LatLngBounds latLngBounds = LatLngBounds.builder()
                .include(endMarker.getPosition())
                .include(MapUtils.toLatLng(map.getMyLocation()))
                .build();
        // 设置显示在规定屏幕范围内的地图经纬度范围 padding-->l r t b
        map.moveCamera(CameraUpdateFactory.newLatLngBoundsRect(latLngBounds, 100, 100, 300, 300));

        // Marker点击事件
        map.setOnMarkerClickListener(marker -> {
            if (marker.getId().equals(endMarker.getId())) {

                List<NaviLatLng> sList = new ArrayList<>();
                sList.add(MapUtils.toNaviLatLng(map.getMyLocation()));

                List<NaviLatLng> eList = new ArrayList<>();
                eList.add(MapUtils.toNaviLatLng(endMarker.getPosition()));

                calRoute(navi, sList, eList);
                return true;
            }
            return false;
        });

    }

    /**
     * 计算路径
     *
     * @param navi  AMapNavi
     * @param sList 起点List
     * @param eList 终点List
     */
    private void calRoute(AMapNavi navi, List<NaviLatLng> sList, List<NaviLatLng> eList) {
        // 计算路径策略
        int strategy = 0;
        try {
            //进行计算路径策略转换，将传入的特定规则转换成PathPlanningStrategy的枚举值
            strategy = navi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 计算路径
        navi.calculateDriveRoute(sList, eList, null, strategy);
        // 导航工具回调
        naviListener = new AMapNaviListenerSimpleImpl() {
            @Override
            public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
                if (0 == aMapCalcRouteResult.getErrorCode()) {// 计算路径成功
                    // 绘制路径
                    drawRoutes(aMapCalcRouteResult);
                } else {
                    Toast.makeText(MainActivity.this, aMapCalcRouteResult.getErrorDetail(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        navi.addAMapNaviListener(naviListener);

    }

    /**
     * 清除路径
     */
    private void clearRoute() {
        for (int i = 0; i < routeOverlays.size(); i++) {
            RouteOverLay routeOverlay = routeOverlays.valueAt(i);
            //移除RouteOverlay上绘制的路线。
            routeOverlay.removeFromMap();
        }
        routeOverlays.clear();
    }

    /**
     * 绘制路径
     *
     * @param result AMapCalcRouteResult 计算路径结果
     */
    private void drawRoutes(AMapCalcRouteResult result) {
        // 获取图片文件
        BitmapDescriptor arrowOnTrafficRoute = BitmapDescriptorFactory.fromResource(R.drawable.custtexture_aolr);
        BitmapDescriptor startPointBitmap = BitmapDescriptorFactory.fromResource(R.drawable.start);
        BitmapDescriptor endPointBitmap = BitmapDescriptorFactory.fromResource(R.drawable.end);
        BitmapDescriptor wayPointBitmap = BitmapDescriptorFactory.fromResource(R.drawable.way);
        BitmapDescriptor normalRoute = BitmapDescriptorFactory.fromResource(R.drawable.lbs_custtexture_dott_gray);
        BitmapDescriptor smoothTraffic = BitmapDescriptorFactory.fromResource(R.drawable.custtexture_green);
        BitmapDescriptor unknownTraffic = BitmapDescriptorFactory.fromResource(R.drawable.custtexture_no);
        BitmapDescriptor slowTraffic = BitmapDescriptorFactory.fromResource(R.drawable.custtexture_slow);
        BitmapDescriptor jamTraffic = BitmapDescriptorFactory.fromResource(R.drawable.custtexture_bad);
        BitmapDescriptor veryJamTraffic = BitmapDescriptorFactory.fromResource(R.drawable.custtexture_grayred);

        // 路径id数组
        int[] routeIdArray = result.getRouteid();
        // 路径数组
        HashMap<Integer, AMapNaviPath> paths = navi.getNaviPaths();
        for (int i = 0; i < routeIdArray.length; i++) {
            AMapNaviPath path = paths.get(routeIdArray[i]);
            if (path != null) {
                // 根据给定的参数，构造一个导航路线图层类对象
                RouteOverLay routeOverLay = new RouteOverLay(map, path, this);
                // 是否开启交通线,步行&骑行不需要设置
                routeOverLay.setTrafficLine(true);
                // 设置红绿灯是否显示
                routeOverLay.setTrafficLightsVisible(false);
                // 设置起始点bitmap
                routeOverLay.setStartPointBitmap(null);
                // 设置结束点的bitmap
                routeOverLay.setEndPointBitmap(null);
                // 设置行车点切换为步行点的bitmap
                routeOverLay.setCartoFootBitmap(null);
                // 设置途经点的bitmap
                routeOverLay.setWayPointBitmap(null);
                // RouteOverlay的配置选项类
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
                routeOverLay.setRouteOverlayOptions(routeOverlayOptions);
                // 添加驾车/步行/骑行路线到地图上显示。
                routeOverLay.addToMap();
                routeOverlays.put(routeIdArray[i], routeOverLay);
            }
        }
        map.moveCamera(CameraUpdateFactory.changeTilt(0));
        changeRoute();
    }

    int routeIndex = 0;
    int zindex = 1;

    /**
     * 切换选中路径
     */
    private void changeRoute() {
        if (routeOverlays.size() == 0) return;

        // 显示导航相关按钮
        clNaviBtn.setVisibility(View.VISIBLE);

        // 先淡化所有路径
        for (int i = 0; i < routeOverlays.size(); i++) {
            int key = routeOverlays.keyAt(i);
            routeOverlays.get(key).setTransparency(0.3f);
        }
        // 计算选中路径index
        routeIndex++;
        if (routeIndex >= routeOverlays.size()) {
            routeIndex = 0;
        }
        // 获取选中路径key
        int routeKey = routeOverlays.keyAt(routeIndex);
        // 选中路径取消淡化
        routeOverlays.get(routeKey).setTransparency(1);
        // 选中路径覆盖在其他路径上
        routeOverlays.get(routeKey).setZindex(zindex++);
        // 将地图zoom到可以全览全路段的级别 padding-->l r t b
        routeOverlays.get(routeKey).zoomToSpan(100, 100, 300, 300, routeOverlays.get(routeKey).getAMapNaviPath());
        // 告诉AMapNavi 你最后选择的哪条路
        navi.selectRouteId(routeKey);
        // 选完路径后判断路线是否是限行路线，需要设置车牌
//        AMapRestrictionInfo info = navi.getNaviPath().getRestrictionInfo();
//        if (!TextUtils.isEmpty(info.getRestrictionTitle())) {
//            Toast.makeText(this, info.getRestrictionTitle(), Toast.LENGTH_SHORT).show();
//        }
    }

    /**
     * 开始导航
     */
    private void startNavi() {
        startActivity(new Intent(MainActivity.this, NaviActivity.class));
    }

    /**
     * 设置地图UI Setting
     *
     * @param map 地图工具
     */
    private void setMapUiSettings(AMap map) {
        // 获取UiSettings
        UiSettings uiSettings = map.getUiSettings();

        // 缩放按钮位置
        uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_BUTTOM);
        // 缩放按钮是否显示
        uiSettings.setZoomControlsEnabled(false);

        // 定位按钮是否显示(只能再右上角)
        uiSettings.setMyLocationButtonEnabled(false);

        // 指南针是否显示(设置了也没有用)
        uiSettings.setCompassEnabled(false);

        // 比例尺控件是否显示(设置了也没有用)
        uiSettings.setScaleControlsEnabled(false);

        // 高德地图标志位置(设置了也没有用)
        uiSettings.setLogoPosition(AMapOptions.ZOOM_POSITION_RIGHT_BUTTOM);

        // 缩放手势开是否开启
        uiSettings.setZoomGesturesEnabled(true);
        // 设置是否以地图中心点缩放
        uiSettings.setGestureScaleByMapCenter(true);

        // 滑动手势是否开启
        uiSettings.setScrollGesturesEnabled(true);

        // 旋转手势是否开启
        uiSettings.setRotateGesturesEnabled(true);

        // 倾斜手势是否开启
        uiSettings.setTiltGesturesEnabled(true);

        // 所有手势是否开启
        uiSettings.setAllGesturesEnabled(true);

    }

    /**
     * 设置定位（当前位置）图层
     *
     * @param map 地图工具
     */
    private void setMyLocationOverlay(AMap map) {
        // 定位（当前位置）的绘制样式
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 设置定位（当前位置）的icon图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.car_arrow_icon));
        // 设置我的位置展示模式
        // LOCATION_TYPE_SHOW --> 只定位一次
        // LOCATION_TYPE_LOCATE --> 只定位一次、且将视角移动到地图中心点
        // LOCATION_TYPE_FOLLOW --> 连续定位、且将视角移动到地图中心点，定位点跟随设备移动
        // LOCATION_TYPE_MAP_ROTATE --> 连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动
        // LOCATION_TYPE_LOCATION_ROTATE --> 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动
        // LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER --> 连续定位、但不会移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动
        // LOCATION_TYPE_FOLLOW_NO_CENTER --> 连续定位、但不会移动到地图中心点，并且会跟随设备移动。
        // LOCATION_TYPE_MAP_ROTATE_NO_CENTER --> 连续定位、但不会移动到地图中心点，地图依照设备方向旋转，并且会跟随设备移动。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        // 设置发起定位请求的时间间隔，单位：毫秒，默认值：1000毫秒，如果传小于1000的任何值将执行单次定位。
        myLocationStyle.interval(1000);
        // 设置是否显示定位小蓝点
        myLocationStyle.showMyLocation(true);
        // 设置圆形区域（以定位位置为圆心，定位半径的圆形区域）的填充颜色
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);
        // 设置圆形区域（以定位位置为圆心，定位半径的圆形区域）的边框颜色
        myLocationStyle.strokeColor(Color.TRANSPARENT);
        // 设置圆形区域（以定位位置为圆心，定位半径的圆形区域）的边框宽度
        myLocationStyle.strokeWidth(0);
        // 设置定位（当前位置）的绘制样式
        map.setMyLocationStyle(myLocationStyle);
        // 设置是否打开定位图层
        map.setMyLocationEnabled(true);
        // 设置用户定位信息监听接口，仅第一次定位移动到地图中心点
        map.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            boolean isNeedMoveToCenter = true;

            @Override
            public void onMyLocationChange(Location location) {
                if (isNeedMoveToCenter) {
                    map.moveCamera(CameraUpdateFactory.zoomTo(15));
                    map.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                    isNeedMoveToCenter = false;
                }
            }
        });
    }

    /**
     * 设置地图View
     */
    private void setMapView() {
        // 获取地图工具
        map = textureMapView.getMap();
        // 设置地图UI Setting
        setMapUiSettings(map);
        // 设置我的位置定位图层
        setMyLocationOverlay(map);
    }

    /**
     * 检查权限
     *
     * @param permissions 被检查的权限
     * @return 是否获取了全部被检查的权限
     */
    private boolean checkPermissions(String... permissions) {
        List<String> needRequestPermissionList = findDeniedPermissions(permissions);
        if (null != needRequestPermissionList && needRequestPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(this, needRequestPermissionList.toArray(new String[needRequestPermissionList.size()]), REQUEST_CODE_PERMISSON);
            return false;
        }
        return true;
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions 被检查的权限
     * @return 需要申请的权限
     */
    private List<String> findDeniedPermissions(String... permissions) {
        List<String> needRequestPermissionList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                needRequestPermissionList.add(perm);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 检测是否所有的权限都已经授权
     *
     * @param grantResults 申请权限结果
     * @return 是否所有的权限都已经授权
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
