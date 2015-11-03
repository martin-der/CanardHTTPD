package net.tetrakoopa.canardhttpd.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import net.tetrakoopa.canardhttpd.CanardHTTPDActivity;
import net.tetrakoopa.canardhttpd.service.sharing.SharesManager;
import net.tetrakoopa.canardhttpd.service.sharing.exception.AlreadySharedException;
import net.tetrakoopa.canardhttpd.service.sharing.exception.BadShareTypeException;
import net.tetrakoopa.mdu.android.util.SystemUtil;

import java.io.File;

public class ShareFeedUtil {

	public static boolean addSharedObjectFromIntentParameter(Activity canardActivity, SharesManager manager) {
		final String type = getIntentType(canardActivity);

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
			File file = new File(streamUri.getPath());
			tryToAddFileToSharesElseNotify(canardActivity, manager, file, type);
			return true;
		}
		
		//Toast.makeText(canardActivity,canardActivity.message(R.string.error_failed_to_share) + ":" + canardActivity.message(R.string.error_activity_param_null_mime_type), Toast.LENGTH_LONG).show();
		//SystemUIUtil.showOKDialog(canardActivity, canardActivity.message(R.string.error_failed_to_share), canardActivity.message(R.string.error_activity_param_null_mime_type));

		return false;
	}

	public static boolean tryToAddFileToSharesElseNotify(final Activity activity, SharesManager manager, final File file, String mimeType) {
		try {
			if (mimeType == null)
				mimeType = SystemUtil.getMimeType(file);
			Toast.makeText(activity, "tryToAddFileToShares('" + file.getPath() + "')", Toast.LENGTH_LONG).show();
			Toast.makeText(activity, "mime = '" + mimeType + "'", Toast.LENGTH_SHORT).show();
			manager.addInode(file, mimeType);
			return true;
		} catch (AlreadySharedException e) {
			//SystemUIUtil.showOKDialog(activity, activity.message(R.string.error_failed_to_share), activity.message(R.string.error_file_already_share));
			Log.i(CanardHTTPDActivity.TAG, "File '" + file.getPath() + "' already shared");
			return false;
		} catch (BadShareTypeException e) {
			Log.i(CanardHTTPDActivity.TAG, "File '" + file.getPath() + "' cannot be shared : " + e.getMessage());
			return false;
		}
	}

	public static String getIntentType(Activity activity) {
		return activity.getIntent().getType();
	}

	public static boolean existsIntentParameter(CanardHTTPDActivity canardHTTPDActivity) {
		return getIntentType(canardHTTPDActivity) != null;
	}

	@Deprecated
	public static void addFakeObjectsToManager(SharesManager sharesManager) {
		if (sharesManager.getThings().isEmpty()) {
			int i = 1;
			for (; i > 0; i--) {
				sharesManager.addText("Some thoughts I had...", "qsdqsdd sf sd fdg !");
				sharesManager.addText(null, "Lalalalalalalalolololilili");
				String if_kipplig = 
				"IF you can keep your head when all about you\n" 
				+"Are losing theirs and blaming it on you,\n"
				+"If you can trust yourself when all men doubt you,\n"
				+"But make allowance for their doubting too;\n"
				+"If you can wait and not be tired by waiting,\n"
				+"Or being lied about, don't deal in lies,\n"
				+"Or being hated, don't give way to hating,\n"
				+"And yet don't look too good, nor talk too wise:\n"
				+"If you can dream - and not make dreams your master;\n"
				+"If you can think - and not make thoughts your aim;\n"
				+"If you can meet with Triumph and Disaster\n"
				+"And treat those two impostors just the same;\n"
				+"If you can bear to hear the truth you've spoken\n"
				+"Twisted by knaves to make a trap for fools,\n"
				+"Or watch the things you gave your life to, broken,\n"
				+"And stoop and build 'em up with worn-out tools:\n"
				+"\n"
				+"If you can make one heap of all your winnings\n" 
				+"And risk it on one turn of pitch-and-toss,\n"
				+"And lose, and start again at your beginnings\n"
				+"And never breathe a word about your loss;\n"
				+"If you can force your heart and nerve and sinew\n"
				+"To serve your turn long after they are gone,\n"
				+"And so hold on when there is nothing in you\n"
				+"Except the Will which says to them: 'Hold on!'\n"
				+"\n"
				+"If you can talk with crowds and keep your virtue,\n"
				+"' Or walk with Kings - nor lose the common touch,\n"
				+"if neither foes nor loving friends can hurt you,\n"
				+"If all men count with you, but none too much;\n"
				+"If you can fill the unforgiving minute\n"
				+"With sixty seconds' worth of distance run,\n"
				+"Yours is the Earth and everything that's in it,\n"
				+"And - which is more - you'll be a Man, my son!\n";
				sharesManager.addText("IF - Kippling", if_kipplig);
				sharesManager.addText(null, "aze qsdai vlqskdjf sdf");
				sharesManager.addText(null, "iazppco qspdfpozeoropbpowfobdg");
			}
		}
	}
	private final static Object getIntentExtra(Activity activity, String key) {
		Intent intent = activity.getIntent();
		Bundle extra = intent.getExtras();
		return extra != null ? extra.get(key) : null;
	}


}
