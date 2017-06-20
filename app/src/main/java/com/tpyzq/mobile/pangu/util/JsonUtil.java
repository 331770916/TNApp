package com.tpyzq.mobile.pangu.util;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Json 转换解析工具类
 */
public class JsonUtil {
	public static Object object2json(Object obj) throws JSONException {
		if (obj == null) {
			return new JSONObject();
		} else if (obj instanceof String || obj instanceof Integer
				|| obj instanceof Float || obj instanceof Boolean
				|| obj instanceof Short || obj instanceof Double
				|| obj instanceof Long || obj instanceof BigDecimal
				|| obj instanceof BigInteger || obj instanceof Byte) {
			return string2json(obj.toString());
		} else if (obj instanceof Object[]) {
			return array2json((Object[]) obj);
		} else if (obj instanceof List) {
			return list2json((List<?>) obj);
		} else if (obj instanceof Map) {
			return map2json((Map<?, ?>) obj);
		} else if (obj instanceof Set) {
			return set2json((Set<?>) obj);
		} else {
			return new JSONObject();
		}
	}

	public static JSONArray list2json(List<?> list) throws JSONException {
		JSONArray arr = new JSONArray();
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				arr.put(object2json(obj));
			}
		}
		return arr;
	}

	public static JSONArray array2json(Object[] array) throws JSONException {
		JSONArray arr = new JSONArray();
		if (array != null && array.length > 0) {
			for (Object obj : array) {
				arr.put(object2json(obj));
			}
		}
		return arr;
	}

	public static JSONObject map2json(Map<?, ?> map) throws JSONException {
		JSONObject json = new JSONObject();
		if (map != null && map.size() > 0) {
			for (Object key : map.keySet()) {
				json.put(String.valueOf(key), object2json(map.get(key)));
			}
		}
		return json;
	}

	public static JSONArray set2json(Set<?> set) throws JSONException {
		JSONArray arr = new JSONArray();
		if (set != null && set.size() > 0) {
			for (Object obj : set) {
				arr.put(object2json(obj));
			}
		}
		return arr;
	}

	public static Object string2json(String s) throws JSONException {
		if (s == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			case '/':
				sb.append("\\/");
				break;
			default:
				if (ch >= '\u0000' && ch <= '\u001F') {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		return sb.toString();
	}
}
