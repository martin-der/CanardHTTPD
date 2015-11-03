package net.tetrakoopa.mdu.android.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.tetrakoopa.mdu.android.view.util.SystemUIUtil;
import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public abstract class DependencyHandler {

	private static final String TAG = "mdu.and.DependencyHandler";

	public static class DependencyResolution {

		public static enum Type {
			WEB_PAGE, FILE
		};

		public final String name;
		public final String url;
		public final Type type;

		public DependencyResolution(String name, String url, Type type) {
			this.name = name;
			this.url = url;
			this.type = type;
		}

		public DependencyResolution(String name, String url) {
			this(name, url, guessTypeFromURL(url));
		}

		protected static Type guessTypeFromURL(String url) {
			return url.endsWith(".apk") ? Type.FILE : Type.WEB_PAGE;
		}
	}

	private final static boolean USE_PROGRESS_DIALOG = false;

	private BroadcastReceiver receiver;

	private boolean downloading = false;

	private final static List<DependencyResolution> dependencyResolutions = new ArrayList<DependencyResolution>();

	public DependencyHandler(Context context) {
		if (context != null)
			setContext(context);
	}

	public DependencyHandler() {
		this(null);
	}

	private void setContext(Context context) {
		populateDependencyResolutions(context);

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				endDownloadListenning(context);
				Long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
				Query query = new Query();
				query.setFilterById(downloadId);
				DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
				Cursor cursor = downloadManager.query(query);
				if (!cursor.moveToFirst()) {
					Log.e(TAG, "No such download #" + downloadId);
					onDownloadComplete(context, null);
					return;
				}
				if (DownloadManager.STATUS_SUCCESSFUL != DataBaseUtil.getInt(cursor, DownloadManager.COLUMN_STATUS)) {
					Log.e(TAG, "Download #" + downloadId + " failed");
					onDownloadComplete(context, null);
					return;
				}
				String uri = DataBaseUtil.getString(cursor, DownloadManager.COLUMN_LOCAL_URI);
				File file = new File(uri);
				onDownloadComplete(context, file);
			}
		};
	}


	public boolean isDependencyFullfilled(Context context) {
		return getDependencyResolution(context) != null;
	}

	/** @return null if no matching dependency was found */
	public DependencyResolution getDependencyResolution(Context context) {
		for (android.content.pm.ApplicationInfo applicationInfo : context.getPackageManager().getInstalledApplications(0)) {
			for (DependencyResolution dependencyResolution : dependencyResolutions) {
				if (applicationInfo.packageName.equals(dependencyResolution.name)) {
					return dependencyResolution;
				}
			}
		}
		return null;
	}

	private synchronized void endDownloadListenning(Context context) {
		context.unregisterReceiver(receiver);

	}

	public synchronized boolean isDownloading() {
		return downloading;
	}

	public synchronized void cancelDownload(Context context) {
		endDownloadListenning(context);
	}

	public synchronized boolean startDownload(final Context context) {

		if (isDownloading())
			return false;
		
		if (dependencyResolutions.size()<1)
			return false;

		try {

			downloading = true;

			// set listener ready

			context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
			context.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_NOTIFICATION_CLICKED));

			// start download

			if (USE_PROGRESS_DIALOG) {
				SystemUIUtil.showProgress(context, "Download", "Downloading Shoual application", new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog1) {
						// TODO Auto-generated method stub

					}
				});
			}

			DependencyResolution dependencyResolution = dependencyResolutions.get(0);

			DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
			DownloadManager.Request request = new DownloadManager.Request(Uri.parse(dependencyResolution.url));
			request.setTitle(downloadTitle(dependencyResolution));
			downloadManager.enqueue(request);

		} catch (Exception ex) {
			Log.i(TAG, "Download start failed : " + ex.getMessage());
			Log.d(TAG, " => ", ex);
			return false;
		}

		return true;
	}
	
	protected abstract String downloadTitle(DependencyResolution dependencyResolution);
	
	protected abstract void onDownloadComplete(Context context, File downloadedFile);

	protected abstract void populateDependencyResolutions(Context context, List<DependencyResolution> dependencyResolutions);
	
	public final void populateDependencyResolutions(Context context) {
		populateDependencyResolutions(context, dependencyResolutions);
	}

	public void showDownloads(Context context) {
		Intent intent = new Intent();
		intent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
		context.startActivity(intent);
	}

}
