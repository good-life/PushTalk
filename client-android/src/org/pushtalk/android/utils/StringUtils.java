package org.pushtalk.android.utils;


public class StringUtils {

    /**
     * will trim the string
     * 
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        if (null == s) return true;
        if (s.length() == 0) return true;
        if (s.trim().length() == 0) return true;
        return false;
    }
    
    public static String emptyStringIfNull(String s) {
        return (null == s) ? "" : s;
    }

    public static String humanReadableByteCount(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = "KMGTPE".charAt(exp-1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
    
    public static String fixedLengthString(String s, int expectedLength) {
        int l = s.length();
        if (l >= expectedLength) {
            return s;
            //return s.substring(0, expectedLength);
        }
        for (int i = 0; i < expectedLength - l; i++) {
            s = s + " ";
        }
        return s;
    }
    
    public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

    
}
