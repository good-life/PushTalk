package org.pushtalk.android.utils;

import android.util.Log;

public class Logger {
	private static final String COMMON_TAG = "Push_Talk";
	
	public static boolean DEBUG_STATE = true;

    public static void v(String tag, String msg) {
        if (DEBUG_STATE) {
        	Log.v(COMMON_TAG, "[" + tag + "] " + msg);
        }
    }
    
    public static void d(String tag, String msg) {
        if (DEBUG_STATE) {
        	Log.d(COMMON_TAG, "[" + tag + "] " + msg);
        }
    }
    
    public static void i(String tag, String msg) {
        if (DEBUG_STATE) {
        	Log.i(COMMON_TAG, "[" + tag + "] " + msg);
        }
    }
    
    public static void w(String tag, String msg) {
        Log.w(COMMON_TAG, "[" + tag + "] " + msg);
    }
    
    public static void e(String tag, String msg) {
        Log.e(COMMON_TAG, "[" + tag + "] " + msg);
    }
    
    public static void v(String tag, String msg, Throwable tr) {
        if (DEBUG_STATE) {
        	Log.v(COMMON_TAG, "[" + tag + "] " + msg, tr);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (DEBUG_STATE) {
        	Log.d(COMMON_TAG, "[" + tag + "] " + msg, tr);
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (DEBUG_STATE) {
        	Log.i(COMMON_TAG, "[" + tag + "] " + msg, tr);
        }
    }

    public static void w(String tag, String msg, Throwable tr) {
        Log.w(COMMON_TAG, "[" + tag + "] " + msg, tr);
    }

    public static void e(String tag, String msg, Throwable tr) {
        Log.e(COMMON_TAG, "[" + tag + "] " + msg, tr);
    }
    
        
}
