package net.tetrakoopa.canardhttpd.view.action;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
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
import net.tetrakoopa.canardhttpd.domain.common.CommonSharedThing;
import net.tetrakoopa.canardhttpd.domain.common.SharedThing;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedDirectory;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedFile;
import net.tetrakoopa.canardhttpd.domain.sharing.SharedText;
import net.tetrakoopa.canardhttpd.preference.MainActivityPreferencesFragment;
import net.tetrakoopa.canardhttpd.service.http.writer.CommonHTMLComponent;
import net.tetrakoopa.canardhttpd.service.sharing.SharesManager;
import net.tetrakoopa.mdu.util.ExceptionUtil;
import net.tetrakoopa.mdua.util.ContractuelUtil;
import net.tetrakoopa.mdua.util.SystemUtil;
import net.tetrakoopa.mdua.view.util.SystemUIUtil;

import java.util.List;

public class MainAction extends AbstractCommonAction implements ServiceConnection {

	private ImageButton aboutButton;

	private CompoundButton toggleServerButton;
	//private TextView statusTextView;
	private TextView publicInfoTextView;

	private Button logButton;
	private Button activityButton;

	private ImageButton addShareButton;
	private ImageButton addShareGroupButton;


	private ListView sharedFilesList;
	private SharedThingAdapter sharedThingAdapter;
	private ImageButton shareServerButton;
	
	private boolean notifyServerWentUpDown = false;

	private View serverDependentViews[];
	private View serviceDependentViews[];
	private View shareView;
	private View addressAddShareButtonView;
	private View logAndActivityButtonsView;

	private final ServerChangeListener serverChangeListener = new ServerChangeListener();


	public MainAction(CanardHTTPDActivity activity, Bundle savedInstanceState, View viewLayout) {
		super(activity, savedInstanceState, viewLayout);
	}

