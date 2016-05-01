package net.tetrakoopa.canardhttpd;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.tetrakoopa.canardhttpd.domain.EventLog;
import net.tetrakoopa.mdua.util.ResourcesUtil;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class LogActivity extends AppCompatActivity {

	public final static String MANIFEST_ACTIVITY = CanardHTTPDActivity.MANIFEST_PACKAGE+".LogActivity";

	private ListView eventsLogList;
	private ImageButton clearButton;

	private DateFormat dateFormat;

	public class EventLogAdapter extends ArrayAdapter<EventLog> {

		private final List<EventLog> events;

		public EventLogAdapter(List<EventLog> events) {
			super(LogActivity.this, R.layout.event_log_listitem, events);
			this.events = events;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			final EventLog event = events.get(position);

			final boolean needInit = convertView == null;
			if (needInit) {
				LayoutInflater vi = LogActivity.this.getLayoutInflater();
				convertView = vi.inflate(R.layout.event_log_listitem, null);
			}

			ImageView severity = (ImageView) convertView.findViewById(R.id.eventlog_severity);
			TextView name = (TextView) convertView.findViewById(R.id.eventlog_text);
			TextView date = (TextView) convertView.findViewById(R.id.eventlog_date);
			TextView user = (TextView) convertView.findViewById(R.id.eventlog_user);


			if (event.getSeverity() == EventLog.Severity.ERROR)
				severity.setImageResource(R.mipmap.severity_error);
			else if (event.getSeverity() == EventLog.Severity.WARN)
				severity.setImageResource(R.mipmap.severity_warn);
			else if (event.getSeverity() == EventLog.Severity.INFO)
				severity.setImageResource(R.mipmap.severity_info);
			severity.setScaleX(0.5f);
			severity.setScaleY(0.5f);


			final String messageFmt = message(event.getMessage());
			name.setText(messageFmt);

			date.setText(dateFormat.format(event.getDate()));
			//date.setText(DateUtils.getRelativeDateTimeString(LogActivity.this, event.getDate().getTime(), 0, 0, DateUtils.FORMAT_SHOW_WEEKDAY));

			if (event.getUser()!=null)
				user.setText(event.getUser());

			return convertView;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		final ActionBar actionBar = getActionBar();
		if (actionBar!=null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}

		dateFormat = android.text.format.DateFormat.getTimeFormat(this);
		// message(R.string.eventlog_date_format);

		clearButton = (ImageButton) findViewById(R.id.action_clear_log);
		eventsLogList = (ListView) findViewById(R.id.events_log);


		List<EventLog> events = new ArrayList<>();
		addFakeLogEvents(events, 5);

		final EventLogAdapter eventLogAdapter = new EventLogAdapter(events);
		eventsLogList.setAdapter(eventLogAdapter);

		//View mainView = findViewById(R.id.main);




		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				eventLogAdapter.events.clear();
				eventLogAdapter.notifyDataSetChanged();
			}
		});

		eventLogAdapter.notifyDataSetChanged();
	}

	private void addFakeLogEvents(List<EventLog> events, int times) {
		int i;
		final Calendar date = new GregorianCalendar();
		date.roll(Calendar.DAY_OF_MONTH, -1);
		//date.setTime(date.getTime()-(1000*3600*24*2));

		createFakeLogEvent(events, date, 0, EventLog.Severity.INFO, EventLog.Type.SERVER_START_REQUESTED);
		createFakeLogEvent(events, date, 1, EventLog.Severity.INFO, EventLog.Type.SERVER_START_FAILED);
		createFakeLogEvent(events, date, 5, EventLog.Severity.INFO, EventLog.Type.SERVER_START_REQUESTED);
		createFakeLogEvent(events, date, 4, EventLog.Severity.INFO, EventLog.Type.SERVER_STARTED);
		for (i=0; i<times; i++) {
			createFakeLogEvent(events, date, "0.0.0.0", 100, EventLog.Severity.ERROR, EventLog.Type.WEBUSER_REQUESTED_UNKOWN_RESOURCE, "bqsazed");
			createFakeLogEvent(events, date, 14, EventLog.Severity.WARN, EventLog.Type.USER_ADD_RESOURCE_DENIED_BECAUSE_DUPLICATED, "cat_pic.jpeg");
			createFakeLogEvent(events, date, "0.0.0.0", 203, EventLog.Severity.INFO, EventLog.Type.WEBUSER_DOWNLOAD_STARTED, "good_music.ogg");
			createFakeLogEvent(events, date, "0.0.0.0", 200, EventLog.Severity.WARN, EventLog.Type.WEBUSER_REQUESTED_FORBIDEN_RESOURCE, "secret.txt");
			createFakeLogEvent(events, date, 124, EventLog.Severity.WARN, EventLog.Type.USER_ADD_RESOURCE_DENIED_BECAUSE_DUPLICATED, "cat_pic.jpeg");
			createFakeLogEvent(events, date, "0.0.0.0", 70, EventLog.Severity.INFO, EventLog.Type.WEBUSER_DOWNLOAD_COMPLETED, "good_music.ogg");
		}
		createFakeLogEvent(events, date, 40, EventLog.Severity.INFO, EventLog.Type.SERVER_STOP_REQUESTED);
		createFakeLogEvent(events, date, 1, EventLog.Severity.INFO, EventLog.Type.SERVER_STOPPED);
	}
	private void createFakeLogEvent(List<EventLog> events, Calendar date, int secondesLater, EventLog.Severity severity, EventLog.Type type, String ... extras) {
		createFakeLogEvent(events, date, null, secondesLater, severity, type);
	}
	private void createFakeLogEvent(List<EventLog> events, Calendar date, String user, int secondesLater, EventLog.Severity severity, EventLog.Type type, String ... extras) {
		//date.setTime(date.getTime()+(1000*secondesLater));
		date.roll(Calendar.SECOND, secondesLater);
		events.add(new EventLog(severity, type, date.getTime(), user, extras));
	}

	private String message(int id) {
		return ResourcesUtil.getString(this, id);
	}

}
