package org.pushtalk.android;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import org.pushtalk.android.utils.Logger;
import org.pushtalk.android.utils.MyPreferenceManager;
import org.pushtalk.android.utils.StringUtils;
import org.pushtalk.android.web.WebHelper;

import android.content.Context;

public class Global {
    private static final String TAG = "Global";
    
    public static void init(Context context) {
        MyPreferenceManager.init(context);
        Config.SERVER = getCurrentServer();
        Logger.i(TAG, "Current pushtalk server: " + Config.SERVER);
    }
    
    
    public static String getCurrentServer() {
        String server = MyPreferenceManager.getString(Constants.PREF_CURRENT_SERVER, null);
        if (null != server) return server;
        
        if (Config.serverList.size() == 0) {
            Logger.e(TAG, "No server configured!");
            return null;
        }
        
        Set<String> serverNames = Config.serverList.keySet();
        String theFirst = null;
        for (String name : serverNames) {
            theFirst = name;
            break;
        }
        
        server = Config.serverList.get(theFirst);
        MyPreferenceManager.commitString(Constants.PREF_CURRENT_SERVER, server);
        return server;
    }
    
    
    public static String getLanguage(Context context){
        return context.getResources().getConfiguration().locale.toString();
    }

    public static String getPathUrl(Context context, String path, String host) {
        if (null == path) path = Constants.PATH_MAIN;
        String url = host + path;
        url = WebHelper.buildDefaultWebpageUrl(context.getApplicationContext(), url);
        url = WebHelper.attachParamsToUrl(url, new String[] { Constants.KEY_HOST, Config.SERVER });
        return url;
    }
    
    public static boolean isAccessMainPage(String url) {
        if (null == url) return false;
        return url.startsWith(Config.SERVER + Constants.PATH_MAIN);
    }
    
    public static boolean isAccessChattingPage(String url) {
        if (null == url) return false;
        return url.startsWith(Config.SERVER + Constants.PATH_CHATTING);
    }
    
    public static String getCurrentChatting(String url) {
        if (StringUtils.isEmpty(url)) return url;
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        String query = uri.getQuery();
        if (null == query) return null;
        
        String[] params = query.split("&");
        if (params.length > 0) {
            for (String param : params) {
                if (param.contains(Constants.KEY_CHATTING)) {
                    String[] s = param.split("=");
                    if (s.length > 1) {
                        return s[1];
                    }
                }
            }
        } else {
            Logger.d(TAG, "No query param.");
        }
        
        return null;
    }
    
}


