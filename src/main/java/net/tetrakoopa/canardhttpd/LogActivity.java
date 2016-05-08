package net.tetrakoopa.canardhttpd;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import net.tetrakoopa.canardhttpd.domain.EventLog;
import net.tetrakoopa.canardhttpd.service.CanardLogger;
import net.tetrakoopa.canardhttpd.util.TestFactoryUtil;
import net.tetrakoopa.mdu.util.ExceptionUtil;
import net.tetrakoopa.mdua.util.NetworkUtil;
import net.tetrakoopa.mdua.util.ResourcesUtil;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class LogActivity extends AppCompatActivity {

	private static final String TAG = CanardHTTPDActivity.TAG+".Logger";

	private ListView eventsLogList;
	private EventLogAdapter eventLogAdapter;
	private final List<EventLog> events = new ArrayList<>();

	private TailLogTask tailLogTask;

	public class SeveritySpinnerAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return EventLog.Severity.values().length;
		}

		@Override
		public Object getItem(int position) {
			return EventLog.Severity.values()[position];
		}

		@Override
		public long getItemId(int position) {
			return (long)position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {

			if (view == null) {
				LayoutInflater vi = LogActivity.this.getLayoutInflater();
				view = vi.inflate(R.layout.severity_spinner_item, null);
			}

			final ImageView icon = (ImageView) view.findViewById(R.id.icon);

			final EventLog.Severity severity = EventLog.Severity.values()[position];
			if (severity == EventLog.Severity.ERROR)
				icon.setImageResource(R.mipmap.severity_error);
			else if (severity == EventLog.Severity.WARN)
				icon.setImageResource(R.mipmap.severity_warn);
			else if (severity == EventLog.Severity.INFO)
				icon.setImageResource(R.mipmap.severity_info);

			return view;
		}
		@Override
		public View getDropDownView(int position, View view, ViewGroup parent) {

			if (view == null) {
				LayoutInflater vi = LogActivity.this.getLayoutInflater();
				view = vi.inflate(R.layout.severity_spinner_item_dd, null);
			}

			final ImageView icon = (ImageView) view.findViewById(R.id.icon);
			final TextView label = (TextView) view.findViewById(R.id.label);

			final EventLog.Severity severity = EventLog.Severity.values()[position];
			if (severity == EventLog.Severity.ERROR)
				icon.setImageResource(R.mipmap.severity_error);
			else if (severity == EventLog.Severity.WARN)
				icon.setImageResource(R.mipmap.severity_warn);
			else if (severity == EventLog.Severity.INFO)
				icon.setImageResource(R.mipmap.severity_info);


			if (severity == EventLog.Severity.ERROR)
				label.setText(R.string.domain_message_severity_error);
			else if (severity == EventLog.Severity.WARN)
				label.setText(R.string.domain_message_severity_warning);
			else if (severity == EventLog.Severity.INFO)
				label.setText(R.string.domain_message_severity_information);

			return view;
		}
	}

	public class EventLogAdapter extends ArrayAdapter<EventLog> implements Filterable {

		private final List<EventLog> events;
		private final List<EventLog> allEvents;

		private Spinner logSeverityFilter;

		public EventLogAdapter(List<EventLog> events) {
			super(LogActivity.this, R.layout.event_log_listitem, events);
			this.events = events;
			this.allEvents = new ArrayList<>(events);
		}

		public Filter getFilter() {
			return new Filter() {

				@SuppressWarnings("unchecked")
				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {
					notifyDataSetChanged();
				}

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {

					final FilterResults results = new FilterResults();
					events.clear();

					for (EventLog event : allEvents) {
						if (event.getSeverity().ordinal()>= EventLog.Severity.WARN.ordinal())  {
							events.add(event);
						}
					}

					results.count = events.size();
					results.values = events;

					return results;
				}
			};

		}

		@Override
		public View getView(final int position, View view, ViewGroup parent) {

			try {
				final EventLog event = events.get(position);

				final boolean needInit = view == null;
				if (needInit) {
					LayoutInflater vi = LogActivity.this.getLayoutInflater();
					view = vi.inflate(R.layout.event_log_listitem, null);
				}

				final ImageView severity = (ImageView) view.findViewById(R.id.eventlog_severity);
				final TextView name = (TextView) view.findViewById(R.id.eventlog_text);
				final TextView date = (TextView) view.findViewById(R.id.eventlog_date);
				final TextView user = (TextView) view.findViewById(R.id.eventlog_user);


				if (event.getSeverity() == EventLog.Severity.ERROR)
					severity.setImageResource(R.mipmap.severity_error);
				else if (event.getSeverity() == EventLog.Severity.WARN)
					severity.setImageResource(R.mipmap.severity_warn);
				else if (event.getSeverity() == EventLog.Severity.INFO)
					severity.setImageResource(R.mipmap.severity_info);
				severity.setScaleX(0.5f);
				severity.setScaleY(0.5f);


				final String messageFmt = message(event.getMessage());
				name.setText(String.format(messageFmt, (Object[])event.getExtras()));

				date.setText(DateUtils.getRelativeDateTimeString(LogActivity.this, event.getDate().getTime(), 0, 0, DateUtils.FORMAT_SHOW_WEEKDAY));

				if (event.getUser() != null)
					user.setText(event.getUser());

			} catch(Exception z) {
				Log.d("LOG", "z = "+z, z);
			}


			return view;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.log, menu);

		final MenuItem severityFilterMenu = menu.findItem(R.id.filter_severity);
		final Spinner logSeverityFilter = (Spinner)MenuItemCompat.getActionView(severityFilterMenu);

		MenuItem clearLogMenu = menu.findItem(R.id.action_menu_clear_log);

		clearLogMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menu) {
				eventLogAdapter.events.clear();
				eventLogAdapter.notifyDataSetChanged();
				return true;
			}
		});


		logSeverityFilter.setAdapter(new SeveritySpinnerAdapter());
		severityFilterMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menu) {
				eventLogAdapter.notifyDataSetChanged();
				return true;
			}
		});

		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		final ActionBar actionBar = getActionBar();
		if (actionBar!=null)
			actionBar.setDisplayHomeAsUpEnabled(true);

		//dateFormat = android.text.format.DateFormat.getTimeFormat(this);
		eventLogAdapter = new EventLogAdapter(events);

		eventsLogList = (ListView) findViewById(R.id.events_log);

		eventsLogList.setAdapter(eventLogAdapter);

