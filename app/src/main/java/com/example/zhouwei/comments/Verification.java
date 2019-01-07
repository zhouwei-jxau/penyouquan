package com.example.zhouwei.comments;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by zhouwei on 2018/12/2.
 */

public class Verification {

    public static boolean isEmpty(Activity activity, String str, String tip)
    {
        if(str==null||str.equals(""))
        {
            Toast.makeText(activity, tip, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public static boolean isMinLength(Activity activity,String str, String tip,int len)
    {
        if(str.length()<len)
        {
            Toast.makeText(activity, tip, Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    public static boolean isMaxLength(Activity activity,String str, String tip,int len) {
        if(str.length()>len)
        {
            Toast.makeText(activity, tip, Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }
}
