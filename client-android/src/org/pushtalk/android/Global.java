package org.pushtalk.android;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

public class Global {
    private static Map<String, String> cachedMessages = new HashMap<String, String>();
    
    public static void newMessageWhenBackground(String friend, String channel, String message) {
        cachedMessages.put(friend, message);
    }
    
    
    public static String getLanguage(Context context){
        return context.getResources().getConfiguration().locale.toString();
    }

}
