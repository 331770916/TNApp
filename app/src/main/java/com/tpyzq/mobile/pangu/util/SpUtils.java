package com.tpyzq.mobile.pangu.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tpyzq.mobile.pangu.data.FunctionEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 通过SharedPreferences存取数据的工具类
 */
public class SpUtils {
	private static SharedPreferences sharedPreferences;
	private static SharedPreferences.Editor editor;
	private static String config = "pangu";

	/**
	 * 存储String数据
	 */
	public static String putString(Context context, String Key, String Value) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(config, Context.MODE_PRIVATE);
			editor = sharedPreferences.edit();
		}
		editor.putString(Key, Value).commit();
		return Key;
	}

	/**
	 * 拿取String数据
	 */
	public static String getString(Context context, String Key, String defvalue) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(config, Context.MODE_PRIVATE);
		}
		return sharedPreferences.getString(Key, defvalue);
	}

	// 保存boolean值
	public static void putBoolean(Context context, String key, boolean value) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(config, Context.MODE_PRIVATE);
		}
		editor = sharedPreferences.edit();
		editor.putBoolean(key, value).commit();
	}

	// 取boolean值
	public static boolean getBoolean(Context context, String key,
									 boolean defValue) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(config, Context.MODE_PRIVATE);
		}
		return sharedPreferences.getBoolean(key, defValue);
	}

	// 保存int值
	public static void putInt(Context context, String key, int value) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(config, Context.MODE_PRIVATE);
			editor = sharedPreferences.edit();
		}
		editor.putInt(key, value).commit();
	}

	//移除值
	public static void removeKey(Context context,String key){
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(config, Context.MODE_PRIVATE);
			editor = sharedPreferences.edit();
		}
		editor.remove(key).commit();
	}


	// 取int值
	public static int getInt(Context context, String key,int defValue) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(config, Context.MODE_PRIVATE);
		}
		return sharedPreferences.getInt(key, defValue);
	}

	public static void setDataList(Context context,String tag, List<FunctionEntity> datalist) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(config, Context.MODE_PRIVATE);
			editor = sharedPreferences.edit();
		}
		if (null == datalist || datalist.size() <= 0)
			return;
		Gson gson = new Gson();
		//转换成json数据，再保存
		String strJson = gson.toJson(datalist);
		editor.putString(tag, strJson);
		editor.commit();

	}

	/**
	 * 获取List
	 * @param tag
	 * @return
	 */
	public static ArrayList<FunctionEntity> getDataList(Context context, String tag) {
		if (sharedPreferences == null) {
			sharedPreferences = context.getSharedPreferences(config, Context.MODE_PRIVATE);
			editor = sharedPreferences.edit();
		}
		ArrayList datalist=new ArrayList();
		String strJson = sharedPreferences.getString(tag, null);
		if (null == strJson) {
			return datalist;
		}
		Gson gson = new Gson();
		datalist = gson.fromJson(strJson, new TypeToken<ArrayList<FunctionEntity>>() {
		}.getType());
		return datalist;
	}
}