package com.mds.myysb.ui.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.mds.myysb.R;
import com.mds.myysb.base.BaseActivity;
import com.mds.myysb.ui.fragment.MyFragment;
import com.mds.myysb.ui.fragment.WeightManagementFragment;
import com.mds.myysb.ui.fragment.FetalHeartMonitoringFragment;
import com.mds.myysb.ui.fragment.PregnancycalendarFragment;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Setting;

import java.util.List;


public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {


    private BottomNavigationBar bottomNavigationBar;
    int lastSelectedPosition = 0;
    private PregnancycalendarFragment mPregnancycalendarFragment;
    private FetalHeartMonitoringFragment mFetalHeartMonitoringFragment;
    private WeightManagementFragment mWeightManagementFragment;
    private MyFragment mMyFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
        .addItem(new BottomNavigationItem(R.drawable.maintab_navigation_nor, R.string.yunshengrili).setActiveColor(R.color.orange))
        .addItem(new BottomNavigationItem(R.drawable.maintab_monitoring_nor, R.string.taixinjiance).setActiveColor(R.color.orange))
        .addItem(new BottomNavigationItem(R.drawable.maintab_happy_stage_nor, R.string.weight).setActiveColor(R.color.orange))
        .addItem(new BottomNavigationItem(R.drawable.maintab_my_nor, R.string.mine) .setActiveColor(R.color.orange))
        .setFirstSelectedPosition(lastSelectedPosition )
        .initialise();
        bottomNavigationBar.setTabSelectedListener(this);
        setDefaultFragment();

        requestPermission(
        Manifest.permission.CAMERA,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onTabSelected(int position) {

        FragmentManager fm = this.getFragmentManager();
        //开启事务
        FragmentTransaction transaction = fm.beginTransaction();
        switch (position) {
            case 0:
                if (mPregnancycalendarFragment == null) {
                    mPregnancycalendarFragment = PregnancycalendarFragment.newInstance();
                }
                transaction.replace(R.id.tb, mPregnancycalendarFragment);
                break;
            case 1:
                if (mFetalHeartMonitoringFragment == null) {
                    mFetalHeartMonitoringFragment = FetalHeartMonitoringFragment.newInstance();
                }
                transaction.replace(R.id.tb, mFetalHeartMonitoringFragment);
                break;
            case 2:
                if (mWeightManagementFragment == null) {
                    mWeightManagementFragment = WeightManagementFragment.newInstance();
                }
                transaction.replace(R.id.tb, mWeightManagementFragment);
                break;
            case 3:
                if (mMyFragment == null) {
                    mMyFragment = MyFragment.newInstance();
                }
                transaction.replace(R.id.tb, mMyFragment);
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        mPregnancycalendarFragment = PregnancycalendarFragment.newInstance();
        transaction.replace(R.id.tb, mPregnancycalendarFragment);
        transaction.commit();
    }


    private void requestPermission(String... permissions) {
        AndPermission.with(this)
        .runtime()
        .permission(permissions)
        .onGranted(new Action<List<String>>() {
            @Override
            public void onAction(List<String> permissions) {
                // Toast.makeText(MainActivity.this,"请求权限成功",Toast.LENGTH_SHORT).show();
            }
        })
        .onDenied(new Action<List<String>>() {
            @Override
            public void onAction(@NonNull List<String> permissions) {
                if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                    showSettingDialog(MainActivity.this, permissions);
                }
            }
        })
        .start();
    }
    public void showSettingDialog(Context context, final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(context, permissions);
        String message = context.getString(R.string.message_permission_always_failed, TextUtils.join("\n", permissionNames));
        new AlertDialog.Builder(context)
        .setCancelable(false)
        .setTitle(R.string.message_permission_qq)
        .setMessage(message)
        .setPositiveButton(R.string.message_permission_system, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setPermission();
            }
        })
        .setNegativeButton(R.string.message_permission_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        })
        .show();
    }
    private void setPermission() {
        AndPermission.with(this)
        .runtime()
        .setting()
        .onComeback(new Setting.Action() {
            @Override
            public void onAction() {

            }
        })
        .start();
    }

}
