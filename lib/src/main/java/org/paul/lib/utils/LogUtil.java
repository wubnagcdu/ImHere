package org.paul.lib.utils;

import android.util.Log;

public class LogUtil {

    private static final String TAG="lib";

    public static void logD(String msg){
        Log.d(TAG,msg);
    }
    public static void logI(String msg){
        Log.i(TAG,msg);
    }
    public static void logV(String msg){
        Log.v(TAG,msg);
    }
    public static void logE(Exception e){
        Log.e(TAG,e.getMessage());
    }
    public static void logE(String e){
        Log.e(TAG,e);
    }

}
