package net.tetrakoopa.canardhttpd;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import net.tetrakoopa.canardhttpd.service.sharing.SharesManager;
import net.tetrakoopa.canardhttpd.util.ShareFeedUtil;
import net.tetrakoopa.canardhttpd.view.action.MainAction;
import net.tetrakoopa.mdua.util.ContractuelUtil;
import net.tetrakoopa.mdua.util.ResourcesUtil;
import net.tetrakoopa.mdua.util.IntentUtil;
import net.tetrakoopa.mdua.view.util.SystemUIUtil;

import java.io.File;


public class CanardHTTPDActivity extends AppCompatActivity {

	public enum UNMET_REQUIREMENT {
		PERMISSION_MISSING_READ_EXTERNAL_STORAGE;
	}

	public final static String MANIFEST_PACKAGE = "net.tetrakoopa.canardhttpd";
	public final static String MANIFEST_ACTIVITY = MANIFEST_PACKAGE+".CanardHTTPDActivity";

	public final static String TAG = "CanardHTTPD";

	private boolean intentParametersNeedToBeChecked;
	private boolean needToCheckPickupActivityReturn;

	private MainAction mainAction;

	MenuItem showActivityMenu;
	MenuItem showLogMenu;


	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final int intStatus = intent.getIntExtra(CanardHTTPDService.MESSAGE_INTENT_SERVER_STATE_CHANGE_VALUE, -1);
			final CanardHTTPDService.ServerStatus status;
			try {
				status = CanardHTTPDService.ServerStatus.fromOrdinal(intStatus);
			} catch (IllegalArgumentException iaex) {
				Log.w(TAG,"Received a illegal  server status : '"+CanardHTTPDService.MESSAGE_INTENT_SERVER_STATE_CHANGE_VALUE+":"+intStatus+"'");
				return;
			}
			updateUI(status);
		}
	};

	private CanardHTTPDService service;
	private boolean uiReady = false;
	private final ServiceExtra serviceExtra = new ServiceExtra() {

	};

	private static class ServiceExtra {
		ComponentName componentName;
		IBinder serviceBinder;
	}

	private Uri uriFromPickupActivityReturn;

	private Bundle savedInstanceState;

    private SharedPreferences dontTellAboutMissingFonctionnalies;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "Create Activity");
		super.onCreate(savedInstanceState);

		this.savedInstanceState = savedInstanceState;

		setContentView(R.layout.activity_canard_httpd);

		final View mainView = findViewById(R.id.main);



		mainAction = new MainAction(this, savedInstanceState, mainView);
		mainAction.doPrepareLayoutAndStuff();


        final ContractuelUtil.PreferenceSavingAndActivityClosingAcceptanceResponse eulaResponseHandler = new ContractuelUtil.PreferenceSavingAndActivityClosingAcceptanceResponse(this, "legal", "eula.accepted");
		ContractuelUtil.showForAcceptanceIfNeeded(this, eulaResponseHandler, "legal/apache-licence-2.0.html", R.string.misc_eula_title, android.R.string.yes, android.R.string.no);

		intentParametersNeedToBeChecked = true;

        dontTellAboutMissingFonctionnalies = getApplicationContext().getSharedPreferences(DONT_TELL_ABOUT_MISSING_FONCTIONNALITIES_PREFERENCES_NAME, Context.MODE_PRIVATE);




		SystemUIUtil.values_R.strings.dont_show_again = R.string.dont_show_again;

	}

	private void copyDefaultCertificatToDir(String path) {
		final File certificatsDirectory = getDir("security/certificate", MODE_PRIVATE );

		//certificatsDirectory.a AssetManager.AssetInputStream qsd;

	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "Destroy Activity");
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.canard_httpd, menu);

		MenuItem item = menu.findItem(R.id.menu_server_toggle);
		item.setActionView(R.layout.main_switch);

		showActivityMenu = menu.findItem(R.id.action_menu_show_activity);
		showLogMenu = menu.findItem(R.id.action_menu_show_activity);

		mainAction.setMenu(menu);

		/*final MenuItem toggleservice = menu.findItem(R.id.toggleservice);
		final Switch actionView = (Switch) toggleservice.getActionView();
		actionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Toast.makeText(CanardHTTPDActivity.this, "Switch : " + isChecked, Toast.LENGTH_SHORT).show();
			}
		});*/

		if (service != null) {
			updateUI(service.isServerSarted() ? CanardHTTPDService.ServerStatus.UP : CanardHTTPDService.ServerStatus.DOWN);
		}
		uiReady = true;

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onStart() {
		Log.d(TAG, "Start Activity");
		super.onStart();

		bindHTTPService();

		LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
				new IntentFilter(CanardHTTPDService.MESSAGE_INTENT_SERVER_STATE_CHANGE)
		);
	}
	@Override
	public void onStop() {
		Log.d(TAG, "stop Activity");
		unbindHTTPService();
		//stopService(intent);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
		super.onStop();
	}

	private void updateUI(CanardHTTPDService.ServerStatus serverStatus) {
		mainAction.onServiceConnected(serviceExtra.componentName, serviceExtra.serviceBinder);
		showActivityMenu.setEnabled(serverStatus == CanardHTTPDService.ServerStatus.UP);
		showLogMenu.setEnabled(serverStatus == CanardHTTPDService.ServerStatus.UP);
	}

	private void bindHTTPService() {
		final Intent serviceIntent = new Intent(this, CanardHTTPDService.class);
		final String action = getIntent().getAction();
		if (Intent.ACTION_SEND.equals(action) || Intent.ACTION_SEND_MULTIPLE.equals(action)) {
			IntentUtil.mimicIntent(getIntent(), serviceIntent);
		}

		Log.d(TAG, "bind HTTPService");
		bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
		Log.d(TAG, "HTTPService binded");
		startService(serviceIntent);
	}
	private void unbindHTTPService() {
		Log.d(TAG, "unbind HTTPService");
		unbindService(connection);
		Log.d(TAG, "HTTPService unbinded");
	}

    private final ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName, IBinder serviceBinder) {

			CanardHTTPDActivity.this.service = ((CanardHTTPDService.LocalBinder) serviceBinder).getService();
			serviceExtra.componentName = componentName;
			serviceExtra.serviceBinder = serviceBinder;

			if (intentParametersNeedToBeChecked) {
				intentParametersNeedToBeChecked = false;
			}
			if (needToCheckPickupActivityReturn) {
				needToCheckPickupActivityReturn = false;
				if (ShareFeedUtil.tryAddFileToSharesElseNotify(CanardHTTPDActivity.this, getService().getSharesManager(), uriFromPickupActivityReturn))
					mainAction.invalidateListe();
			}

			if (uiReady) {
				mainAction.onServiceConnected(componentName, serviceBinder);
				updateUI(CanardHTTPDActivity.this.service.isServerSarted() ? CanardHTTPDService.ServerStatus.UP : CanardHTTPDService.ServerStatus.DOWN);
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mainAction.onServiceDisconnected(componentName);
			CanardHTTPDActivity.this.service = null;
		}
	};

	public SharesManager getFilesManager() {
		return service == null ? null : service.getSharesManager();
	}

	public CanardHTTPDService getService() {
		return service;
	}

	public static void quickLogAndShowInternalError(Context context, String message) {
		quickLogAndShowInternalError(context, message, null);
	}

	public static void quickLogAndShowInternalError(Context context, String message, Exception exception) {
		if (exception != null) {
			message = message + " : " + exception.getClass().getName();
			if (exception.getMessage() != null) {
				message = message + ":" + exception.getMessage();
			}
		}
		Log.e(CanardHTTPDActivity.TAG, message);
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public String message(int id) {
		return ResourcesUtil.getString(this, id);
	}

	private static final int FILE_SELECT_CODE = 6384;

	public void showPicker() {
		final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		// message(R.string.title_pick_file_to_share);
		intent.setType("*/*");
		try {
			startActivityForResult(intent, FILE_SELECT_CODE);
		} catch (ActivityNotFoundException e) {
			SystemUIUtil.showOKDialog(this, "Needed Application", message(R.string.no_activity_for_picking_file_or_directory));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case FILE_SELECT_CODE:
			if (resultCode == RESULT_OK && data != null) {
				try {
					final Uri uri = data.getData();
					if (this.service != null) {
						if (ShareFeedUtil.tryAddFileToSharesElseNotify(this, getService().getSharesManager(), uri)) {
							mainAction.updateSharedThingsList();
						}
					} else {
						needToCheckPickupActivityReturn = true;
						uriFromPickupActivityReturn = uri;
					}
				} catch (Exception e) {
					Log.e(TAG, "Error while tying to pickup a file", e);
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		final int id = item.getItemId();


		if (id==R.id.action_menu_settings) {
			startActivity(new Intent(this, CanardHTTPDPreferenceActivity.class));
			return true;
		}

        if (id==R.id.action_menu_show_activity) {
            startActivity(new Intent(this, MonitoringActivity.class), savedInstanceState);
            return true;
        }
		if (id==R.id.action_menu_show_log) {
			startActivity(new Intent(this, LogActivity.class), savedInstanceState);
			return true;
		}
		if (id==R.id.action_menu_share_url) {
			IntentUtil.shareText(this, R.string.title_share_address, message(R.string.intent_object_share_server_address), mainAction.getServerIndexURL());
			return true;
		}


		return super.onOptionsItemSelected(item);
	}

    public static final String DONT_TELL_ABOUT_MISSING_FONCTIONNALITIES_PREFERENCES_NAME = "Dont_Tell_ABout_Missing_Functionnality";
    public SharedPreferences getDontTellAboutMissingFonctionnaliesPreferences() {
        return dontTellAboutMissingFonctionnalies;
    }

}
