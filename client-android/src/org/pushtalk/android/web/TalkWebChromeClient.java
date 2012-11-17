package org.pushtalk.android.web;

import java.lang.ref.WeakReference;

import org.pushtalk.android.Config;
import org.pushtalk.android.utils.Logger;

import android.app.Activity;
import android.content.Context;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

public class TalkWebChromeClient extends WebChromeClient {    
    private static final String TAG = "TalkWebChromeClient";
    private final WeakReference<Activity> mActivity;
    
    public TalkWebChromeClient(Context context) {
        this.mActivity = new WeakReference<Activity> ((Activity) context);
    }
    
    @Override
    public void onCloseWindow(WebView window) {
        if (null != mActivity && null != mActivity.get()) {
            mActivity.get().finish();
        } else {
            Logger.w(TAG, "Unexpected: no action for mWebView closed.");
        }
    }
    
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
    	if (Config.IS_TEST_MODE) {
    		return false;
    	} else {
    		return true;
    	}
    }
    
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
    	if (Config.IS_TEST_MODE) {
    		return false;
    	} else {
    		return true;
    	}
    }
    
    @Override
    public boolean onJsTimeout() {
    	if (Config.IS_TEST_MODE) {
    		return false;
    	} else {
    		return true;
    	}
    }
    
//    @Override
//    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
//        Log.d(TAG, consoleMessage.message());
//        return false;
//    }

    @Override
    public void onReachedMaxAppCacheSize(long spaceNeeded,
            long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
        quotaUpdater.updateQuota(spaceNeeded * 2);
    }
    
}

