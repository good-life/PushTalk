package org.pushtalk.android.utils;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

public class AndroidUtil {
	
	private static final String TAG = "AndroidUtil";
	
	
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
