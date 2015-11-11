package net.tetrakoopa.canardhttpd;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import net.tetrakoopa.canardhttpd.CanardHTTPDService.LocalBinder;
import net.tetrakoopa.canardhttpd.service.sharing.SharesManager;
import net.tetrakoopa.canardhttpd.util.ShareFeedUtil;
import net.tetrakoopa.canardhttpd.view.action.MainAction;
import net.tetrakoopa.mdu.android.util.ContractuelUtil;
import net.tetrakoopa.mdu.android.util.ResourcesUtil;
import net.tetrakoopa.mdu.android.view.util.SystemUIUtil;

import java.io.File;


public class CanardHTTPDActivity extends AppCompatActivity {

	public enum UNMET_REQUIREMENT {
		PERMISSION_MISSING_READ_EXTERNAL_STORAGE;
	}

	public final static String MANIFEST_PACKAGE = "net.tetrakoopa.canardhttpd";
	public final static String MANIFEST_ACTIVITY = MANIFEST_PACKAGE+".CanardHTTPDActivity";

	public final static String TAG = "CanardHTTPD";

	private boolean neverCheckIntentParameters;
	private boolean needToCheckPickupActivityReturn;

	private String serverIp;

	private MainAction mainAction;
	


	private CanardHTTPDService service;
	private boolean serviceBound = false;

	private Uri uriFromPickupActivityReturn;

	private Bundle savedInstanceState;

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

		neverCheckIntentParameters = true;

		createService();

		SystemUIUtil.values_R.strings.dont_show_again = R.string.dont_show_again;
	}

	private void copyDefaultCertificatToDir(String path) {
		final File certificatsDirectory = getDir("security/certificate", MODE_PRIVATE );

		//certificatsDirectory.a AssetManager.AssetInputStream qsd;

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "unbindService ");
		unbindService(connection);
		//stopService(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.canard_httpd, menu);

		return true;
	}

	@Override
	public void onStart() {
		super.onStart();
	}
	@Override
	public void onStop() {
		super.onStop();
	}

	private void createService() {
		final Intent intent = new Intent(this, CanardHTTPDService.class);
		Log.d(TAG, "bindService ");
		bindService(intent, connection, Context.BIND_AUTO_CREATE);
		Log.d(TAG, "bindService done");
		startService(intent);
	}

	private final ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName componentName, IBinder serviceBinder) {

			LocalBinder binder = (LocalBinder) serviceBinder;
			CanardHTTPDActivity.this.service = binder.getService();
			serviceBound = true;
			mainAction.onServiceConnected(componentName, serviceBinder);

			if (neverCheckIntentParameters) {
				neverCheckIntentParameters = false;
				if (ShareFeedUtil.existsIntentParameter(CanardHTTPDActivity.this)) {
					if (!ShareFeedUtil.addSharedObjectFromIntentParameter(CanardHTTPDActivity.this, getService().getSharesManager())) {

					}
				}
			}
			if (needToCheckPickupActivityReturn) {
				needToCheckPickupActivityReturn = false;
				if (ShareFeedUtil.tryAddFileToSharesElseNotify(CanardHTTPDActivity.this, getService().getSharesManager(), uriFromPickupActivityReturn))
					mainAction.invalidateListe();
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
			SystemUIUtil.showOKDialog(this, "!!!!", message(R.string.no_activity_for_picking_file_or_directory));
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

		if (id==R.id.action_menu_settings) {
			startActivity(new Intent(this, SettingsActivity.class), savedInstanceState);
			return true;
		}


		//noinspection SimplifiableIfStatement
		//if (id == R.id.action_settings) {
		//	return true;
		//}

		return super.onOptionsItemSelected(item);
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
