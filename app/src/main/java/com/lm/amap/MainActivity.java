package com.lm.amap;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.TextureSupportMapFragment;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.navi.AMapNavi;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * 搜索输入View
     */
    AutoCompleteTextView etSearch;

    /**
     * 地图工具
     */
    private AMap map;
    /**
     * 导航工具
     */
    private AMapNavi navi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 设置搜索输入框
        setSearchView();
        // 设置地图View
        setMapView();
        // 获取导航工具
        navi = AMapNavi.getInstance(getApplicationContext());

    }

    /**
     * 设置搜索输入框
     */
    private void setSearchView() {
        etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString().trim();
                InputtipsQuery inputtipsQuery = new InputtipsQuery(newText, null);
                Inputtips inputTips = new Inputtips(getApplicationContext(), inputtipsQuery);
                inputTips.setInputtipsListener((list, resultCode) -> {
                    if (resultCode == 1000) {

                    } else {
                        Toast.makeText(MainActivity.this, "搜索结果出错 resultCode=" + resultCode, Toast.LENGTH_SHORT).show();
                    }
                });
                inputTips.requestInputtipsAsyn();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 设置地图View
     */
    private void setMapView() {
        try {
            // 初始化高德地图Android API,为使用它包含的类做准备。
            // 如果正在使用MapFragment或MapView，并且调用getMap()获得非空的地图，可以不需要调用这个方法。
            MapsInitializer.initialize(getApplicationContext());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        // 一个地图Fragment基于TextureView
        TextureSupportMapFragment textureSupportMapFragment = TextureSupportMapFragment.newInstance();
        // 显示地图Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent, textureSupportMapFragment).commit();

        // 获取地图工具
        map = textureSupportMapFragment.getMap();

        // 设置我的位置定位图层
        setMyLocationOverlay(map);
    }

    /**
     * 设置定位（当前位置）图层
     *
     * @param map 地图工具
     */
    private void setMyLocationOverlay(AMap map) {
        // 定位（当前位置）的绘制样式
        MyLocationStyle myLocationStyle = new MyLocationStyle();
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
                    map.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                    isNeedMoveToCenter = false;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navi.destroy();
    }
}