	@Override
	protected void internalLayout(final View parentView) {

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

		addShareGroupButton = (ImageButton) parentView.findViewById(R.id.action_add_share_group);
		addShareGroupButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View button) {
				MainAction.this.activity().showPicker();
			}
		});

		logButton = (Button) parentView.findViewById(R.id.action_show_log);


		serverDependentViews = new View[] { shareServerButton, activityButton };
		serviceDependentViews = new View[] { shareView };

		addressAddShareButtonView = parentView.findViewById(R.id.view_address_add_share_button);
		logAndActivityButtonsView = parentView.findViewById(R.id.view_log_and_activity_buttons);
		PreferenceManager.getDefaultSharedPreferences(activity()).registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
				if (MainActivityPreferencesFragment.SHOW_LOG_AND_ACTIVITY_BUTTONS.equals(key)) {
					updateLogAndActivityButtonsView(sharedPreferences);
					return;
				}
				if (MainActivityPreferencesFragment.SHOW_ADDRESS_AND_SHARE_BUTTON.equals(key)) {
					updateAdressAddShareButtonView(sharedPreferences);
					return;
				}
			}
		});

		updateUI(PreferenceManager.getDefaultSharedPreferences(activity()));
	}

	private final static void enableViews(View[] views, boolean enabled) {
		for (View view : views)
			view.setEnabled(enabled);
	}
	private void updateUI(SharedPreferences sharedPreferences) {
		updateLogAndActivityButtonsView(sharedPreferences);
		updateAdressAddShareButtonView(sharedPreferences);
	}


	private void updateUI(ServerStatus status) {
		switch (status) {
			case STARTING:
				toggleServerButton.setEnabled(false);
				//statusTextView.setText(message(R.string.domain_server_status_starting));
				publicInfoTextView.setText(message(R.string.domain_server_status_running));
				enableViews(serverDependentViews, false);
				break;
			case UP:
				toggleServerButton.setEnabled(true);
				toggleServerButton.setChecked(true);
				//toggleServerButton.setText(R.string.BUTTON_server_stop);
				//statusTextView.setText(message(R.string.domain_server_status_running));
				final String serverInfo = getServerIndexURL();
				publicInfoTextView.setText(serverInfo);
				enableViews(serverDependentViews, true);
				break;
			case STOPING:
				toggleServerButton.setEnabled(false);
				//statusTextView.setText(message(R.string.domain_server_status_halting));
				publicInfoTextView.setText("");
				enableViews(serverDependentViews, false);
				break;
			case DOWN:
				toggleServerButton.setEnabled(true);
				toggleServerButton.setChecked(false);
				//toggleServerButton.setText(R.string.BUTTON_server_start);
				//statusTextView.setText(message(R.string.domain_server_status_stopped));
				publicInfoTextView.setText("");
				enableViews(serverDependentViews, false);
				break;
			default:
				publicInfoTextView.setText("");
				//statusTextView.setText(message(R.string.domain_server_status_unknown));
				enableViews(serverDependentViews, false);
		}
	}

	public void setMenu(Menu menu) {
		MenuItem item = menu.findItem(R.id.menu_server_toggle);
		item.setActionView(R.layout.main_switch);
		View view = item.getActionView();
		toggleServerButton = (CompoundButton)view.findViewById(R.id.action_server_toogle_menu);
		toggleServerButton.setOnCheckedChangeListener(serverSwitchListener);
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
				if (ex instanceof java.net.BindException) {
					messageExplain = message(R.string.error_explain__could_not_bind);
				} else {
					messageExplain = message(R.string.error_explain__error_unknown_server_error);
				}
				final String msg = messageExplain + "\n\n" + ExceptionUtil.getMessages(ex, true);
				SystemUIUtil.showOKDialog(MainAction.this.activity(), message(triggeringAction == null ? R.string.error_failed_server_unknown_operation : getActionTriggerMessageResource(triggeringAction)), msg);
			}

			updateUI(status);

			switch (status) {
			case STARTING:
				break;
			case UP:
				if (notifyServerWentUpDown) {
					final String serverInfo = getServerIndexURL();
					Toast.makeText(MainAction.this.activity(), message(R.string.notification_server_running_on) + " " + serverInfo, Toast.LENGTH_LONG).show();
				}
				break;
			case STOPING:
				break;
			case DOWN:
				if (notifyServerWentUpDown) {
					Toast.makeText(MainAction.this.activity(), message(R.string.notification_server_stopped), Toast.LENGTH_LONG).show();
				}
				break;
			default:
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
		throw new IllegalArgumentException("No such ServerStatusChangeListener.ActionTrigger'"+trigger+"'");
	}

	public class SharedThingAdapter extends ArrayAdapter<SharedThing> {

		public SharedThingAdapter(int textViewResourceId, List<SharedThing> elements) {
			super(activity, textViewResourceId, elements);
		}
		
		private OnClickListener deleteButtonOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View element) {
            final CommonSharedThing thing = (CommonSharedThing) element.getTag();

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
            MainAction.this.updateSharedThingsList();
            //Toast.makeText(MainAction.this.activity(), "Removed " + thing.getName(), Toast.LENGTH_SHORT).show();

			}
		};
		
		private OnClickListener shareButtonOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View element) {
            final CommonSharedThing thing = (CommonSharedThing) element.getTag();

            if (thing == null) {
                CanardHTTPDActivity.quickLogAndShowInternalError(element.getContext(), "Oops, tried to share a null element");
                return;
            }

            final String thingName = CommonHTMLComponent.escapeToUrl(thing.getName());

            if (thing instanceof SharedText) {
                SystemUtil.shareText(element.getContext(), message(R.string.title_share_sharedtext_url) + " : " + thing.getName(), MainAction.this.getServerIndexURL() + thingName);
                return;
            }

            if (thing instanceof SharedFile) {
                SystemUtil.shareText(element.getContext(), message(R.string.title_share_sharedfile_url) + " : " + thing.getName(), MainAction.this.getServerIndexURL() + thingName);
                return;
            }
            if (thing instanceof SharedDirectory) {
                SystemUtil.shareText(element.getContext(), message(R.string.title_share_sharedfolder_url) + " : " + thing.getName(), MainAction.this.getServerIndexURL() + thingName);
                return;
            }

            CanardHTTPDActivity.quickLogAndShowInternalError(element.getContext(), "Unable to handle share of " + thing.getClass().getName());
			}
		};

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final SharesManager filesManager = sharesManager();
			final SharedThing thing = filesManager.getThings().get(position);

            final boolean needInit = convertView == null;
			if (needInit) {
				LayoutInflater vi = activity.getLayoutInflater();
				convertView = vi.inflate(R.layout.shared_thing_listitem, null);
			}

            Button deleteButton = (Button) convertView.findViewById(R.id.sharedthing_action_remove);
            deleteButton.setTag(thing);

            Button shareButton = (Button) convertView.findViewById(R.id.action_share_sharedthing_info);
            shareButton.setTag(thing);

            TextView name = (TextView) convertView.findViewById(R.id.sharedthing_name);
            TextView shareDate = (TextView) convertView.findViewById(R.id.sharedthing_shareDate);
            ImageView icon = (ImageView) convertView.findViewById(R.id.sharedthing_icon);
            TextView mime = (TextView) convertView.findViewById(R.id.sharedthing_mime);
            TextView users = (TextView) convertView.findViewById(R.id.sharedthing_users);

            if (needInit) {
                deleteButton.setClickable(true);
                deleteButton.setOnClickListener(deleteButtonOnClickListener);
                shareButton.setClickable(true);
                shareButton.setOnClickListener(shareButtonOnClickListener);
            }

            name.setText(thing.getName());

			shareDate.setText(thing.getShareDate() == null ? message(R.string.generic_unknown) : formater.getDateFormat().format(thing.getShareDate()));
			if (thing instanceof SharedFile) {
				mime.setText(((SharedFile) thing).getMimeType());
			} else {
				mime.setText("");
			}
			icon.setImageResource(android.R.drawable.ic_menu_view);

			return convertView;
		}
	}

	public void updateSharedThingsList() {
		sharedThingAdapter.notifyDataSetChanged(); // or notifyDataSetInvalidated
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {

		sharedThingAdapter = new SharedThingAdapter(R.layout.shared_thing_listitem, sharesManager().getThings());
		sharedFilesList.setAdapter(sharedThingAdapter);

		serverChangeListener.onServerStatusChange(service(), ActionTrigger.STATUS, service().isServerSarted() ? ServerStatus.UP : ServerStatus.DOWN, null);

		//ShareFeedUtil.addFakeObjectsToManager(sharesManager());
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		Toast.makeText(activity, "Unexpectedly disconnected from Service !", Toast.LENGTH_LONG).show();
		serverChangeListener.onServerStatusChange(MainAction.this.service(), ActionTrigger.DISCONNECTION, ServerStatus.DOWN, null);
	}

	private void updateAdressAddShareButtonView(boolean visible) {
		addressAddShareButtonView.setVisibility(visible ? View.VISIBLE : View.GONE);
	}

	public String getServerIndexURL() {
		final String ip = service().getExternalIp();
		return "http://" + (ip==null?"0.0.0.0":ip) + ":" + MainAction.this.service().getPort() + "/";
	}


	public void invalidateListe() {
		sharedThingAdapter.notifyDataSetInvalidated();
	}

	private CompoundButton.OnCheckedChangeListener serverSwitchListener = new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			service().start(MainAction.this.activityIntent(), activity(), serverChangeListener);
		} else {
			service().stop(serverChangeListener);
		}
		}
	};

	private void updateLogAndActivityButtonsView(SharedPreferences sharedPreferences) {
		final boolean show = sharedPreferences.getBoolean(MainActivityPreferencesFragment.SHOW_LOG_AND_ACTIVITY_BUTTONS, MainActivityPreferencesFragment.DEFAULT_SHOW_LOG_AND_ACTIVITY_BUTTONS);
		logAndActivityButtonsView.setVisibility(show ? View.VISIBLE : View.GONE);
	}
	private void updateAdressAddShareButtonView(SharedPreferences sharedPreferences) {
		final boolean show = sharedPreferences.getBoolean(MainActivityPreferencesFragment.SHOW_ADDRESS_AND_SHARE_BUTTON, MainActivityPreferencesFragment.DEFAULT_SHOW_ADRESS_AND_SHARE_BUTTON);
		addressAddShareButtonView.setVisibility(show ? View.VISIBLE : View.GONE);
	}
}
