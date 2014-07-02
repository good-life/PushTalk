package org.pushtalk.android.activity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pushtalk.android.Config;
import org.pushtalk.android.Constants;
import org.pushtalk.android.Global;
import org.pushtalk.android.R;
import org.pushtalk.android.utils.HttpHelper;
import org.pushtalk.android.utils.Logger;
import org.pushtalk.android.utils.MyPreferenceManager;
import org.pushtalk.android.web.TalkWebChromeClient;
import org.pushtalk.android.web.TalkWebViewCallback;
import org.pushtalk.android.web.TalkWebViewClient;
import org.pushtalk.android.web.WebHelper;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

@SuppressLint("JavascriptInterface")
public class MainActivity extends WebBaseActivity {
	private static final String TAG = "MainActivity";

	private TextView mTitleView;
	private Button backButton;
	
	private boolean isMainPage;

	public void setTitle(String pageTitle) {
		mTitleView.setText(pageTitle);
	}

	public void setBackButtonName(String backButtonName) {
		backButton.setText(backButtonName);
	}

	@Override
	public void onPageFinished(String url) {
		if (null != url) {
			if (Global.isAccessMainPage(url)) {
				isMainPage = true;
			} else {
				isMainPage = false;
			}
			
			if (Global.isAccessChattingPage(url)) {
			    String chatting = Global.getCurrentChatting(url);
			    Logger.d(TAG, "is now chatting with - " + chatting);
			    MyPreferenceManager.commitString(Constants.PREF_CURRENT_CHATTING, chatting);
			}
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		Logger.d(TAG, "onCreate");

		setContentView(R.layout.webpage);

		// 设置返回键
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		
		backButton = new Button(getApplicationContext());
		backButton.setPadding(-1, 0, -1, 0);
		backButton.setText(R.string.btn_back);
		backButton.setGravity(Gravity.CENTER);
		backButton.setTextColor(Color.WHITE);
		backButton.setBackgroundResource(R.drawable.opt_back_action_selector);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mWebView.canGoBack() && !isMainPage) {
					mWebView.goBack();
				} else {
					finish();
				}
			}
		});

		Button confButton = new Button(getApplicationContext());
		confButton.setText(R.string.settings);
		confButton.setGravity(Gravity.CENTER);
		confButton.setTextColor(Color.WHITE);
		confButton.setBackgroundResource(R.drawable.function_button_selector);
		confButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ServerConfActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
			}
		});

		((LinearLayout) findViewById(R.id.add_leftView)).addView(backButton,lParams);
		((LinearLayout) findViewById(R.id.add_rightView)).addView(confButton,lParams);

		CookieSyncManager.createInstance(this);
		CookieSyncManager.getInstance().startSync();

		mWebView = (WebView) findViewById(R.id.webpage);
		mWebView.setWebViewClient(new TalkWebViewClient(this));
		mWebView.setWebChromeClient(new TalkWebChromeClient(this));
		WebHelper.setDefaultWebviewSettings(mWebView);
		WebHelper.setAppCacheWebviewSettings(getApplicationContext(), mWebView);

		TalkWebViewCallback webViewCallback = new TalkWebViewCallback(this);
		webViewCallback.setCallBackHandler(mHandler);
		mWebView.addJavascriptInterface(webViewCallback, Config.WEB_JS_MODULE);

		mTitleView = ((TextView) findViewById(R.id.view_title));

		registerMessageReceiver();

		newThreadToReset();

		loadUrlWithWebView(getIntent(), true);
	}

	private void loadUrlWithWebView(Intent intent, boolean newCreated) {
		String url = null;
		String host = intent.getStringExtra(ServerConfActivity.EXTRA_MESSAGE);
        if(host == null){
        	host = Config.SERVER;
        }
        
		Bundle bundle = intent.getExtras();
		if (null != bundle) {
			String chatting = bundle.getString(Constants.KEY_CHATTING);
			
			if (null != chatting) {
				String path = null;
				if (newCreated) {
					path = Constants.PATH_CHATTING;
				} else {
					path = Constants.PATH_CHATTING;
				}

				url = Global.getPathUrl(getApplicationContext(), path, host);
				boolean isChannel = bundle.getBoolean(Constants.KEY_IS_CHANNEL);
				url = WebHelper.attachParamsToUrl(url, new String[] {
						Constants.KEY_CHATTING, chatting }, new String[] {
						Constants.KEY_IS_CHANNEL, String.valueOf(isChannel) });

			} else {
				url = Global.getPathUrl(getApplicationContext(), null, host);
			}
		} else {
			url = Global.getPathUrl(getApplicationContext(), null, host);
		}

		Logger.i(TAG, "load url: " + url);
		mWebView.loadUrl(url);
	}

	@Override
	public void onNewIntent(Intent intent) {
		Logger.d(TAG, "onNewIntent");
		loadUrlWithWebView(intent, false);
	}

	@Override
	public void onResume() {
		Config.isBackground = false;

		if (!mWebView.canGoBack()) {
			backButton.setText(R.string.btn_close);
		}

		super.onResume();
	}

	@Override
	public void onPause() {
		Config.isBackground = true;
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		Logger.v(TAG, "onDestroy()");

		mHandler.removeCallbacksAndMessages(null);
		CookieSyncManager.getInstance().stopSync();

		unregisterReceiver(mMessageReceiver);

		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)
				&& (mWebView.canGoBack() && !isMainPage)) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private MessageReceiver mMessageReceiver;

	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public static final String MESSAGE_RECEIVED_ACTION = "org.pushtalk.android.MESSAGE_RECEIVED_ACTION";

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
//                String title = intent.getStringExtra(Constants.KEY_TITLE);
//                String channel = intent.getStringExtra(Constants.KEY_CHANNEL);
			    
                String all = intent.getStringExtra(Constants.KEY_ALL);
				String receivedMessage = "javascript:receivedMessage('" + all + "')";
				mWebView.loadUrl(receivedMessage);
			}
		}
	}

	private void resetAliasAndTags() {
		// download from pushtalk server
		Map<String, String> params = new HashMap<String, String>();
		params.put("udid", Config.udid);

		String userInfo = null;
		try {
			userInfo = HttpHelper.post(Constants.PATH_USER, params);
		} catch (Exception e) {
			Logger.e(TAG, "Call pushtalk api to get user info error", e);
			return;
		}

		Logger.d(TAG, "Original user info - " + userInfo);
		if (null == userInfo) {
			Logger.w("TAG", "Unexpected: failed to get user info from server.");
			return;
		}

		Set<String> myChannels = new HashSet<String>();
		String myChattingName = null;
		try {
			JSONObject json = new JSONObject(userInfo);
			String username = json.optString("username");
			if (null != username) {
			    myChattingName = username;
			    String[] s = username.split("_");
			    if (s.length > 1) {
			        Config.myName = s[0];
			    } else {
			        Logger.w(TAG, "Unexpected: failed to get myName.");
			    }
			}

			JSONArray array = json.getJSONArray("channels");
			int size = array.length();

			for (int i = 0; i < size; i++) {
				myChannels.add(array.getString(i));
			}
		} catch (JSONException e) {
			Logger.e(TAG, "Parse user info json error", e);
			return;
		}
		if (Config.myChannels != null) {
			Config.myChannels.clear();
		}
		Config.myChannels = myChannels;

		// reset to jpush
		JPushInterface.setAliasAndTags(MainActivity.this, myChattingName, myChannels, new TagAliasCallback() {
			
			@Override
			public void gotResult(int code, String alias, Set<String> tags) {
				Log.d(TAG, "[TagAliasCallback], code: " + code);
				Log.d(TAG, "[TagAliasCallback], code: " + alias);
				Log.d(TAG, "[TagAliasCallback], code: " + tags);
			}
		});
	}

	private void newThreadToReset() {
		new Thread() {
			public void run() {
				resetAliasAndTags();
			}
		}.start();
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case TalkWebViewCallback.MSG_UPDATE_CHANNEL:
				newThreadToReset();
				break;
			case TalkWebViewCallback.MSG_UPDATE_USERNAME:
				newThreadToReset();
				break;
			default:
				;
			}
		}
	};

}