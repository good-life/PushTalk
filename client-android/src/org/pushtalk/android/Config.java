package org.pushtalk.android;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Config {

    public static boolean IS_TEST_MODE = true;
    
    public static String VERSION = "0.2.3";
    
    public static String WEB_JS_MODULE = "pushtalk";
    
    public static final boolean NOTIFICATION_NEED_SOUND = true;
    public static final boolean NOTIFICATION_NEED_VIBRATE = true;
    
    public static String SERVER;
    public static String udid;
    public static String myName;
    public static Set<String> myChannels;
    
    public static boolean isBackground = true;
    
    public static Map<String, String> serverList = new LinkedHashMap<String, String>();
    static {
        serverList.put("推聊官方 (北京)", "http://111.13.48.109:10010");
        serverList.put("Local Dev", "http://192.168.3.195:10010");
    };

}

