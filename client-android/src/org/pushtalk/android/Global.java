package org.pushtalk.android;

import org.pushtalk.android.web.WebHelper;

import android.content.Context;

public class Global {
    
    
    public static String getLanguage(Context context){
        return context.getResources().getConfiguration().locale.toString();
    }

    public static String getPathUrl(Context context, String path, String host) {
        if (null == path) path = Constants.PATH_MAIN;
        String url = host + path;
        url = WebHelper.buildDefaultWebpageUrl(context.getApplicationContext(), url);
        url = WebHelper.attachParamsToUrl(url, new String[] { Constants.KEY_HOST, Config.HOST });
        return url;
    }
    
    public static boolean isAccessMainPage(String url) {
        if (null == url) return false;
        return url.startsWith(Config.HOST + Constants.PATH_MAIN);
    }
}
