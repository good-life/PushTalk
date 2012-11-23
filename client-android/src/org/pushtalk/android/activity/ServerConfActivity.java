package org.pushtalk.android.activity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.pushtalk.android.Config;
import org.pushtalk.android.Constants;
import org.pushtalk.android.R;
import org.pushtalk.android.utils.Logger;
import org.pushtalk.android.utils.MyPreferenceManager;
import org.pushtalk.android.utils.StringUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class ServerConfActivity extends Activity {
	private static final String TAG = "ServerConfActivity";
	private static final String PREF_SERVER_LIST = "pushtalk_server_list";
	
	private List<String> mServerNameList = new ArrayList<String>();
	private ArrayAdapter<String> spinnerAdapter;
	private SharedPreferences sharedPreference;
	private Map<String, String> mAllServerMap;
	
	private Spinner mSpinner;
	private TextView mTitleText;
	private TextView mAddressText;
	private EditText serverName;
	private EditText serverHost;
	private EditText serverPort;
	private Button backButton;
	private Button addButton;
	
	private String serverUrl = null;
	private String selectedKey = null;
	
	public static final String EXTRA_MESSAGE = "ServerConfActivity.MESSAGE";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serverconf);
		mAllServerMap = new LinkedHashMap<String, String>();
		
		initView();
		
		resetServerListData();
		
		configSpinner();
	}

	private void initView() {
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		
		mTitleText = (TextView)findViewById(R.id.view_title);
		mTitleText.setText(R.string.server_settings);
		mAddressText = (TextView)findViewById(R.id.current_address_text);
		mSpinner = (Spinner) findViewById(R.id.server_list);
		serverName = (EditText) findViewById(R.id.server_name_field);
		serverHost = (EditText) findViewById(R.id.server_host_field);
		serverPort = (EditText) findViewById(R.id.server_port_field);
		
		addButton = (Button) findViewById(R.id.btn_add);
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addNewServer(v);
			}
		});
		
		backButton = new Button(getApplicationContext());
		backButton.setPadding(-1, 0, -1, 0);
		backButton.setText(R.string.btn_back);
		backButton.setGravity(Gravity.CENTER);
		backButton.setTextColor(Color.WHITE);
		backButton.setBackgroundResource(R.drawable.opt_back_action_selector);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
				
		((LinearLayout) findViewById(R.id.add_leftView)).addView(backButton,lParams);
	}
	
    private void resetServerListData() {
		sharedPreference = getSharedPreferences(PREF_SERVER_LIST, 0);
		
		// dump fixed servers into prefs
        Editor editor = sharedPreference.edit();
		for (String builtinName : Config.serverList.keySet()) {
		    editor.putString(builtinName, Config.serverList.get(builtinName));
		}
		editor.commit();
		
		// dump all into local params
        dumpPreferencesIntoMap();
		mServerNameList.addAll(mAllServerMap.keySet());
	}
    
    private void dumpPreferencesIntoMap() {
        for (String key : sharedPreference.getAll().keySet()) {
            String url = sharedPreference.getString(key, "");
            Logger.d(TAG, "dump server - name:" + key + ", url:" + url);
            mAllServerMap.put(key, url);
        }
    }
	
	private void refreshAdpter(){
		mServerNameList.clear();
		resetServerListData();
		spinnerAdapter.notifyDataSetChanged();
	}
	
	private void configSpinner() {
		spinnerAdapter = new ArrayAdapter<String>(this, R.layout.define_spinner, mServerNameList) {
			@Override
			public View getDropDownView(int position, View convertView,ViewGroup parent) {
				View view = getLayoutInflater().inflate(R.layout.define_spinner_adapter_item, parent, false);
				TextView label = (TextView) view.findViewById(R.id.label);
				ImageView checkedView = (ImageView)view.findViewById(R.id.icon);
				label.setText(getItem(position));
				if (mSpinner.getSelectedItemPosition() == position) {
					if(position == 0){
						label.setText(getItem(position) + getString(R.string.default_value));
					}
					label.setTextColor(getResources().getColor(R.color.common_blue));
					checkedView.setVisibility(View.VISIBLE);
					view.setBackgroundColor(Color.rgb(255, 255, 203));
				}
				return view;
			}
		};
        mSpinner.setAdapter(spinnerAdapter);
		
		String currentUrl = MyPreferenceManager.getString(Constants.PREF_CURRENT_SERVER, null);
		Logger.d(TAG, "Current server url: " + currentUrl);
        mAddressText.setText(currentUrl);
        
	    int spinnerPosition = getPositionFromMap(currentUrl);
	    Logger.d(TAG, "Current spinner position: " + spinnerPosition);
        mSpinner.setSelection(spinnerPosition, false);
        
		mSpinner.setPromptId(R.string.prompt_server);
		mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
				if (arg2 >= 0) {
					selectedKey = spinnerAdapter.getItem(arg2).toString();
					if (mAllServerMap.containsKey(selectedKey)) {
						serverUrl = mAllServerMap.get(selectedKey);
					}
					if (StringUtils.isEmpty(serverUrl)) {
					    Logger.w(TAG, "No serverUrl for the selectedKey - " + selectedKey);
					    return;
					}
					
					mAddressText.setText(serverUrl);
					
					Logger.v(TAG, "Selected host: " + selectedKey + " " + serverUrl);
                    Config.SERVER = serverUrl;
                    MyPreferenceManager.commitString(Constants.PREF_CURRENT_SERVER, serverUrl);
					
                    Intent intent = new Intent(ServerConfActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                    
				}else{
				    Logger.d(TAG, "Unvalid select position");
					mAddressText.setText("");
					serverUrl = "";
				}
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {
				arg0.setVisibility(View.INVISIBLE);
			}
		});
	}
	

	private void addNewServer(View view) {
		Logger.v(TAG, "Add server function start");
		String name = serverName.getText().toString().trim();
		String host = serverHost.getText().toString().trim();
		String port = serverPort.getText().toString().trim();

		if (StringUtils.isEmpty(name)) {
			serverName.requestFocus();
		} else if (StringUtils.isEmpty(host)) {
			serverHost.requestFocus();
		} else if (!org.pushtalk.android.utils.StringUtils.isInteger(port)) {
			serverPort.requestFocus();
		} else {
			Logger.v(TAG, "Add server start");
			String serverUrl = "http://" + host + ":" + port;
			
			Editor sharedata = getSharedPreferences(PREF_SERVER_LIST, 0).edit();
			sharedata.putString(name, serverUrl);
			sharedata.commit();
			
			refreshAdpter();
			
			mAddressText.setText(serverUrl);
			int newServerPosition = getPositionFromMap(serverUrl);
			mSpinner.setSelection(newServerPosition, false);
			
			Logger.v(TAG, "Added new server - name:" + name + ", url:" + serverUrl);
		}
	}
	
	private int getPositionFromServerList(String serverName) {
	    int i = 0;
	    for (String s : mServerNameList) {
	        if (s.equalsIgnoreCase(serverName)) {
	            return i;
	        }
	        i ++;
	    }
	    return -1;
	}
	
	private int getPositionFromMap(String keyName) {
	    int pos = 0;
	    for (String server : mAllServerMap.values()) {
	        if (server.equalsIgnoreCase(keyName)) {
	            return pos;
	        }
	        pos ++;
	    }
	    return -1;
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
	}

	private boolean isEmpty(String s) {
		if (null == s) return true;
		if (s.length() == 0) return true;
		if (s.trim().length() == 0) return true;
		return false;
	}
}
