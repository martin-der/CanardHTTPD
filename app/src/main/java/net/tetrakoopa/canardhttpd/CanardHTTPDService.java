package net.tetrakoopa.canardhttpd;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import net.tetrakoopa.canardhttpd.CanardHTTPDService.ServerStatusChangeListener.ActionTrigger;
import net.tetrakoopa.canardhttpd.listener.HTTPServerCancelReceiver;
import net.tetrakoopa.canardhttpd.service.http.CanardHTTPD;
import net.tetrakoopa.canardhttpd.service.sharing.SharesManager;
import net.tetrakoopa.mdu.android.util.ResourcesUtil;



@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class CanardHTTPDService extends Service {

	public final static String MANIFEST_ACTIVITY = CanardHTTPDActivity.MANIFEST_PACKAGE+".CanardHTTPDService";

	public final static String TAG = "CanardHTTPDService";
	
	private final static int NOTIFICATION_ID = 777;

	private final IBinder binder = new LocalBinder();

	private int requestedPort = 8080;
	private int requestedSecurePort = 8443;

	private final SharesManager sharesManager = new SharesManager();

	private CanardHTTPD server;

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public class LocalBinder extends Binder {
		public CanardHTTPDService getService() {
			return CanardHTTPDService.this;
		}
	}

	public SharesManager getSharesManager() {
		return sharesManager;
	}
	
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
	}
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		Log.d(TAG,"onStartCommand");

		return START_STICKY;
	}

	public enum ServerStatus {
		DOWN, STARTING, UP, STOPING;
	}

	private void removeNotification() {
		//((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(NOTIFICATION_ID);
		stopForeground(true);
	}
	private void showNotification(final Intent applicationIntent) {

		final String title = message(R.string.app_name);
		final String text = message(R.string.server_status_no_download);
		String info = ""+sharesManager.getThings().size()+" object(s)";

		Intent deleteIntent = new Intent(this, NotificationEventReceiver.class);
		PendingIntent pendingIntentStop = PendingIntent.getBroadcast(this, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		PendingIntent pendingIntentKill = PendingIntent.getBroadcast(this, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification.Builder builder = new Notification.Builder(this);
		builder.setContentTitle(title).setSmallIcon(R.mipmap.canard_httpd_server);
		builder.setContentText(text).setContentInfo(info);
		builder.setOngoing(true);
		builder.addAction(android.R.drawable.stat_sys_download_done, message(R.string.server_action_finish_then_stop), pendingIntentStop);
		builder.addAction(android.R.drawable.ic_notification_clear_all, message(R.string.server_action_kill), pendingIntentKill);
		Notification notification = builder.build();
		notification.contentIntent = PendingIntent.getActivity(this, 0, applicationIntent, Intent.FLAG_ACTIVITY_SINGLE_TOP);

		//((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notification);
		startForeground(NOTIFICATION_ID, notification);

	}

	public interface ServerStatusChangeListener {
		enum ActionTrigger {
			START, STOP, STATUS, DISCONNECTION;
		};

		void onServerStatusChange(CanardHTTPDService service, ActionTrigger actionTrigger, ServerStatus status, Throwable ex);
	}

	public boolean isServerSarted() {
		return server != null && server.isStarted();
	}

	public void start(Intent intent, CanardHTTPDActivity canardHTTPDActivity, ServerStatusChangeListener listener) {
		//Toast.makeText(this.getApplicationContext(), "start | starting service...", Toast.LENGTH_SHORT).show();
		super.startService(intent);
		//Toast.makeText(this.getApplicationContext(), "start | service started", Toast.LENGTH_SHORT).show();
		startServer(listener, canardHTTPDActivity);
		//Toast.makeText(this.getApplicationContext(), "start | HTTPD started", Toast.LENGTH_SHORT).show();
	}
	private synchronized void startServer(ServerStatusChangeListener listener, CanardHTTPDActivity canardHTTPDActivity) {

		if (listener != null)
			listener.onServerStatusChange(this, ActionTrigger.START, ServerStatus.STARTING, null);
		try {
			Log.d(TAG, "HTTP Server : creating...");
			server = new CanardHTTPD(canardHTTPDActivity, sharesManager, null, requestedPort, requestedSecurePort, "file:///android_asset/security/martin.home.crt.psk", "martin home");
			Log.d(TAG, "HTTP Server : starting...");
			server.start();
			//server.join();
			Log.i(TAG, "HTTP Server : Up");
			if (listener != null)
				listener.onServerStatusChange(this, ActionTrigger.START, ServerStatus.UP, null);
			showNotification(new Intent(this, CanardHTTPDActivity.class));
		} catch (Exception ex) {
			server = null;
			Log.e(TAG, "HTTP server : Start failed", ex);
			if (listener != null)
				listener.onServerStatusChange(this, ActionTrigger.START, ServerStatus.DOWN, ex);
		}
	}

	public void stop(ServerStatusChangeListener listener) {
		try {
			Log.d(TAG, "HTTP Server : stoping...");
			stopHTTPDServer(listener);
			Log.i(TAG, "HTTP Server : Down");
			removeNotification();
		} catch (Exception ex) {
			Log.e(TAG, "HTTP Server : Stop Failed", ex);
		}
		stopSelf();
	}
	private synchronized void stopHTTPDServer(ServerStatusChangeListener listener) {
		if (!isServerSarted()) {
			return;
		}
		if (listener != null)
			listener.onServerStatusChange(this, ActionTrigger.STOP, ServerStatus.STOPING, null);

		try {
			server.stop();
			server.join();
			server = null;
			Log.i(TAG, "Server Down");
			if (listener != null)
				listener.onServerStatusChange(this, ActionTrigger.STOP, ServerStatus.DOWN, null);
		} catch (Exception ex) {
			Log.e(TAG, "Stop server failed", ex);
			if (listener != null)
				listener.onServerStatusChange(this, ActionTrigger.STOP, ServerStatus.DOWN, ex);
		}
	}

	public int getPort() {
		return server == null ? requestedPort : getServerPort(false);
	}

	/**
	 * @throws IllegalStateException
	 *             when <code>checkConsistency</code> is queried and real server
	 *             port != requested port
	 */
	private int getServerPort(boolean checkConsistency) {
		final int listeningPort = server.getPort(0);
		if (listeningPort >= 0 && listeningPort != requestedPort)
			throw new IllegalStateException("Listening Port != Requested Port ( " + listeningPort + " != " + requestedPort + " )");
		return listeningPort;
	}


	private String message(int id) {
		return ResourcesUtil.getString(this, id);
	}

	public static class NotificationEventReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Toast.makeText(context, "Clicked some button", Toast.LENGTH_SHORT).show();

		}
	}
}
