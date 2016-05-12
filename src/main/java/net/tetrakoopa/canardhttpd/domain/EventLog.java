package net.tetrakoopa.canardhttpd.domain;

import net.tetrakoopa.canardhttpd.R;

import java.util.Date;
import java.util.ServiceConfigurationError;

public class EventLog {

	public enum Severity {
		DEBUG, INFO, WARN, ERROR
	}

	public enum Origin {
		WEB_SERVER, MAIN_ACTIVITY
	}

	public enum Type {
		UNDEFINED_WEB_SERVER_PROBLEM(R.string.eventlog_UNDEFINED_WEB_SERVER_PROBLEM, Origin.WEB_SERVER),
		UNDEFINED_MAIN_ACTIVITY_PROBLEM(R.string.eventlog_UNDEFINED_WEB_SERVER_PROBLEM, Origin.WEB_SERVER),

		SERVER_START_REQUESTED(R.string.eventlog_SERVER_START_REQUESTED, Origin.MAIN_ACTIVITY),
		SERVER_STARTED(R.string.eventlog_SERVER_STARTED, Origin.MAIN_ACTIVITY),
		SERVER_START_FAILED(R.string.eventlog_SERVER_START_FAILED, Origin.MAIN_ACTIVITY),
		SERVER_STOP_REQUESTED(R.string.eventlog_SERVER_STOP_REQUESTED, Origin.MAIN_ACTIVITY),
		SERVER_STOPPED(R.string.eventlog_SERVER_STOPPED, Origin.MAIN_ACTIVITY),
		SERVER_STOP_FAILED(R.string.eventlog_SERVER_STOP_FAILED, Origin.MAIN_ACTIVITY),

		WEBUSER_REQUESTED_UNKOWN_RESOURCE(R.string.eventlog_WEBUSER_REQUESTED_UNKOWN_RESOURCE, Origin.WEB_SERVER),
		WEBUSER_REQUESTED_FORBIDEN_RESOURCE(R.string.eventlog_WEBUSER_REQUESTED_FORBIDEN_RESOURCE, Origin.WEB_SERVER),
		WEBUSER_DOWNLOAD_STARTED(R.string.eventlog_WEBUSER_DOWNLOAD_STARTED, Origin.WEB_SERVER),
		WEBUSER_DOWNLOAD_COMPLETED(R.string.eventlog_WEBUSER_DOWNLOAD_COMPLETED, Origin.WEB_SERVER),
		WEBUSER_DOWNLOAD_WAS_CANCELLED(R.string.eventlog_WEBUSER_DOWNLOAD_WAS_CANCELLED, Origin.WEB_SERVER),

		USER_ADDED_RESOURCE(R.string.eventlog_USER_ADDED_RESOURCE, Origin.MAIN_ACTIVITY),
		USER_REMOVED_RESOURCE(R.string.eventlog_USER_REMOVED_RESOURCE, Origin.MAIN_ACTIVITY),
		USER_ADD_RESOURCE_DENIED_BECAUSE_DUPLICATED(R.string.eventlog_USER_ADD_RESOURCE_DENIED_BECAUSE_DUPLICATED, Origin.MAIN_ACTIVITY);

		private final int message;

		private final Origin origin;

		Type(int message, Origin origin) {
			this.message = message;
			this.origin = origin;
		}

	}

	private final Date date;

	private final String extras[];

	private final Severity severity;

	private final Type type;

	private final String user;

	public EventLog(Severity severity, Type type, String... extras) {
		this(severity, type, null, null, extras);
	}
	public EventLog(Severity severity, Type type, Date date, String user, String... extras) {
		if (severity == null || type == null || extras == null)
			throw new IllegalArgumentException("Neither 'severity', 'type' nor 'extras' can be null");
		this.severity = severity;
		this.type = type;
		this.date = date == null ? new Date() : date;
		this.user = user;
		this.extras = extras;
	}


	public Date getDate() {
		return date;
	}

	public String[] getExtras() {
		return extras;
	}

	public Severity getSeverity() {
		return severity;
	}

	public Type getType() {
		return type;
	}

	public Origin getOrigin() {
		return type.origin;
	}

	public int getMessage() {
		return type.message;
	}

	public String getUser() {
		return user;
	}
}
