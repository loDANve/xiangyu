package com.lvgou.jj.utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lidroid.xutils.util.LogUtils;
import com.lvgou.jj.Constants;

public class Utils {

	// 获取SharedPreferences
	public static SharedPreferences getSharedPreferences(Context context,
			String spid) {
		SharedPreferences spf = null;
		try {
			spf = context.getSharedPreferences(spid, Context.MODE_PRIVATE);
		} catch (NullPointerException e) {
			// e.printStackTrace();
			return null;
		}
		return spf;
	}

	// 封装SharedPreferences的修改
	public static void editSharedPreferences(Context context,
			Map<String, String> map, String spid) {
		if (null != map && null != context) {
			Editor editor = getSharedPreferences(context, spid).edit();
			for (String key : map.keySet()) {
				editor.putString(key, map.get(key));
			}
			editor.commit();
		}

	}

	public static String getUserName(Context context) {
		return getSharedPreferences(context, Constants.SHARED_PREFERENCE_NAME)
				.getString(Constants.XMPP_USERNAME, "xiangyu");
	}

	public static void initXmpp(Context context) {
		Properties props = loadProperties(context);
		String apiKey = props.getProperty("apiKey", "");
		String xmppHost = props.getProperty("xmppHost", "127.0.0.1");
		String xmppPort = props.getProperty("xmppPort", "5222");
		LogUtils.i("apiKey=" + apiKey);
		LogUtils.i("xmppHost=" + xmppHost);
		LogUtils.i("xmppPort=" + xmppPort);

		SharedPreferences sharedPrefs = getSharedPreferences(context,
				Constants.SHARED_PREFERENCE_NAME);
		Editor editor = sharedPrefs.edit();
		editor.putString(Constants.API_KEY, apiKey);
		editor.putString(Constants.VERSION, "1.0");
		editor.putString(Constants.XMPP_HOST, xmppHost);
		editor.putInt(Constants.XMPP_PORT, Integer.parseInt(xmppPort));
		editor.commit();
	}

	private static Properties loadProperties(Context context) {
		Properties props = new Properties();
		try {
			int id = context.getResources().getIdentifier("androidpn", "raw",
					context.getPackageName());
			props.load(context.getResources().openRawResource(id));
		} catch (Exception e) {
			LogUtils.e("Could not find the properties file.", e);
		}
		return props;
	}

	public static <T> T json2Object(byte[] json, T t) {
		Gson gson = new Gson();
		T object = null;
		// String str = json.trim();
		String str = null;
		try {
			str = new String(json, "utf-8");
			// String str =
			// "﻿{\"code\":1,\"msg\":\"\u8bf7\u6c42\u6210\u529f\"}";
			if (null != t) {
				LogUtils.e(t.getClass().toString() + " : " + str);
			}
			object = (T) gson.fromJson(str, t.getClass());
			// object = (T) getGson().fromJson("﻿{\"code\":4001}",
			// t.getClass());
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return object;
	}
}
