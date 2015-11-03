package net.tetrakoopa.mdu.android.util;

import java.io.IOException;
import java.util.Locale;

import net.tetrakoopa.mdu.Mdu;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.Html;
import android.util.Log;
import android.webkit.WebView;

public class ContractuelUtil {

	private static String DEFAULT_ENCODING = "UTF-8";

	public interface AcceptanceResponseHandler {
		void handleResponse(boolean accept);
	}
	public interface ConditionnalAcceptanceResponseHandler extends AcceptanceResponseHandler {
		boolean isAccepted();
	}

	public static class PreferenceSavingAndActivityClosingAcceptanceResponse implements ConditionnalAcceptanceResponseHandler {

		private final Activity activity;
		private final SharedPreferences preferences;
		private final String preferenceKey;


		public PreferenceSavingAndActivityClosingAcceptanceResponse(Activity activity, String preferenceName, String preferenceKey) {
			this.activity = activity;
			preferences = activity.getSharedPreferences(preferenceName, Activity.MODE_PRIVATE);
			this.preferenceKey = preferenceKey;
		}



		@Override
		public boolean isAccepted() {
			return preferences.getBoolean(preferenceKey, false);
		}

		@Override
		public void handleResponse(boolean accept) {
			if (accept) {
				preferences.edit().putBoolean(preferenceKey, true).commit();
			} else {
				activity.finish();
			}
		}
	}

	public static void show(final Activity activity, String assetFilename, int titleResourceId, int closeResourceId) {
		show(activity, assetFilename, ResourcesUtil.getString(activity, titleResourceId), ResourcesUtil.getString(activity, closeResourceId));
	}

	public static void show(final Activity activity, String assetFilename, boolean isHtml, int titleResourceId, int closeResourceId) {
		show(activity, assetFilename, isHtml, ResourcesUtil.getString(activity, titleResourceId), ResourcesUtil.getString(activity, closeResourceId));
	}

	public static void show(final Activity activity, String assetFilename, String titleText, String closeText) {

		show(activity, assetFilename, isHTMLFilename(assetFilename), titleText, closeText);
	}

	public static void show(final Activity activity, String assetFilename, boolean isHtml, String titleText, String closeText) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		builder.setCancelable(true);
		builder.setNeutralButton(closeText, null);

		final String text;

		try {
			text = readAsset(activity, assetFilename).toString();
		} catch (IOException e) {
			Log.e(Mdu.TAG, "Unable to read text from " + assetFilename + " : " + e.getClass().getName() + ":" + e.getMessage());
			return;
		}
		makeDialogContent(activity, builder, titleText, text, isHtml);

		builder.create().show();
	}

	public static void showForAcceptanceIfNeeded(final Activity activity,  ConditionnalAcceptanceResponseHandler userResponseHandler, String assetFilename, int titleResourceId, int acceptResourceId, int refuseResourceId) {
		showForAcceptanceIfNeeded(activity, userResponseHandler, assetFilename, ResourcesUtil.getString(activity, titleResourceId), ResourcesUtil.getString(activity, acceptResourceId), ResourcesUtil.getString(activity, refuseResourceId));
	}

	public static void showForAcceptanceIfNeeded(final Activity activity, ConditionnalAcceptanceResponseHandler userResponseHandler, String assetFilename, String titleText, String acceptText, String refuseText) {
		if (!userResponseHandler.isAccepted()) {
			showForAcceptance(activity, userResponseHandler, assetFilename, titleText, acceptText, refuseText);
		}
	}

	public static void showForAcceptance(final Activity activity, AcceptanceResponseHandler userResponseHandler, String assetFilename, int titleResourceId, int acceptResourceId, int refuseResourceId) {
		showForAcceptance(activity, userResponseHandler, assetFilename, ResourcesUtil.getString(activity, titleResourceId), ResourcesUtil.getString(activity, acceptResourceId), ResourcesUtil.getString(activity, refuseResourceId));
	}
	public static void showForAcceptance(final Activity activity, final AcceptanceResponseHandler userResponseHandler, String assetFilename, String titleText, String acceptText, String refuseText) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(titleText);
		builder.setCancelable(true);
		builder.setPositiveButton(acceptText, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				userResponseHandler.handleResponse(true);
			}
		});
		builder.setNegativeButton(refuseText, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				userResponseHandler.handleResponse(false);

			}
		});
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				userResponseHandler.handleResponse(false);
			}
		});
		final String text;
		try {
			text = readAsset(activity, assetFilename).toString();
		} catch (IOException ioex) {
			throw new IllegalStateException("Unable to load text ('"+assetFilename+"') : "+ioex.getLocalizedMessage(), ioex);
		}
		makeDialogContent(activity, builder, titleText, text, isHTMLFilename(assetFilename));
		builder.create().show();
	}

	private static boolean isHTMLFilename(String filename) {
		final String lowerCaseFilename = filename.toLowerCase(Locale.US);
		return lowerCaseFilename.endsWith(".html") || lowerCaseFilename.endsWith(".htm") || lowerCaseFilename.endsWith(".xhtml");
	}

	private static CharSequence readAsset(Activity activity, String asset) throws IOException {
		return FileUtil.readCharSequence(activity.getAssets().open(asset));
	}

	private static void makeDialogContent(Activity activity, AlertDialog.Builder builder, String title, String content, boolean isHtml) {
		if (isHtml) {
			final WebView view = new WebView(activity);
			builder.setView(view);
			view.loadData(content, "text/html", DEFAULT_ENCODING);
			view.setClickable(true);
			builder.setMessage(Html.fromHtml(title));
		} else {
			builder.setMessage(content);
		}
	}

}