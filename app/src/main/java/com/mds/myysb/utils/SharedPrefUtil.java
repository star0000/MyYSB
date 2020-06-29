package com.mds.myysb.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefUtil {

    /**
     * 存储 个人是否注册状态
     */
    public static void putUserRegisterStatus(Context context, Boolean state){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ysb_basic_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("user_register_status", state);
        editor.commit();
    }
    /**
     * 提取 个人是否注册状态
     */
    public static Boolean getUserRegisterStatus(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ysb_basic_info", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("user_register_status", false);
    }

    /**
     * 存储 个人id信息
     */
    public static void putSessionId(Context context, String id){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ysb_basic_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("session_Id", id);
        editor.commit();
    }
    /**
     * 提取 个人id信息
     */
    public static String getSessionId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ysb_basic_info", Context.MODE_PRIVATE);
        return sharedPreferences.getString("session_Id", null);
    }

    /**
     * 存储 个人所在地信息
     */
    public static void putArea(Context context, String area){
        if(context==null)
            return;
        SharedPreferences sharedPreferences = context.getSharedPreferences("ysb_basic_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_area", area);
        editor.commit();
    }
    /**
     * 提取 个人所在地信息
     */
    public static String getArea(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ysb_basic_info", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_area", null);
    }

    /**
     * 存储 个人胎心探头号信息
     */
    public static void putUserProbeNo(Context context, String orderNo){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ysb_basic_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_order_no", orderNo);
        editor.commit();
    }
    /**
     * 提取  个人胎心探头号信息
     */
    public static String getUserProbeNo(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ysb_basic_info", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_order_no", null);
    }

    /**
     * 存储是否是激活用户的信息
     */
    public static void putActivateState(Context context, boolean isActivate){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ysb_basic_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("user_is_activate", isActivate);
        editor.commit();
    }
    /**
     * 提取是否是激活用户的信息
     */
    public static boolean getActivateState(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ysb_basic_info", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("user_is_activate", false);
    }

    /**
     * 存储 个人血压设备mac
     */
    public static void putUserXueyaNo(Context context, String orderNo){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ysb_basic_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_xueya_no", orderNo);
        editor.commit();
    }
    /**
     * 提取  个人血压设备mac
     */
    public static String getUserXueyaNo(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ysb_basic_info", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_xueya_no", null);
    }

    /**
     * 存储 个人体重设备mac
     */
    public static void putUserTizhongNo(Context context, String orderNo){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ysb_basic_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_tizhong_no", orderNo);
        editor.commit();
    }
    /**
     * 提取  个人体重设备mac
     */
    public static String getUserTizhongNo(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ysb_basic_info", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_tizhong_no", null);
    }

    /**
     * 存储 个人尿检设备mac
     */
    public static void putUserNiaoJianNo(Context context, String orderNo){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ysb_basic_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_niaojian_no", orderNo);
        editor.commit();
    }
    /**
     * 提取  个人尿检设备mac
     */
    public static String getUserNiaojianNo(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ysb_basic_info", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_niaojian_no", null);
    }

    /**
     * 存储 个人预产期
     */
    public static void putUserEDC(Context context, String EDC){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ysb_basic_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_EDC", EDC);
        editor.commit();
    }
    /**
     * 提取  个人预产期
     */
    public static String getUserEDC(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ysb_basic_info", Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_EDC", null);
    }

    public static void  putString(Context c,String key,String msg){
        SharedPreferences sp = c.getSharedPreferences("aa.xml",Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key,msg);
        e.commit();
    }
    public static  String getString(Context c,String key,String d){
        SharedPreferences sp = c.getSharedPreferences("aa.xml",Context.MODE_PRIVATE);
        return sp.getString(key,d);
    }

}
