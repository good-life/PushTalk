package org.pushtalk.android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

public class AndroidUtil {
	
	private static final String TAG = "AndroidUtil";
	
    private static List<String> INVALID_IMEIs = new ArrayList<String>();
    static {
        INVALID_IMEIs.add("358673013795895");
        INVALID_IMEIs.add("004999010640000");
        INVALID_IMEIs.add("00000000000000");
        INVALID_IMEIs.add("000000000000000");
    }
    
    public static boolean isValidImei(String imei) {
        if (StringUtils.isEmpty(imei)) return false;
        if (imei.length() < 10) return false;
        if (INVALID_IMEIs.contains(imei)) return false;
        return true;
    }
    
    private static final String INVALID_ANDROIDID = "9774d56d682e549c";
	
	/** 
	 * 取得设备的唯一标识
	 * 
	 * imei -> androidId -> mac address -> uuid saved in sdcard
	 * 
	 * @param context
	 * @return
	 */
    public static String getUdid(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();

        if (AndroidUtil.isValidImei(imei)) {
            return imei;
        }
        
        String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        if (!StringUtils.isEmpty(androidId) && !INVALID_ANDROIDID.equals(androidId.toLowerCase())) {
            return androidId;
        }
        
        String macAddress = AndroidUtil.getWifiMacAddress(context);
        if (!StringUtils.isEmpty(macAddress)) {
            String udid = StringUtils.toMD5(macAddress
                    + Build.MODEL + Build.MANUFACTURER
                    + Build.ID + Build.DEVICE);
            return udid;
        }
        
        return getSavedUuid();
    }
    
    private static final String UDID_PATH = Environment.getExternalStorageDirectory().getPath() + "/data/.pushtalk_udid";
    private static final String UDID_PREF_KEY = "pushtalk_udid";
    private static String getSavedUuid() {
        String udid = MyPreferenceManager.getString(UDID_PREF_KEY, null);
        if (null != udid) return udid;
        
        File file = new File(UDID_PATH);
        if (file.exists()) {
            try {
                ArrayList<String> content = FileUtil.readLines(new FileInputStream(file));
                if (content.size() > 0) {
                    udid = content.get(0);
                    MyPreferenceManager.commitString(UDID_PREF_KEY, udid);
                    Logger.i(TAG, "Got sdcard file saved udid - " + udid);
                    return udid;
                }
            } catch (FileNotFoundException e) {
            }
        }
        
        String name = System.currentTimeMillis() + "";
        udid = UUID.nameUUIDFromBytes(name.getBytes()).toString();
        udid = StringUtils.toMD5(udid);
        
        MyPreferenceManager.commitString(UDID_PREF_KEY, udid);
        
        try {
            file.createNewFile();
        } catch (IOException e) {
            Logger.e(TAG, "Create file in sdcard error", e);
            return udid;
        }
        
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(udid.getBytes());
            fos.flush();
            Logger.i(TAG, "Saved udid into file");
        } catch (IOException e) {
            Logger.e(TAG, "write file error", e);
        } finally {
            try {
                if (null != fos) {
                    fos.close();
                }
            } catch (IOException e) {
            }
        }
        
        return udid;
    }
    
    public static String getWifiMacAddress(final Context context) {
        try {
            WifiManager wifimanager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            String mac = wifimanager.getConnectionInfo().getMacAddress();
            if (StringUtils.isEmpty(mac)) return null;
            return mac;
        } catch (Exception e) {
            Logger.d(TAG, "Get wifi mac address error", e);
        }
        return null;
    }

	
    // 打印所有的 intent extra 数据
    public static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
        }
        return sb.toString();
    }
	
    public static String getNetworkTypeName(int type) {
    	String name = "Unknown";
    	switch (type) {
			case TelephonyManager.NETWORK_TYPE_GPRS:
				name = "GPRS";break;
			case TelephonyManager.NETWORK_TYPE_EDGE:
				name = "EDGE";break;
    		case TelephonyManager.NETWORK_TYPE_CDMA:
    			name = "CDMA";break;
    		case TelephonyManager.NETWORK_TYPE_EVDO_0:
				name = "EVDO_0";break;
    		case TelephonyManager.NETWORK_TYPE_EVDO_A:
				name = "EVDO_A";break; 
    		case TelephonyManager.NETWORK_TYPE_HSDPA:
    			name = "HSDPA";break;
    		case TelephonyManager.NETWORK_TYPE_HSPA:
    			name = "HSPA";break;
    		case TelephonyManager.NETWORK_TYPE_HSUPA:
				name = "HSUPA";break;
    		case TelephonyManager.NETWORK_TYPE_UMTS:
				name = "UMTS";break;
    		default: 
    	}
    	
    	return name;
    }
    
    public static boolean is2gNetwork(Context context) {
        TelephonyManager tm = (TelephonyManager) context.
			getSystemService(Context.TELEPHONY_SERVICE);
	    int type = tm.getNetworkType();
	    if (type == TelephonyManager.NETWORK_TYPE_GPRS
	    		|| type == TelephonyManager.NETWORK_TYPE_EDGE) {
	    	return true;
	    }
	    return false;
    }
    
    
	public static int getCurrentSdkVersion() {
		return Build.VERSION.SDK_INT;
	}
	
	public Location getLocation(Context context) {
		LocationManager locMan = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		Location location = locMan
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location == null) {
			location = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}
		return location;
	}
	
	public static void sendEmail(Context context, String chooserTitle, 
			String mailAddress, String subject, String preContent) {
        Logger.v(TAG, "action: sendEmail");
	    final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
	    emailIntent.setType("plain/text");
	    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ mailAddress });
	    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
	    String content = "\n\n=====================\n";
	    content += "Device Environment: \n----\n" + preContent;
	    emailIntent.putExtra(Intent.EXTRA_TEXT, content);
	    context.startActivity(Intent.createChooser(emailIntent, chooserTitle));
	}
	
	
	// some apps only show content, some apps show both subject and content
	public static Intent getAndroidShareIntent(CharSequence chooseTitle, 
			CharSequence subject, CharSequence content) {
    	Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
    	shareIntent.setType("text/plain");
    	shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
    	shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
    	return Intent.createChooser(shareIntent, chooseTitle);
	}
	
	public static Intent getAndroidImageShareIntent(CharSequence chooseTitle, 
			String pathfile) {
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("image/*");
		share.putExtra(Intent.EXTRA_STREAM, Uri.parse(pathfile));
		return Intent.createChooser(share, chooseTitle);
	}
	
	public static String getImei(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}
	
	public static String getImsi(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSubscriberId();
	}
	
	public static String getSimSerialNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSimSerialNumber();
	}
	
}
