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
			writer.append("|");
			writer.append(user == null ? "" :user);
			writer.append("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void d(EventLog.Type type, String... extras) {
		d(type, new Date(), null, extras);
	}
	public void d(EventLog.Type type, Date date, String user, String... extras) {
		log(EventLog.Severity.DEBUG, type, date, user, extras);
	}

	public void i(EventLog.Type type, String... extras) {
		i(type, new Date(), null, extras);
	}
	public void i(EventLog.Type type, Date date, String user, String... extras) {
		log(EventLog.Severity.INFO, type, date, user, extras);
	}

	public void w(EventLog.Type type, String... extras) {
		w(type, new Date(), null, extras);
	}
	public void w(EventLog.Type type, Date date, String user, String... extras) {
		log(EventLog.Severity.WARN, type, date, user, extras);
	}

	public void e(EventLog.Type type, String... extras) {
		e(type, new Date(), null, extras);
	}
	public void e(EventLog.Type type, Date date, String user, String... extras) {
		log(EventLog.Severity.ERROR, type, date, user, extras);
	}
}
