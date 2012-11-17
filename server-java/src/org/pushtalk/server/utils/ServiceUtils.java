package org.pushtalk.server.utils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pushtalk.server.Config;

public class ServiceUtils {

    private static final String SERVICE_ID_PREFIX = "_";
    public static final String CHANNEL_PREFIX = "#";
    public static final String USER_PREFIX = "@";
    

    public static String postfixAliasAndTag(String aliasOrTag) {
        return aliasOrTag + SERVICE_ID_PREFIX + Config.SERVER_ID;
    }
    
    
    public static String getChattingChannel(String channelName) {
        return CHANNEL_PREFIX + channelName;
    }
    
    public static String getChattingChannel(String myName, String friendName) {
        String[] name = new String[] { myName, friendName };
        Arrays.sort(name);
        return USER_PREFIX + name[0] + USER_PREFIX + name[1];
    }
    
    public static String getChattingFriend(String friendName) {
        if (null == friendName) return null;
        return USER_PREFIX + friendName;
    }
    
    
    public static boolean isValidAliasOrTag(String aliasOrTag) {
        if (StringUtils.isTrimedEmpty(aliasOrTag)) return false;
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_]{0,}$");
        Matcher m = p.matcher(aliasOrTag);
        return m.matches();
    }
    
}

