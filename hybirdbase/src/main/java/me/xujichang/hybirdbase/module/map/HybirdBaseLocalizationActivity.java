package me.xujichang.hybirdbase.module.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.xujichang.utils.tool.LocationTool;
import com.xujichang.utils.tool.LogTool;

import me.xujichang.hybirdbase.R;
import me.xujichang.hybirdbase.base.HybirdBaseActivity;
import me.xujichang.hybirdbase.base.HybirdConst;
import me.xujichang.hybirdbase.bean.Location;

/**
 * 选取位置
 * Created by xjc on 2017/6/9.
 */

public abstract class HybirdBaseLocalizationActivity extends HybirdBaseActivity {
    /**
     * 经纬度提示
     */
    private TextView tvLocShow;
    /**
     * 百度地图 View
     */
    private MapView mvBaiduMapView;
    /**
     * 百度地图
     */
    private BaiduMap mBaiduMap;
    /**
     * 携带位置信息的自定义类
     */
    private Location location;

    public MapView getMvBaiduMapView() {
        return mvBaiduMapView;
    }

    public void setMvBaiduMapView(MapView mvBaiduMapView) {
        this.mvBaiduMapView = mvBaiduMapView;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_localization);
        location = getIntent().getParcelableExtra(HybirdConst.FLAG.LOCATION);
        initView();
    }

    private void initView() {
        initActionBar();
        tvLocShow = (TextView) findViewById(R.id.tv_loc_name);
        mvBaiduMapView = (MapView) findViewById(R.id.mv_baidu_map_view);
        FloatingActionButton fabLocationDone = (FloatingActionButton) findViewById(R.id.fab_location_done);
        initBaiduMapView();

        fabLocationDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng target = mBaiduMap.getMapStatus().target;
                onLocationDown(target);
            }
        });
    }

    protected void onLocationDown(LatLng target) {
        Location location = new Location();
        location.init(LocationTool.convertBaidu2Gps(new double[]{target.latitude, target.longitude}));
        Log.d("11", "-----------------" + target);
        Intent intent = new Intent();
        intent.putExtra(HybirdConst.FLAG.LOCATION, location);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initBaiduMapView() {
        LatLng GpsLatLng = getDefaultGpsLatLng();
        LogTool.d(GpsLatLng.toString());
        LatLng BaiduLatlng = new CoordinateConverter().from(CoordinateConverter.CoordType.GPS).coord(GpsLatLng).convert();
        mBaiduMap = mvBaiduMapView.getMap();
        BaiduMap.OnMapStatusChangeListener mapStatusChangeListener = MapStatusChangeListener();
        //地图状态改变相关监听
        mBaiduMap.setOnMapStatusChangeListener(mapStatusChangeListener);
        //关闭定位图层
        mBaiduMap.setMyLocationEnabled(true);

        MapStatus mMapStatus = new MapStatus.Builder().target(BaiduLatlng).zoom(18).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
    }

    private void initActionBar() {
        showBackArrow();
        setActionBarTitle("获取位置信息");
    }

    @Override
    protected void onLeftAreaClick() {
        locationWarning();
    }

    private void locationWarning() {
        showWarningDialog("位置已获取，但不会生效，确定返回?", new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                materialDialog.dismiss();
                if (dialogAction == DialogAction.POSITIVE) {
                    finish();
                }
            }
        });
    }

    /**
     * 按顺序获取初始化的地址信息
     * 首先是成功定位的
     * 手机定位
     * 上次定位的位置
     * 默认的位置
     *
     * @return
     */
    protected abstract LatLng getDefaultGpsLatLng();

    private BaiduMap.OnMapStatusChangeListener MapStatusChangeListener() {
        BaiduMap.OnMapStatusChangeListener listener = new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                double[] gps = LocationTool.convertBaidu2Gps(new double[]{mapStatus.target.latitude, mapStatus.target.longitude});
                String str = new StringBuilder("lat:").append(gps[1]).append(" lng:").append(gps[0]).toString();
                updateLocation(str);
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
            }
        };
        return listener;
    }

    private void updateLocation(String str) {
        tvLocShow.setText(str);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mvBaiduMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mvBaiduMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mvBaiduMapView.onPause();
    }

    @Override
    public void onBackPressed() {
        onLeftAreaClick();
    }

}
