package net.tetrakoopa.canardhttpd;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import net.tetrakoopa.canardhttpd.CanardHTTPDService.ServerStatusChangeListener.ActionTrigger;
import net.tetrakoopa.canardhttpd.service.http.CanardHTTPD;
import net.tetrakoopa.canardhttpd.service.sharing.SharesManager;
import net.tetrakoopa.mdu.util.ExceptionUtil;
import net.tetrakoopa.mdua.util.ResourcesUtil;




@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class CanardHTTPDService extends Service {

	public final static String MANIFEST_ACTIVITY = CanardHTTPDActivity.MANIFEST_PACKAGE+".CanardHTTPDService";

	public final static String TAG = "CanardHTTPDService";
	
	private final static int NOTIFICATION_ID = 777;

	private final static String EVENT_ID_SERVER_STOP = "net.tetrakoopa.canardHttpD.Service.STOP";
	private final static String EVENT_ID_SERVER_KILL = "net.tetrakoopa.canardHttpD.Service.KILL";

	private final static String INTEND_EXTRA_KEY_COMMAND = "COMMAND";
	private final static Integer INTEND_EXTRA_COMMAND_UNDEFINED = 0;
	private final static Integer INTEND_EXTRA_COMMAND_SERVER_STOP = 1;
	private final static Integer INTEND_EXTRA_COMMAND_SERVER_KILL = 2;

	private final static String MESSAGE_INTENT_EXTRA_COMMAND_KEY = "command";
	private final static String MESSAGE_SERVER_STOP = "net.tetrakoopa.canardHttpD.Message.Service.SERVER_STOP";
	private final static String MESSAGE_SERVER_KILL = "net.tetrakoopa.canardHttpD.Message.Service.SERVER_KILL";

	public final static String MESSAGE_INTENT_SERVER_STATE_CHANGE = "net.tetrakoopa.canardHttpD.Server.state.change";
	public final static String MESSAGE_INTENT_SERVER_STATE_CHANGE_VALUE = "value";
	public final static String MESSAGE_INTENT_SERVER_STATE_CHANGE_ERROR = "error";

	private final IBinder binder = new LocalBinder();

	private int requestedPort = 8080;
	private int requestedSecurePort = 8443;

	private final SharesManager sharesManager = new SharesManager();

	private CanardHTTPD server;

	private Intent applicationIntent;
	private LocalBroadcastManager broadcaster;

	private ServerStatusChangeListener localStatusChangeListener = new ServerStatusChangeListener() {
		@Override
		public void onServerStatusChange(CanardHTTPDService service, ActionTrigger actionTrigger, ServerStatus status, Throwable ex) {
			final Intent intent = new Intent(MESSAGE_INTENT_SERVER_STATE_CHANGE);
			intent.putExtra(MESSAGE_INTENT_SERVER_STATE_CHANGE_VALUE, status.ordinal());
			if (ex != null) {
				intent.putExtra(MESSAGE_INTENT_SERVER_STATE_CHANGE_ERROR, ExceptionUtil.getMessages(ex));
			}
			broadcaster.sendBroadcast(intent);
		}
	};

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
		super.onCreate();
		applicationIntent = new Intent(this, CanardHTTPDActivity.class);
		broadcaster = LocalBroadcastManager.getInstance(this);
		Log.d(TAG, "onCreate");
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (intent == null) {
			return START_STICKY;
		}

		if (intent.getExtras() != null) {
			final String command = intent.getExtras().getString(MESSAGE_INTENT_EXTRA_COMMAND_KEY);
			if (command != null) {
				if (command.equals(MESSAGE_SERVER_STOP)) {
					//stopRecord();
				} else if (command.equals(MESSAGE_SERVER_KILL)) {
					stopHTTPDServer(null);
				}
			}
		}

		Log.d(TAG,"onStartCommand");

		return START_STICKY;
	}

	public enum ServerStatus {
		DOWN, STARTING, UP, STOPING;

		public static ServerStatus fromOrdinal(int ordinal) {
			if (ordinal<0 || ordinal>=ServerStatus.values().length) {
				throw new IllegalArgumentException("No such "+ServerStatus.class.getName()+" with ordinal value "+ordinal);
			}
			return ServerStatus.values()[ordinal];
		}
	}

	private void removeNotification() {
		//((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancel(NOTIFICATION_ID);
		stopForeground(true);
	}
	private void showNotification() {

		final String title = message(R.string.app_name);
		final String text = message(R.string.server_status_no_download);
		String info = ""+sharesManager.getThings().size()+" object(s)";

		final Intent serverStopIntent = new Intent(this, NotificationEventReceiver.class);
		serverStopIntent.putExtra(INTEND_EXTRA_KEY_COMMAND, INTEND_EXTRA_COMMAND_SERVER_STOP);
		PendingIntent pendingIntentStop = PendingIntent.getBroadcast(this, 0, serverStopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		final Intent serverKillIntent = new Intent(this, NotificationEventReceiver.class);
		serverKillIntent.putExtra(INTEND_EXTRA_KEY_COMMAND, INTEND_EXTRA_COMMAND_SERVER_KILL);
		PendingIntent pendingIntentKill = PendingIntent.getBroadcast(this, 0, serverKillIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification.Builder builder = new Notification.Builder(this);
		builder.setContentTitle(title).setSmallIcon(R.mipmap.canard_httpd_server);
		builder.setContentText(text).setContentInfo(info);
		builder.setOngoing(true);
		builder.addAction(android.R.drawable.stat_sys_download_done, message(R.string.server_action_finish_then_stop), pendingIntentStop);
		builder.addAction(android.R.drawable.ic_notification_clear_all, message(R.string.server_action_kill), pendingIntentKill);
		Notification notification = builder.build();

		notification.contentIntent = PendingIntent.getActivity(this, 0, applicationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

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
		super.startService(intent);
		startServer(listener, canardHTTPDActivity);
	}
	private synchronized void startServer(ServerStatusChangeListener listener, CanardHTTPDActivity canardHTTPDActivity) {

		localStatusChangeListener.onServerStatusChange(this, ActionTrigger.START, ServerStatus.STARTING, null);
		if (listener != null)
			listener.onServerStatusChange(this, ActionTrigger.START, ServerStatus.STARTING, null);
		try {
			Log.d(TAG, "HTTP Server : creating...");
			server = new CanardHTTPD(canardHTTPDActivity, sharesManager, null, requestedPort, requestedSecurePort, "file:///android_asset/security/martin.home.crt", "martin home");
			Log.d(TAG, "HTTP Server : starting...");
			server.start();
			//server.join();
			Log.i(TAG, "HTTP Server : Up");
			localStatusChangeListener.onServerStatusChange(this, ActionTrigger.START, ServerStatus.UP, null);
			if (listener != null)
				listener.onServerStatusChange(this, ActionTrigger.START, ServerStatus.UP, null);
			showNotification();
		} catch (Exception ex) {
			server = null;
			Log.e(TAG, "HTTP server : Start failed", ex);
			localStatusChangeListener.onServerStatusChange(this, ActionTrigger.START, ServerStatus.DOWN, null);
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

		localStatusChangeListener.onServerStatusChange(this, ActionTrigger.STOP, ServerStatus.STOPING, null);
		if (listener != null)
			listener.onServerStatusChange(this, ActionTrigger.STOP, ServerStatus.STOPING, null);

		try {
			server.stop();
			server.join();
			server = null;
			Log.i(TAG, "Server Down");
			localStatusChangeListener.onServerStatusChange(this, ActionTrigger.STOP, ServerStatus.DOWN, null);
			if (listener != null)
				listener.onServerStatusChange(this, ActionTrigger.STOP, ServerStatus.DOWN, null);
			removeNotification();
		} catch (Exception ex) {
			Log.e(TAG, "Stop server failed", ex);
			localStatusChangeListener.onServerStatusChange(this, ActionTrigger.STOP, ServerStatus.DOWN, null);
			if (listener != null)
				listener.onServerStatusChange(this, ActionTrigger.STOP, ServerStatus.DOWN, ex);
		}
	}

	public int getPort() {
		return server == null ? requestedPort : server.getPort(0);
	}


	private String message(int id) {
		return ResourcesUtil.getString(this, id);
	}

	public static class NotificationEventReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			final int command = intent.getIntExtra(INTEND_EXTRA_KEY_COMMAND, INTEND_EXTRA_COMMAND_UNDEFINED);

			if (command == INTEND_EXTRA_COMMAND_SERVER_STOP) {
				final Intent serviceIntent = new Intent(context, CanardHTTPDService.class);
				serviceIntent.putExtra(MESSAGE_INTENT_EXTRA_COMMAND_KEY, MESSAGE_SERVER_STOP);
				context.startService(serviceIntent);
				return;
			}
			if (command == INTEND_EXTRA_COMMAND_SERVER_KILL) {
				final Intent serviceIntent = new Intent(context, CanardHTTPDService.class);
				serviceIntent.putExtra(MESSAGE_INTENT_EXTRA_COMMAND_KEY, MESSAGE_SERVER_KILL);
				context.startService(serviceIntent);
				return;
			}
		}
	}
}
