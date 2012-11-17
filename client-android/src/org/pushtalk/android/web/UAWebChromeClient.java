package org.pushtalk.android.web;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class UAWebChromeClient extends WebChromeClient {
    final private ProgressBar mProgressBar;
    
    public UAWebChromeClient(ProgressBar pb){
        this.mProgressBar = pb;
    }
    
    
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        
        if (null != mProgressBar) {
	        if(mProgressBar.getVisibility() != View.VISIBLE){
	            mProgressBar.setVisibility(View.VISIBLE);
	        }
	        mProgressBar.setProgress(newProgress);
	        if(mProgressBar.getProgress() == 100){
	            mHandler.sendEmptyMessageDelayed(0, 500);
	        }
        }
    }
    
    
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            if (mProgressBar != null) {
            	mProgressBar.setProgress(0);
            	mProgressBar.setVisibility(View.GONE);
            }
        }
    };

}
