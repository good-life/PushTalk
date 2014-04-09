package org.pushtalk.server;

public class Config {
	
    // Should be assign value when db init
    public static String SERVER_ID;
    
    public static final String JPUSH_MASTER_SECRET = "6b316faa5969d2fe40c8feec";
    public static final String JPUSH_APPKEY = "b82c29c3905986e698054726";
    
    public static final String VERSION = "2.0";
    
    
    public static final int CACHE_DURATION_DAYS = 7;
    public static final int MESSAGE_CACHE_MAX_NUMBER = 100;
    public static final int RECENT_CHATS_CACHE_MAX_NUMBER = 10;

}
