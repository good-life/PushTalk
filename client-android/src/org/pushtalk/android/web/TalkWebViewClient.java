package org.pushtalk.android.web;

import java.lang.ref.WeakReference;

import org.pushtalk.android.Global;
import org.pushtalk.android.R;
import org.pushtalk.android.activity.WebBaseActivity;
import org.pushtalk.android.utils.DialogUtil;
import org.pushtalk.android.utils.Logger;
import org.pushtalk.android.utils.StringUtils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TalkWebViewClient extends WebViewClient {
	private static final String TAG = "TalkWebViewClient";
	private final WeakReference<WebBaseActivity> mActivity;
	
	public TalkWebViewClient(Context context) {
		this.mActivity = new WeakReference<WebBaseActivity> ((WebBaseActivity) context);
	}
	
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            url = WebHelper.buildDefaultWebpageUrl(mActivity.get(), url);
            view.loadUrl(url);
            return false;
            
        } else {
            Intent intent = null;
            if(url.startsWith("tel")){
                intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            }else if(url.startsWith("smsto")){
                intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
            }else if(url.startsWith("mailto")){
                intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
            }
            if(null != intent){
                view.getContext().startActivity(intent);
            }
            return true;
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Logger.d(TAG, "action:onPageStarted - " + url);
    	super.onPageStarted(view, url, favicon);
    }
    
    @Override
    public void onPageFinished(WebView view, String url) {
        Logger.d(TAG, "onPageFinished - url:" + url);
    	
    	String title = view.getTitle();
        if (mActivity.get() != null) {
            if (!StringUtils.isEmpty(title)) {
        		mActivity.get().setTitle(title);
        	}
            if (view.canGoBack() && !Global.isAccessMainPage(url)) {
                mActivity.get().setBackButtonName(mActivity.get().getString(R.string.btn_back));
            } else {
                mActivity.get().setBackButtonName(mActivity.get().getString(R.string.btn_close));
            }
        }
        
        mActivity.get().onPageFinished(url);
        
        super.onPageFinished(view, url);
    }
    
    @Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
    	switch (errorCode) {
	    	case WebViewClient.ERROR_BAD_URL:	//1
	    		Logger.d(TAG, "Malformed URL - " + description);
	    		break;
	    		
        	case WebViewClient.ERROR_CONNECT:	//2
        		Logger.d(TAG, "Failed to connect to the server - " + description);
        		showErrorDialog(description);
        		break;
        		
        	case WebViewClient.ERROR_HOST_LOOKUP:	//6
        		Logger.d(TAG, "Server or proxy hostname lookup failed - " + description);
        		break;
        		
        	case WebViewClient.ERROR_IO:			//7
        		Logger.d(TAG, "Cannot connect server - " + description);
        		showErrorDialog(description);
        		break;
        		
        	case WebViewClient.ERROR_REDIRECT_LOOP:	//9
        		Logger.w(TAG, "Too many redirects - " + description);
        		break;
        	case WebViewClient.ERROR_TIMEOUT:	//10
        		Logger.d(TAG, "Connection timed out - " + description);
        		break;
        	default:
        		Logger.d(TAG, "Unhandled errorCode - " + errorCode + ", " + description);
    	}
    	
    	super.onReceivedError(view, errorCode, description, failingUrl);
    }
    
    private void showErrorDialog(final String description) {
    	if (null == mActivity || null == mActivity.get()) return;
		DialogUtil.createConfirmDialog(mActivity.get(), new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mActivity.get().finish();
			}
		}, "连接服务器失败", description).show();
    }
    
    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }
    
}

