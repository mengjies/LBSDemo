package com.mj.lbsdemo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.mj.lbsdemo.R;
import com.mj.lbsdemo.adapter.ItemAdapter;
import com.mj.lbsdemo.entity.Item;
import com.mj.lbsdemo.listener.MyLocationListener;
import com.mj.lbsdemo.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BasicActivity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = "MainActivity";

    private long firstClick = 0;
    private List<Item> itemList = new ArrayList<>();

    //百度地图
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    public static void actionStart(Activity act) {
        Intent intent = new Intent(act, MainActivity.class);
        act.startActivity(intent);
        act.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);
        //配置LocationClientOption
        initLocation();
        //开始定位
        startLocate();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("LBSDemo");
        }

        itemList.clear();
        itemList.add(new Item(getString(R.string.text_location), R.mipmap.ic_location));
        itemList.add(new Item(getString(R.string.text_map), R.mipmap.ic_map));

        ItemAdapter adapter = new ItemAdapter(itemList);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 10000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    @AfterPermissionGranted(1)
    private void startLocate() {
        String[] perms = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (EasyPermissions.hasPermissions(this, perms)) {
            //有权限 继续业务 开启定位
            mLocationClient.start();
            ToastUtils.showTestToast(this, "定位开启");
        } else {
            //无权限 申请权限
            EasyPermissions.requestPermissions(this, getString(R.string.permission_alert), 1, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //some permissions granted
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // some permission denied
        // 判断是否有权限被永久禁止，如果有提示到应用管理界面设置
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this, getString(R.string.permission_ask_again))
                    .setTitle(getString(R.string.title_settings_dialog))
                    .setPositiveButton(getString(R.string.text_setting))
                    .setNegativeButton(getString(R.string.text_cancel), null)
                    .setRequestCode(2)
                    .build()
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 2:
                // Do something
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    //双击退出应用
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondClick = System.currentTimeMillis();
            if (secondClick - firstClick > 1000) {
                ToastUtils.showToast(this, "再次点击退出");
                firstClick = secondClick;
                return true;
            } else {
                //退出应用
                finish();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

}
