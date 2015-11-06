package net.tetrakoopa.canardhttpd.view.action;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.tetrakoopa.canardhttpd.CanardHTTPDActivity;
import net.tetrakoopa.canardhttpd.CanardHTTPDService;
import net.tetrakoopa.canardhttpd.CanardHTTPDService.ServerStatus;
import net.tetrakoopa.canardhttpd.CanardHTTPDService.ServerStatusChangeListener;
import net.tetrakoopa.canardhttpd.CanardHTTPDService.ServerStatusChangeListener.ActionTrigger;
import net.tetrakoopa.canardhttpd.LogActivity;
import net.tetrakoopa.canardhttpd.R;
import net.tetrakoopa.canardhttpd.SettingsActivity;
import net.tetrakoopa.canardhttpd.domain.common.CommonSharedThing;
import net.tetrakoopa.canardhttpd.domain.common.SharedThing;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedDirectory;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedText;
import net.tetrakoopa.canardhttpd.service.sharing.SharesManager;
import net.tetrakoopa.canardhttpd.util.ShareFeedUtil;
import net.tetrakoopa.mdu.android.util.ContractuelUtil;
import net.tetrakoopa.mdu.android.util.NetworkUtil;
import net.tetrakoopa.mdu.android.util.SystemUtil;
import net.tetrakoopa.mdu.android.view.util.SystemUIUtil;

import java.net.SocketException;
import java.util.List;

public class MainAction extends AbstractCommonAction implements ServiceConnection {

	private ImageButton aboutButton;

	private Button toggleServerButton;
	private TextView statusTextView;
	private TextView publicInfoTextView;

	private Button logButton;
	private Button activityButton;

	private ImageButton addShareButton;

	private ListView sharedFilesList;
	private SharedThingAdapter sharedThingAdapter;
	private ImageButton shareServerButton;
	
	private boolean notifyServerWentUpDown = false;

	private View serverDependentViews[];
	private View serviceDependentViews[];
	private View shareView;
	
	private final ServerChangeListener serverChangeListener = new ServerChangeListener();


	public MainAction(CanardHTTPDActivity activity, Bundle savedInstanceState, View viewLayout) {
		super(activity, savedInstanceState, viewLayout);
	}

