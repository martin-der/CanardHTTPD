package net.tetrakoopa.canardhttpd.service;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import net.tetrakoopa.canardhttpd.domain.EventLog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class CanardLogger {

	private final static String TAG = "Canard-Log";

	private final FileWriter writer;

	public CanardLogger(Context context, String filename) throws IOException {
		final File location = getLocation(context);
		//final File location = new File(Environment.getExternalStorageDirectory(), filename);
		writer = new FileWriter(location, true);
	}

	public static File getLocation(Context context) {
		return new File(context.getFilesDir(), "canard-log.txt");
	}

	public void close() throws IOException {
		writer.close();
	}

	public synchronized void log(EventLog event) {
		log(event.getSeverity(), event.getType(), event.getDate(), event.getUser(), event.getExtras());
	}
	public void log(EventLog.Severity severity, EventLog.Type type, Date date, String user, String... extras) {
		try {
			writer.append(severity.name());
			writer.append("|");
			writer.append(type.name());
			writer.append("|");
			writer.append(date == null ? "" :String.valueOf(date.getTime()));
//			writer.append("|");
//			writer.append(message);
			writer.append("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void d(String message) {
		d(message, null);
	}
	public static void d(String message, Throwable throwable) {
		//log(null, message, throwable);
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