/*
		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				eventLogAdapter.events.clear();
				eventLogAdapter.notifyDataSetChanged();
			}
		});
*/

		//TestFactoryUtil.addFakeLogEvents(events, 5);

		eventLogAdapter.notifyDataSetChanged();
	}


	private final static String newEventsLock = new String("TailLogTask-Event-Lock");

	public class TailLogTask extends AsyncTask<File, Integer, Void> {

		private Tailer tailer;

		private final List<EventLog> newEvents = new ArrayList<>();

		private void pushEvent(EventLog event) {
			synchronized (newEventsLock) {
				newEvents.add(event);
			}
		}
		private EventLog[] popEvent() {
			final EventLog events[];
			synchronized (newEventsLock) {
				events = newEvents.toArray(new EventLog[newEvents.size()]);
				newEvents.clear();
			}
			return events;
		}

		private class LogTailerListener extends TailerListenerAdapter {


			@Override
			public void handle(String line) {
				final EventLog event;
				try {
					final String tokens[] = line.split("\\|");
					final EventLog.Severity severity = EventLog.Severity.valueOf(tokens[0]);
					final EventLog.Type type = EventLog.Type.valueOf(tokens[1]);
					event = new EventLog(severity, type);
				} catch(Exception ex) {
					Log.e(TAG, "malformed log line", ex);
					return;
				}
				pushEvent(event);
				TailLogTask.this.publishProgress(1);
			}
		}

		protected Void doInBackground(File... files) {
			tailer = new Tailer(files[0], new LogTailerListener(), 1000);
			tailer.run();
			return null;
		}

		@Override
		public void onProgressUpdate(Integer... progress) {
			final EventLog newEvents[] = popEvent();
			for (EventLog event : newEvents)
				events.add(event);
			eventLogAdapter.notifyDataSetChanged();
		}

		public void stop() {
			tailer.stop();
		}
	}
	@Override
	public void onResume() {
		super.onResume();

		tailLogTask = new TailLogTask();
		// FIXME FIXME FIXME : disabled for now since activity locks after all events are read
		//tailLogTask.doInBackground(CanardLogger.getLocation(this));
	}
	@Override
	public void onStop() {
		super.onStop();
		tailLogTask.stop();
	}

	private String message(int id) {
		return ResourcesUtil.getString(this, id);
	}

}
