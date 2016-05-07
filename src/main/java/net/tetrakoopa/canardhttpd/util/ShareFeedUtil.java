package net.tetrakoopa.canardhttpd.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import net.tetrakoopa.canardhttpd.CanardHTTPDActivity;
import net.tetrakoopa.canardhttpd.CanardHTTPDService;
import net.tetrakoopa.canardhttpd.R;
import net.tetrakoopa.canardhttpd.domain.common.SharedThing;
import net.tetrakoopa.canardhttpd.service.sharing.SharesManager;
import net.tetrakoopa.canardhttpd.service.sharing.exception.AlreadySharedException;
import net.tetrakoopa.canardhttpd.service.sharing.exception.BadShareTypeException;
import net.tetrakoopa.canardhttpd.service.sharing.exception.NotFoundFromUriException;
import net.tetrakoopa.mdua.view.util.SystemUIUtil;

public class ShareFeedUtil {

	private final static String TAG = CanardHTTPDService.TAG;

	/*public static boolean addSharedObjectFromIntentParameter(CanardHTTPDActivity canardActivity, SharesManager manager) {
		final String type = canardActivity.getIntent().getType();

		if (type != null) {
			if (type.equals("text/plain")) {

				String name = null;

				// TODO ask user to give a name to the shared text

				manager.addText(name, (String) getIntentExtra(canardActivity, android.content.Intent.EXTRA_TEXT));
				return true;
			}
		}

		final android.net.Uri streamUri = (android.net.Uri) getIntentExtra(canardActivity, android.content.Intent.EXTRA_STREAM);
		Toast.makeText(canardActivity, "streamUri = " + (streamUri == null ? "<null>" : streamUri.getPath()), Toast.LENGTH_LONG).show();

		if (streamUri != null) {
			Toast.makeText(canardActivity, "streamUri = " + streamUri.getClass().getName() + ":" + streamUri.getPath(), Toast.LENGTH_LONG).show();
			tryAddFileToSharesElseNotify(canardActivity, manager, streamUri);
			return true;
		}
		
		//Toast.makeText(canardActivity,canardActivity.message(R.string.error_failed_to_share) + ":" + canardActivity.message(R.string.error_activity_param_null_mime_type), Toast.LENGTH_LONG).show();
		//SystemUIUtil.showOKDialog(canardActivity, canardActivity.message(R.string.error_failed_to_share), canardActivity.message(R.string.error_activity_param_null_mime_type));

		return false;
	}*/

	public static boolean tryAddFileToSharesElseNotify(final Context context, SharesManager manager, final Uri... uris) {
		final ContentResolver contentResolver = context.getContentResolver();
		boolean success = true;
		for (Uri uri : uris) {
			try {
				final String dontAskPreferenceName = "Shares_Persistance_with_READ_EXTERNAL_CONTEXT";
				final SharedThing thing = manager.add(contentResolver, uri);
				Log.i(TAG, "Now sharing '" + uri + "'");

			/*if (manager.unmetRequirements.contains(CanardHTTPDActivity.UNMET_REQUIREMENT.PERMISSION_MISSING_READ_EXTERNAL_STORAGE)) {
				if (!activity.getDontTellAboutMissingFonctionnaliesPreferences().getBoolean(dontAskPreferenceName, false)) {
					final SystemUIUtil.DontShowAgainLinkedToPreference dontShowAgain = new SystemUIUtil.DontShowAgainLinkedToPreference(true, CanardHTTPDActivity.DONT_TELL_ABOUT_MISSING_FONCTIONNALITIES_PREFERENCES_NAME, dontAskPreferenceName);
					SystemUIUtil.showOKDialog(activity, activity.message(R.string.title_half_functionnal), activity.message(R.string.message_READ_EXTERNAL_STORAGE_for_share_manager), dontShowAgain);
				}
			}*/
			} catch (AlreadySharedException e) {
				Log.i(TAG, "Uri '" + uri + "' already shared");
				success = false;
			} catch (BadShareTypeException e) {
				Log.w(TAG, "Uri '" + uri + "' cannot be shared : " + e.getMessage());
				success = false;
			} catch (NotFoundFromUriException e) {
				Log.e(TAG, "Uri '" + uri + "' couldn't be found : " + e.getLocalizedMessage(), e);
				success = false;
			}
		}
		return success;
	}

	private final static Object getIntentExtra(Activity activity, String key) {
		Intent intent = activity.getIntent();
		Bundle extra = intent.getExtras();
		return extra != null ? extra.get(key) : null;
	}


}
