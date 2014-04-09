package org.pushtalk.server.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class StringUtils {
	
	public static boolean equals(String s1, String s2) {
		return ((null == s1 && null == s2)
				|| (null != s1 && s1.equals(s2))
				);
	}
	
	public static String emptyStringIfNull(String s) {
		return (null == s) ? "" : s;
	}
	
	public static String toArrayString(String... array) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (String s : array) {
			sb.append(s).append(", ");
		}
		sb.append("]");
		String s = sb.toString();
		sb.setLength(0);
		sb = null;
		return s;
	}
	
	@SuppressWarnings("rawtypes")
	public static String toListString(List list) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Object o : list) {
			sb.append(o.toString()).append(", ");
		}
		sb.append("]");
		String s = sb.toString();
		sb.setLength(0);
		sb = null;
		return s;
	}
	
	
	public static String toString(ByteArrayInputStream is) {
		int size = is.available();
		char[] theChars = new char[size];
		byte[] bytes = new byte[size];

		is.read(bytes, 0, size);
		for (int i = 0; i < size;)
			theChars[i] = (char) (bytes[i++] & 0xff);

		return new String(theChars);
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

	public static String valueOfInt(int i, int length) {
		return valueOfString(String.valueOf(i), length);
	}

	public static String valueOfLong(long i, int length) {
		return valueOfString(String.valueOf(i), length);
	}
	
	public static String valueOfString(String i, int length) {
		int size = i.length();
		if (size >= length) {
			return i;
		}
		int leadingZeroCount = length - size;
		char[] zeros = new char[leadingZeroCount];
		Arrays.fill(zeros, '0');

		StringBuilder buffer = new StringBuilder();
		buffer.append(zeros);
		buffer.append(i);
		
		String s = buffer.toString();
		buffer.setLength(0);
		buffer = null;
		return s;
	}
	
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isTrimedEmpty(String s) {
        return s == null || s.trim().length() == 0;
    }

    public static boolean isNotEmpty(String s) {
        return s != null && s.length() > 0;
    }
    
    public static String list2String(List<String> list) {
    	StringBuilder sb = new StringBuilder();
    	for (String s : list) {
    		sb.append(s + ",");
    	}
    	String string = sb.toString().trim();
    	if (list.size() > 0) {
    		string = string.substring(0, string.length() - 1);
    	}
    	
    	String s = sb.toString();
    	sb.setLength(0);
    	sb = null;
    	return s;
    }
    
	public static String toMD5(String source) {
		if (null == source || "".equals(source)) return null;
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(source.getBytes());
			return toHex(digest.digest());
		} catch (NoSuchAlgorithmException e) {
		}
		return null;
	}
	
	public static String toMD5(InputStream input, int size) {
		if (null == input || size == 0) return null;
		try {
			byte[] buffer = new byte[size];
			input.read(buffer);
			return toMD5(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String toMD5(byte[] data) {
		if (null == data || data.length == 0) return null;
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(data);
			return toHex(digest.digest());
		} catch (NoSuchAlgorithmException e) {
		}
		return null;
	}
	
	public static String toHex(String txt) {
		return toHex(txt.getBytes());
	}

	public static String fromHex(String hex) {
		return new String(toByte(hex));
	}

	public static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		return result;
	}

	public static String toHex(byte[] buf) {
		if (buf == null) return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	public static String toHexLog(byte[] buf) {
		if (buf == null) return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
			result.append(' ');
		}
		return result.toString();
	}

	private final static String HEX = "0123456789ABCDEF";
	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}
	
	public static synchronized String getGuid() {
		String name = "reg_id" + System.currentTimeMillis();
		return UUID.nameUUIDFromBytes(name.getBytes()).toString();
	}

    public static String padRight(String s, char space, int length) {
        byte[] bs = new byte[length];
        byte[] ss = s.getBytes();
        Arrays.fill(bs, (byte) (space & 0xff));
        System.arraycopy(ss, 0, bs, 0, ss.length);
        return new String(bs);
    }
    
    public static String getRandomString() {
        String name = System.currentTimeMillis() + "";
        return UUID.nameUUIDFromBytes(name.getBytes()).toString();
    }
    
    
}
