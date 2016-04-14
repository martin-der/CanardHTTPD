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
import net.tetrakoopa.mdua.view.util.SystemUIUtil;

import java.io.File;
import java.util.List;


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
	private boolean serviceBound = false;

	private Uri uriFromPickupActivityReturn;

	private Bundle savedInstanceState;

    private SharedPreferences dontTellAboutMissingFonctionnalies;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.canard_httpd, menu);

		MenuItem item = menu.findItem(R.id.menu_server_toggle);
		item.setActionView(R.layout.main_switch);

		mainAction.setMenu(menu);

		bindHTTPService();

		/*final MenuItem toggleservice = menu.findItem(R.id.toggleservice);
		final Switch actionView = (Switch) toggleservice.getActionView();
		actionView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Toast.makeText(CanardHTTPDActivity.this, "Switch : " + isChecked, Toast.LENGTH_SHORT).show();
			}
		});*/

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onStart() {
		super.onStart();
		LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
				new IntentFilter(CanardHTTPDService.MESSAGE_INTENT_SERVER_STATE_CHANGE)
		);
	}
	@Override
	public void onStop() {
		Log.d(TAG, "unbindService ");
		unbindHTTPService();
		//stopService(intent);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
		super.onStop();
	}

	private void updateUI(CanardHTTPDService.ServerStatus serverStatus) {
		mainAction.updateUI(serverStatus);

	}

	private void bindHTTPService() {
		final Intent intent = new Intent(this, CanardHTTPDService.class);
		Log.d(TAG, "bindService ");
		bindService(intent, connection, Context.BIND_AUTO_CREATE);
		Log.d(TAG, "bindService done");
		startService(intent);
	}
	private void unbindHTTPService() {
		unbindService(connection);
	}


    private final ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName, IBinder serviceBinder) {

            CanardHTTPDService.LocalBinder binder = (CanardHTTPDService.LocalBinder) serviceBinder;
			CanardHTTPDActivity.this.service = binder.getService();
			serviceBound = true;
			mainAction.onServiceConnected(componentName, serviceBinder);

			if (intentParametersNeedToBeChecked) {
				intentParametersNeedToBeChecked = false;
                tryHandleSend();
			}
			if (needToCheckPickupActivityReturn) {
				needToCheckPickupActivityReturn = false;
				if (ShareFeedUtil.tryAddFileToSharesElseNotify(CanardHTTPDActivity.this, getService().getSharesManager(), uriFromPickupActivityReturn))
					mainAction.invalidateListe();
			}
			updateUI(binder.getService().isServerSarted() ? CanardHTTPDService.ServerStatus.UP : CanardHTTPDService.ServerStatus.DOWN);
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
					if (serviceBound) {
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
		int id = item.getItemId();

		if (id==R.id.server_switch) {
            Toast.makeText(this, "Clicket switch 3", Toast.LENGTH_SHORT).show();
			boolean isChecked = !item.isChecked();
			item.setChecked(isChecked);
            return true;
        }

		if (id==R.id.action_menu_server) {
			startActivity(new Intent(this, SettingsActivity.class), savedInstanceState);
			return true;
		}

        if (id==R.id.action_menu_misc) {
            startActivity(new Intent(this, SettingsActivity.class), savedInstanceState);
            return true;
        }


		//noinspection SimplifiableIfStatement
		//if (id == R.id.action_settings) {
		//	return true;
		//}

		return super.onOptionsItemSelected(item);
	}

    private boolean tryHandleSend() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent);
                return true;
            }
            final Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            ShareFeedUtil.tryAddFileToSharesElseNotify(this, getService().getSharesManager(), uri);
            return true;
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            handleSendMultipleStreams(intent);
            return true;
        }

        return false;

    }
    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            Toast.makeText(this, "Received : "+sharedText, Toast.LENGTH_SHORT).show();
            // Update UI to reflect text being shared
        }
    }

    void handleSendStream(Intent intent) {
        Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (uri != null) {
            Toast.makeText(this, "Received : "+uri.getPath(), Toast.LENGTH_SHORT).show();
            // Update UI to reflect image being shared
        }
    }

    void handleSendMultipleStreams(Intent intent) {
        List<Uri> uris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (uris != null) {
            // Update UI to reflect multiple images being shared
        }
    }
    public static final String DONT_TELL_ABOUT_MISSING_FONCTIONNALITIES_PREFERENCES_NAME = "Dont_Tell_ABout_Missing_Functionnality";
    public SharedPreferences getDontTellAboutMissingFonctionnaliesPreferences() {
        return dontTellAboutMissingFonctionnalies;
    }


//	public class NetworkListener extends BroadcastReceiver {
//
//		// public static void registerStorageListener(Context context) {
//		// IntentFilter filter = new IntentFilter();
//		// filter.addAction(Intent.EV);
//		// ZZZNetworkListener listener = new ZZNetworkListener();
//		// context.registerReceiver(listener, filter);
//		// }
//
//		@Override
//		public void onReceive(Context context, Intent arg1) {
//
//			Toast.makeText(context, "Network changed !", Toast.LENGTH_SHORT).show();
//		}
//
//	}

}
