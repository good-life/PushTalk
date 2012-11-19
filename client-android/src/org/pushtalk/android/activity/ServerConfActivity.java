package org.pushtalk.android.activity;

import java.util.ArrayList;
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
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ServerConfActivity extends Activity {
	private static final String TAG = "ServerConfActivity";

	private List<String> serverList = new ArrayList<String>();
	private Spinner mSpinner;
	private EditText serverName;
	private EditText serverHost;
	private EditText serverPort;
	private Button addButton;
	private ArrayAdapter<String> adapter;
	private String selectedKey = null;
	public static final String EXTRA_MESSAGE = "ServerConfActivity.MESSAGE";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serverconf);

		findWidget();
		configSpinner();

		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addNewServer(v);
			}
		});
	}

	private void findWidget() {
		mSpinner = (Spinner) findViewById(R.id.server_list);
		serverName = (EditText) findViewById(R.id.server_name_field);
		serverHost = (EditText) findViewById(R.id.server_host_field);
		serverPort = (EditText) findViewById(R.id.server_port_field);
		addButton = (Button) findViewById(R.id.btn_add);
	}

	// 配置Spinner，处理item选择事件
	private void configSpinner() {
		SharedPreferences sharedata = getSharedPreferences("serverlist", 0);
		final Map<String, ?> data = sharedata.getAll();
		serverList.addAll(Config.serverList.keySet());
		serverList.addAll(data.keySet());

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, serverList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setPromptId(R.string.prompt_server);
		mSpinner.setAdapter(adapter);

		mSpinner.setSelection(0, false);

		mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				if (arg2 != 0) {

					selectedKey = adapter.getItem(arg2);

					String url = "";
					if (data.containsKey(selectedKey)) {
						url = data.get(selectedKey).toString();
					} else if (Config.serverList.containsKey(selectedKey)) {
						url = Config.serverList.get(selectedKey);
					}
					Logger.v(TAG, "Selected host: " + selectedKey + " " + url);

					Intent intent = new Intent(ServerConfActivity.this,
							WebPageActivity.class);
					intent.putExtra(EXTRA_MESSAGE, url);
					startActivity(intent);
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
			Logger.v(TAG, "Add server " + HOST);

			Intent intent = new Intent(ServerConfActivity.this,
					WebPageActivity.class);
			intent.putExtra(EXTRA_MESSAGE, HOST);
			startActivity(intent);
		}
	}
}
