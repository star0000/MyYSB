package com.mds.myysb.utils;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

public class YSBAcitivityList {

    public static List<Activity> activityList = new LinkedList<Activity>();

    private static YSBAcitivityList instance;

    public static YSBAcitivityList getInstance() {
        if (instance == null) {
            return new YSBAcitivityList();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public void delActivity(Activity activity) {
        activity.finish();
        // activityList.remove(activity);
    }

    public void exitAccount() {
        int size = activityList.size();
        for (int i = 0; i < size; i++) {
            if (activityList.get(i) != null) {
                activityList.get(i).finish();
            }
        }
        activityList.clear();
    }

    public void exitAccountExceptItem(Activity item) {
        int size = activityList.size();
        for (int i = 0; i < size; i++) {
            Activity activity = activityList.get(i);
            if (activity != null) {
                if (!activity.equals(item)) {
                    activityList.get(i).finish();
                }
            }
        }
        activityList.clear();
        activityList.add(item);
    }

    public void killall() {

        int size = activityList.size();
        for (int i = 0; i < size; i++) {
            if (activityList.get(i) != null) {
                activityList.get(i).finish();
            }
        }
        activityList.clear();
        // android.os.Process.killProcess(android.os.Process.myPid());
        // System.exit(0);
    }

}
