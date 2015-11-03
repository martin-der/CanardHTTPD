package net.tetrakoopa.canardhttpd.service;

import android.util.Log;

public class CanardLogger {
	
	private final static String TAG = "Canard";

	public static void d(String message) {
		Log.d(TAG, message);
	}
	public static void d(String message, Throwable throwable) {
		Log.d(TAG, message, throwable);
	}

	public static void v(String message) {
		Log.v(TAG, message);
	}

	public static void v(String message, Throwable throwable) {
		Log.v(TAG, message, throwable);
	}

	public static void i(String message) {
		Log.i(TAG, message);
	}
	public static void i(String message, Throwable throwable) {
		Log.i(TAG, message, throwable);
	}

	public static void w(String message) {
		Log.w(TAG, message);
	}
	public static void w(String message, Throwable throwable) {
		Log.w(TAG, message, throwable);
	}

	public static void e(String message) {
		Log.e(TAG, message);
	}
	public static void e(String message, Throwable throwable) {
		Log.e(TAG, message, throwable);
	}
}
