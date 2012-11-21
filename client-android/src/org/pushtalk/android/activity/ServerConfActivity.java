package org.pushtalk.android.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.pushtalk.android.Config;
import org.pushtalk.android.R;
import org.pushtalk.android.utils.Logger;
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
import android.widget.Toast;

public class ServerConfActivity extends Activity {
	private static final String TAG = "ServerConfActivity";

	private List<String> serverList = new ArrayList<String>();
	private ArrayAdapter<String> spinnerAdapter;
	private SharedPreferences sharedata;
	private Map<String, ?> data;
	private Spinner mSpinner;
	private TextView mTitleText;
	private TextView mAddressText;
	private EditText serverName;
	private EditText serverHost;
	private EditText serverPort;
	private Button backButton;
	private Button addButton;
	private Button confButton;
	private String serverUrl = "";
	private String selectedKey = null;
	public static final String EXTRA_MESSAGE = "ServerConfActivity.MESSAGE";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serverconf);
		initView();
		getServerListData();
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
		
		confButton = new Button(getApplicationContext());
		confButton.setText(R.string.comfirm);
		confButton.setGravity(Gravity.CENTER);
		confButton.setTextColor(Color.WHITE);
		confButton.setBackgroundResource(R.drawable.function_button_selector);
		confButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!isEmpty(serverUrl)){
					Intent intent = new Intent(ServerConfActivity.this,WebPageActivity.class);
					intent.putExtra(EXTRA_MESSAGE, serverUrl);
					startActivity(intent);
				}else{
					Toast.makeText(getApplicationContext(), getString(R.string.server_url_null_alert), Toast.LENGTH_SHORT).show();
				}
				finish();
			}
		});
		((LinearLayout) findViewById(R.id.add_leftView)).addView(backButton,lParams);
		((LinearLayout) findViewById(R.id.add_rightView)).addView(confButton,lParams);
	}
	
	private void getServerListData(){
		sharedata = getSharedPreferences("serverlist", 0);
		data = sharedata.getAll();
		serverList.addAll(Config.serverList.keySet());
		serverList.addAll(data.keySet());
	}
	
	private void refreshAdpter(){
		serverList.clear();
		getServerListData();
		spinnerAdapter.notifyDataSetChanged();
	}
	
	private String getAddressByPosition(int position){
		String serverAddress = "";
		String serverKeyName = serverList.get(position);
		if(position > 0 && data.containsKey(serverKeyName)){
			 serverAddress = data.get(serverKeyName).toString();
		}else{
			serverAddress = Config.serverList.get(serverKeyName);
		}
		return serverAddress;
	}

	// 配置Spinner，处理item选择事件
	private void configSpinner() {
		int spinnerPosition =  getSharedPreferences("ServerListPosition", 0).getInt("ServerPosition", 0);
		mAddressText.setText(getAddressByPosition(spinnerPosition));
		spinnerAdapter = new ArrayAdapter<String>(this, R.layout.define_spinner,serverList) {
			@Override
			public View getDropDownView(int position, View convertView,ViewGroup parent) {
				View view = getLayoutInflater().inflate(R.layout.define_spinner_adapter_item, parent, false);
				TextView label = (TextView) view.findViewById(R.id.label);
				ImageView checkedView = (ImageView)view.findViewById(R.id.icon);
				label.setText(getItem(position));
				if (mSpinner.getSelectedItemPosition() == position) {
					label.setTextColor(getResources().getColor(R.color.common_blue));
					checkedView.setVisibility(View.VISIBLE);
					view.setBackgroundColor(Color.rgb(255, 255, 203));
				}
				return view;
			}
		};
		
		mSpinner.setPromptId(R.string.prompt_server);
		mSpinner.setAdapter(spinnerAdapter);
		mSpinner.setSelection(spinnerPosition, false);
		mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View view,int arg2, long arg3) {
				if (arg2 != 0) {
					selectedKey = spinnerAdapter.getItem(arg2).toString();
					if (data.containsKey(selectedKey)) {
						serverUrl = data.get(selectedKey).toString();
					} else if (Config.serverList.containsKey(selectedKey)) {
						serverUrl = Config.serverList.get(selectedKey);
					}
					mAddressText.setText(serverUrl);
					Editor sharedata = getSharedPreferences("ServerListPosition", 0).edit();
					sharedata.putInt("ServerPosition", arg2);
					sharedata.commit();
					Logger.v(TAG, "Selected host: " + selectedKey + " " + serverUrl);
				}else{
					mAddressText.setText("");
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
			String HOST = "http://" + host + ":" + port;
			Editor sharedata = getSharedPreferences("serverlist", 0).edit();
			sharedata.putString(name, HOST);
			sharedata.commit();
			refreshAdpter();
			mAddressText.setText(HOST);
			int newServerPosition = getPositionFromMap(name);
			mSpinner.setSelection(newServerPosition, false);
			Logger.v(TAG, "Add server " + HOST);
			Intent intent = new Intent(ServerConfActivity.this,	WebPageActivity.class);
			intent.putExtra(EXTRA_MESSAGE, HOST);
			startActivity(intent);
		}
	}
	
	
	private int getPositionFromMap(String keyName){
		Iterator iter = data.entrySet().iterator(); 
		int position = 0;
		while (iter.hasNext()) { 
			position ++;
		    Map.Entry entry = (Map.Entry) iter.next(); 
		    Object key = entry.getKey(); 
		    if(String.valueOf(key).equals(keyName)){
		    	break;
		    }
		} 
		return position + Config.serverList.size() - 1;
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
