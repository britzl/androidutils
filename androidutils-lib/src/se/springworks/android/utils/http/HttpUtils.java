package se.springworks.android.utils.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

public class HttpUtils {

	
	public static String createUrlWithQueryString(String url, Map<String, String> keyValuePairs) {
		if(keyValuePairs == null) {
			return url;
		}
		
        StringBuffer buffer = new StringBuffer();
        Iterator<String> keys = keyValuePairs.keySet().iterator();
        while(keys.hasNext()) {
        	String key = keys.next();
        	String value = keyValuePairs.get(key);
        	buffer.append(key);
        	buffer.append('=');
        	try {
				buffer.append(URLEncoder.encode(value, "UTF-8"));
			}
        	catch (UnsupportedEncodingException e) {
				buffer.append(value);
			}
        	if(keys.hasNext()) {
        		buffer.append('&');
        	}
        }
        if (url.indexOf("?") == -1) {
        	url += "?" + buffer.toString();
        }
        else {
        	url += "&" + buffer.toString();
        }
		return url;
	}
}