	@Override
	protected void internalLayout(View parentView) {

		statusTextView = (TextView) parentView.findViewById(R.id.server_status);
		publicInfoTextView = (TextView) parentView.findViewById(R.id.server_public_info);

		aboutButton = (ImageButton) parentView.findViewById(R.id.about);
		aboutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				ContractuelUtil.show(activity, "legal/CanardHTTPD-about.html", R.string.app_name, android.R.string.ok);
			}
		});

		shareServerButton = (ImageButton) parentView.findViewById(R.id.action_share_server_info);
		shareServerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SystemUtil.shareText(MainAction.this.activity(), MainAction.this.message(R.string.title_share_server_info), MainAction.this.getServerIndexURL());
			}
		});


		toggleServerButton = (Button) parentView.findViewById(R.id.action_server_toogle);
		toggleServerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (service().isServerSarted()) {
					service().stop(serverChangeListener);
				}
				else {
					service().start(MainAction.this.activityIntent(), activity(),  serverChangeListener);
				}
			}
		});

		shareView = parentView.findViewById(R.id.share);

		sharedFilesList = (ListView) parentView.findViewById(R.id.shared_files);
		sharedFilesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int index, long arg3) {
				// SharedThing sharedThing = canardHTTPDActivity.getFilesManager().getSharedThings().get((int) sharedFilesList.getSelectedItemId());
				Toast.makeText(MainAction.this.activity(), view.getClass().getName(), Toast.LENGTH_LONG).show();

				// Intent detailIntent = new Intent(MainAction.this.canardHTTPDActivity, SharedThingActivity.class);
				// detailIntent.putExtra(SharedThingActivity.ARG_SHARED_THING, );
				// MainAction.this.canardHTTPDActivity.startActivity(detailIntent);
			}
		});

		activityButton = (Button) parentView.findViewById(R.id.action_show_transfert);
		activityButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View button) {
			}
		});
		logButton = (Button) parentView.findViewById(R.id.action_show_log);
		logButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View button) {
				activity.startActivity(new Intent(MainAction.this.activity, LogActivity.class), savedInstanceState);
			}
		});

		addShareButton = (ImageButton) parentView.findViewById(R.id.action_add_share);
		addShareButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View button) {
				MainAction.this.activity().showPicker();
			}
		});

		logButton = (Button) parentView.findViewById(R.id.action_show_log);


		serverDependentViews = new View[] { shareServerButton, activityButton, sharedFilesList };
		serviceDependentViews = new View[] { shareView };
	}

	private final static void enableViews(View[] views, boolean enabled) {
		for (View view : views)
			view.setEnabled(enabled);
	}

	public class ServerChangeListener implements ServerStatusChangeListener {


		@Override
		public void onServerStatusChange(CanardHTTPDService service, ActionTrigger triggeringAction, ServerStatus status, Throwable ex) {

			if (status == null) {
				CanardHTTPDActivity.quickLogAndShowInternalError(MainAction.this.activity(), "CanardHTTPD has a <null> status");
				return;
			}
			
			if (ex != null) {
				final String messageExplain;
				final String exClassName = ex.getClass().getName();
				if (ex instanceof java.net.BindException) {
					messageExplain = message(R.string.error_explain__could_not_bind);
				} else {
					messageExplain = message(R.string.error_explain__error_unknown_server_error);
				}
				String message = messageExplain + "\n\n" + exClassName + "\n" + ex.getLocalizedMessage();
				SystemUIUtil.showOKDialog(MainAction.this.activity(), message(triggeringAction == null ? R.string.error_failed_server_unknown_operation : getActionTriggerMessageResource(triggeringAction)), message);
			}

			switch (status) {
			case STARTING:
				toggleServerButton.setEnabled(false);
				statusTextView.setText(message(R.string.domain_server_status_starting));
				publicInfoTextView.setText(message(R.string.domain_server_status_running));
				enableViews(serverDependentViews, false);
				break;
			case UP:
				toggleServerButton.setEnabled(true);
				toggleServerButton.setText(R.string.BUTTON_server_stop);
				statusTextView.setText(message(R.string.domain_server_status_running));
				String serverInfo = getServerIndexURL();
				publicInfoTextView.setText(serverInfo);
				enableViews(serverDependentViews, true);
				if (notifyServerWentUpDown) {
					Toast.makeText(MainAction.this.activity(), message(R.string.notification_server_running_on) + " " + serverInfo, Toast.LENGTH_LONG).show();
				}
				break;
			case STOPING:
				toggleServerButton.setEnabled(false);
				statusTextView.setText(message(R.string.domain_server_status_halting));
				publicInfoTextView.setText("");
				enableViews(serverDependentViews, false);
				break;
			case DOWN:
				toggleServerButton.setEnabled(true);
				toggleServerButton.setText(R.string.BUTTON_server_start);
				statusTextView.setText(message(R.string.domain_server_status_stopped));
				publicInfoTextView.setText("");
				enableViews(serverDependentViews, false);
				if (notifyServerWentUpDown) {
					Toast.makeText(MainAction.this.activity(), message(R.string.notification_server_stopped), Toast.LENGTH_LONG).show();
				}
				break;
			default:
				publicInfoTextView.setText("");
				statusTextView.setText(message(R.string.domain_server_status_unknown));
				enableViews(serverDependentViews, false);
				Log.e(CanardHTTPDActivity.TAG, "CanardHTTPD has an unknown status : " + status.name() + "!!\nCan you believe it?");
			}
		}
	}

	private int getActionTriggerMessageResource(ServerStatusChangeListener.ActionTrigger trigger) {
		switch (trigger) {
			case START : return R.string.error_failed_server_start;
			case STOP : return R.string.error_failed_server_stop;
			case STATUS : return R.string.error_failed_server_status;
			case DISCONNECTION : return R.string.error_server_disconnection;
		}
		throw new IllegalArgumentException("No nuch ServerStatusChangeListener.ActionTrigger'"+trigger+"'");
	}

	public class SharedThingAdapter extends ArrayAdapter<SharedThing> {

		public SharedThingAdapter(int textViewResourceId, List<SharedThing> elements) {
			super(activity, textViewResourceId, elements);
		}
		
		private OnClickListener deleteButtonOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View element) {
				CommonSharedThing thing = (CommonSharedThing) element.getTag();

				if (thing == null) {
					CanardHTTPDActivity.quickLogAndShowInternalError(element.getContext(), "Oops, tried to delete a null element");
					return;
				}

				try {
					MainAction.this.sharesManager().remove(thing);
				} catch (/* NotShared */Exception ex) {
					CanardHTTPDActivity.quickLogAndShowInternalError(element.getContext(), "Failed to remove : " + ex.getClass().getName(), ex);
					return;
				}
				SharedThingAdapter.this.notifyDataSetChanged(); // or notifyDataSetInvalidated
				Toast.makeText(MainAction.this.activity(), "Removed " + thing.getName(), Toast.LENGTH_SHORT).show();

			}
		};
		
		private OnClickListener shareButtonOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View element) {
				CommonSharedThing thing = (CommonSharedThing) element.getTag();

				if (thing == null) {
					CanardHTTPDActivity.quickLogAndShowInternalError(element.getContext(), "Oops, tried to share a null element");
					return;
				}

				if (thing instanceof SharedText) {
					SystemUtil.shareText(element.getContext(), message(R.string.title_share_sharedtext_url) + " : " + thing.getName(), MainAction.this.getServerIndexURL() + thing.getName());
					return;
				}

				if (thing instanceof SharedFile) {
					SystemUtil.shareText(element.getContext(), message(R.string.title_share_sharedfile_url) + " : " + thing.getName(), MainAction.this.getServerIndexURL() + thing.getName());
					return;
				}
				if (thing instanceof SharedDirectory) {
					SystemUtil.shareText(element.getContext(), message(R.string.title_share_sharedfolder_url) + " : " + thing.getName(), MainAction.this.getServerIndexURL() + thing.getName());
					return;
				}

				CanardHTTPDActivity.quickLogAndShowInternalError(element.getContext(), "Unable to handle share of " + thing.getClass().getName());
			}
		};

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final SharesManager filesManager = sharesManager();
			final SharedThing thing = filesManager.getThings().get(position);
			
			if (convertView == null) {
				LayoutInflater vi = activity.getLayoutInflater();
				convertView = vi.inflate(R.layout.shared_thing_listitem, null);

				Button deleteButton = (Button) convertView.findViewById(R.id.sharedthing_action_remove);
				deleteButton.setTag(thing);
				deleteButton.setClickable(true);
				deleteButton.setOnClickListener(deleteButtonOnClickListener);

				Button shareButton = (Button) convertView.findViewById(R.id.action_share_sharedthing_info);
				shareButton.setTag(thing);
				shareButton.setClickable(true);
				shareButton.setOnClickListener(shareButtonOnClickListener);
			}

			if (filesManager != null) {

				TextView name = (TextView) convertView.findViewById(R.id.sharedthing_name);
				TextView shareDate = (TextView) convertView.findViewById(R.id.sharedthing_shareDate);
				ImageView icon = (ImageView) convertView.findViewById(R.id.sharedthing_icon);
				TextView mime = (TextView) convertView.findViewById(R.id.sharedthing_mime);
				TextView users = (TextView) convertView.findViewById(R.id.sharedthing_users);

				DateFormat.is24HourFormat(MainAction.this.activity());


				name.setText(thing.getName());
				
				shareDate.setText(thing.getShareDate() == null ? message(R.string.generic_unknown) : formater.getDateFormat().format(thing.getShareDate()));
				if (thing instanceof SharedFile) {
					mime.setText(((SharedFile) thing).getMimeType());
				} else {
					mime.setText("");
				}
				icon.setImageResource(android.R.drawable.ic_menu_view);
			}

			return convertView;
		}
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {

		sharedThingAdapter = new SharedThingAdapter(R.layout.shared_thing_listitem, sharesManager().getThings());
		sharedFilesList.setAdapter(sharedThingAdapter);

		serverChangeListener.onServerStatusChange(service(), ActionTrigger.STATUS, service().isServerSarted() ? ServerStatus.UP : ServerStatus.DOWN, null);

		ShareFeedUtil.addFakeObjectsToManager(MainAction.this.sharesManager());
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		serverChangeListener.onServerStatusChange(MainAction.this.service(), ActionTrigger.DISCONNECTION, ServerStatus.DOWN, null);
	}

	protected String getServerIndexURL() {
		return "http://" + findOutServerIP() + ":" + MainAction.this.service().getPort() + "/";
	}

	public static String findOutServerIP() {
		String ip = null;
		try {
			ip = NetworkUtil.getIPAddressFromExternalProviders(NetworkUtil.SOME_IP_PROVIDERS);
			if (ip != null)
				return ip;
		} catch (Exception ex) {
			// Toast.makeText(this.canardHTTPDActivity, "Failed to retreive server IP : " + ex.getClass().getName() + " : " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			Log.w(CanardHTTPDActivity.TAG, "Failed to retreive IP using external provider : "+ex.getMessage());
		}
		try {
			ip = NetworkUtil.getIPAddress(true);
			if (ip != null)
				return ip;
		} catch (SocketException e) {
			Log.w(CanardHTTPDActivity.TAG, "Failed to retreive IP");
			// Toast.makeText(this.canardHTTPDActivity, "Failed to retreive server IP : " + ex.getClass().getName() + " : " + ex.getLocalizedMessage(), Toast.LENGTH_LONG).show();
		}
		return "0.0.0.0";
	}

	public void invalidateListe() {
		sharedThingAdapter.notifyDataSetInvalidated();
	}

}
