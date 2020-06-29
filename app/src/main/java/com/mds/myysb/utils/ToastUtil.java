package com.mds.myysb.utils;

import android.content.Context;
import android.widget.Toast;

import com.mds.myysb.base.MyApplication;

public class ToastUtil {

    private static String oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    /**
     * 弹一个Toast
     *
     * @param msg
     *            要展示的信息
     * @param lengthType
     *            展示的时间长度（0--短，1--长）
     */
    public static void showToast(String msg, int lengthType) {
        if (0 == lengthType)
            Toast.makeText(MyApplication.getInstance(), msg,Toast.LENGTH_SHORT).show();
        else if (1 == lengthType)
            Toast.makeText(MyApplication.getInstance(), msg,Toast.LENGTH_LONG).show();
        else
            Toast.makeText(MyApplication.getInstance(), msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹一个Toast
     *
     * @param msgStringid
     *            要展示的信息
     * @param lengthType
     *            展示的时间长度（0--短，1--长）
     */
    public static void showToast(int msgStringid, int lengthType) {
        if (0 == lengthType)
            Toast.makeText(MyApplication.getInstance(), msgStringid,Toast.LENGTH_SHORT).show();
        else if (1 == lengthType)
            Toast.makeText(MyApplication.getInstance(), msgStringid,Toast.LENGTH_LONG).show();
        else
            Toast.makeText(MyApplication.getInstance(), msgStringid,Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String s) {
        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

}
