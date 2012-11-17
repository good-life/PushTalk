package org.pushtalk.android.activity;

import android.webkit.WebView;
import cn.jpush.android.api.InstrumentedActivity;

public abstract class WebBaseActivity extends InstrumentedActivity {
    
    protected WebView mWebView;
    
	public abstract void setTitle(String pageTitle);
	
	public abstract void setBackButtonName(String backButtonName);
	
	
    @Override
    protected void onDestroy() {
        if(null != mWebView){
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

}
