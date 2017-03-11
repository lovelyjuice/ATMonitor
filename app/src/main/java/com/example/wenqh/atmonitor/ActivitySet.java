package com.example.wenqh.atmonitor;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by wenqh on 2017/3/10.
 */

public class ActivitySet
{
    public static ArrayList<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity)
    {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity)
    {
        activities.remove(activity);
    }

    public static void destroyAll()
    {
        for (Activity activity : activities)
        {
            if (!activity.isFinishing()) activity.finish();
        }
        activities.clear();
    }
}
