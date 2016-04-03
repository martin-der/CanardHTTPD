package net.tetrakoopa.canardhttpd.service;

import android.util.Log;

public class CanardLogger {
	
	private final static String TAG = "Canard-Log";

	public static void d(String message) {
		d(message, null);
	}
	public static void d(String message, Throwable throwable) {
		Log.d(TAG, message, throwable);
	}

	public static void v(String message) {
		v(message, null);
	}
	public static void v(String message, Throwable throwable) {
		Log.v(TAG, message, throwable);
	}

	public static void i(String message) {
		i(message, null);
	}
	public static void i(String message, Throwable throwable) {
		Log.i(TAG, message, throwable);
	}

	public static void w(String message) {
		w(message, null);
	}
	public static void w(String message, Throwable throwable) {
		Log.w(TAG, message, throwable);
	}

	public static void e(String message) {
		e(message, null);
	}
	public static void e(String message, Throwable throwable) {
		Log.e(TAG, message, throwable);
	}
}
