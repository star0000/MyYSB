package com.mds.myysb.base;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.mds.myysb.R;
import com.mds.myysb.ui.activity.SplashActivity;
import com.mds.myysb.utils.StatusBarUtil;
import com.mds.myysb.utils.ToastUtil;
import com.mds.myysb.utils.YSBAcitivityList;

public class BaseActivity extends AppCompatActivity {

    private static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        //设置状态栏文字为黑色
        //StatusBarUtil.setStatusBarDarkTheme(this,true);
        //设置状态栏文字为白色
        //StatusBarUtil.setStatusBarDarkTheme(this,false);
        //设置状态栏透明
       // StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        //if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0xFFFFAACC);
        //}
        YSBAcitivityList.getInstance().addActivity(this);

//        boolean crash_status = sharedPreferences.getBoolean("gsk_crash_status", false);
//
//        if (YSBAcitivityList.activityList.get(0) instanceof SplashActivity) {
//        } else {
//            if (crash_status) {
//                finish();
//                return;
//            }
//        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        YSBAcitivityList.getInstance().removeActivity(this);
    }


    /**
     * 为子类提供一个权限检查方法
     * String... permission表示不定参数，也就是调用这个方法的时候这里可以传入多个String对象(JDK5新特性)
     * @return
     */
    //String... permission表示不定参数，也就是调用这个方法的时候这里可以传入多个String对象(JDK5新特性)
    public boolean hasPermission(String... permissions ){
        for(String permission: permissions ){
            if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                return true;
            }
        }
        return false;
    }


    long exitTime = 0;
    // 连续两次点击返回键退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 3000) {
                ToastUtil.showToast(this,"再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
//					System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
