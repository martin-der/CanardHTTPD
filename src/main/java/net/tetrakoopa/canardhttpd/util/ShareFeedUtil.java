package net.tetrakoopa.canardhttpd.util;

import android.app.Activity;
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

	public static boolean tryAddFileToSharesElseNotify(final Context context, SharesManager manager, final Uri uri) {
		try {
            final String dontAskPreferenceName = "Shares_Persistance_with_READ_EXTERNAL_CONTEXT";
			final SharedThing thing = manager.add(context, uri);
			//Toast.makeText(activity, "Added "+thing.getType()+ " '" + thing.getName() + "'", Toast.LENGTH_SHORT).show();
			/*if (manager.unmetRequirements.contains(CanardHTTPDActivity.UNMET_REQUIREMENT.PERMISSION_MISSING_READ_EXTERNAL_STORAGE)) {
                if (!activity.getDontTellAboutMissingFonctionnaliesPreferences().getBoolean(dontAskPreferenceName, false)) {
                    final SystemUIUtil.DontShowAgainLinkedToPreference dontShowAgain = new SystemUIUtil.DontShowAgainLinkedToPreference(true, CanardHTTPDActivity.DONT_TELL_ABOUT_MISSING_FONCTIONNALITIES_PREFERENCES_NAME, dontAskPreferenceName);
                    SystemUIUtil.showOKDialog(activity, activity.message(R.string.title_half_functionnal), activity.message(R.string.message_READ_EXTERNAL_STORAGE_for_share_manager), dontShowAgain);
                }
			}*/
			return true;
		} catch (AlreadySharedException e) {
			//SystemUIUtil.showOKDialog(activity, activity.message(R.string.error_title_share_failure), activity.message(R.string.error_file_already_shared));
			Log.i(CanardHTTPDService.TAG, "Uri '" + uri + "' already shared");
			return false;
		} catch (BadShareTypeException e) {
			Log.i(CanardHTTPDService.TAG, "Uri '" + uri + "' cannot be shared : " + e.getMessage());
			//Toast.makeText(activity, R.string.error_failed_to_share_this_file_type, Toast.LENGTH_LONG).show();
			return false;
		} catch (NotFoundFromUriException e) {
			final String message = "Uri '" + uri + "' couldn't be found : " + e.getLocalizedMessage();
			//SystemUIUtil.showOKDialog(activity, activity.message(R.string.error_title_share_failure), message);
			Log.e(CanardHTTPDService.TAG, message, e);
			return false;
		}
	}

	@Deprecated
	public static void addFakeObjectsToManager(SharesManager sharesManager) {
		if (sharesManager.getThings().isEmpty()) {
			int i = 1;
			for (; i > 0; i--) {
				sharesManager.addText("Some thoughts I had...", "The phenomenology movement in philosophy saw a radical change in the way in which we understand thought. Martin Heidegger's phenomenological analyses of the existential structure of man in Being and Time cast new light on the issue of thinking, unsettling traditional cognitive or rational interpretations of man which affect the way we understand thought. The notion of the fundamental role of non-cognitive understanding in rendering possible thematic consciousness informed the discussion surrounding Artificial Intelligence during the 1970s and 1980s.[12]\n" +
						"\n" +
						"Phenomenology, however, is not the only approach to thinking in modern Western philosophy. Philosophy of mind is a branch of philosophy that studies the nature of the mind, mental events, mental functions, mental properties, consciousness and their relationship to the physical body, particularly the brain. The mind-body problem, i.e. the relationship of the mind to the body, is commonly seen as the central issue in philosophy of mind, although there are other issues concerning the nature of the mind that do not involve its relation to the physical body.[13]");
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
				sharesManager.addText("Karl Ernst von Baer", "Ses ancêtres venaient de Westphalie. Il fait ses études à Revel et à l'université de Dorpat, ainsi qu'à Berlin, Vienne et Wurtzbourg.\n" +
						"\n" +
						"En 1817, il devient professeur assistant à l'université de Königsberg, professeur de zoologie en 1821, puis d'anatomie en 1826.\n" +
						"\n" +
						"En 1829, il s'installe brièvement à Saint-Pétersbourg avant de revenir à Königsberg. En 1834, il s'établit à Saint-Pétersbourg et rejoint l'Académie des sciences, d'abord en zoologie (de 1834 à 1846) puis en physiologie (de 1846 à 1862). Il dirige le 2e département de la Bibliothèque de l'Académie des sciences de Russie de 1835 à 1862.\n" +
						"\n" +
						"Il s'intéresse à de nombreux sujets comme l'anatomie, l'ichtyologie, l'ethnologie, l'anthropologie et la géographie. C'est le cofondateur et le premier président de la Société entomologique de Russie.\n" +
						"\n" +
						"Il passe la fin de sa vie à Dorpat où il est l'un des critiques les plus virulents des théories darwiniennes.\n" +
						"\n" +
						"Il étudie particulièrement le développement embryonnaires des animaux, découvrant les différents stades de la blastula et de la notochorde. Poursuivant les travaux de Caspar Friedrich Wolff (1734-1794), il décrit avec Christian Heinrich von Pander (1794-1865) le développement de l'embryon à partir de feuillets (ou couches) embryonnaires. Il est à l'origine de la loi de von Baer spécifiant que les caractères embryonnaires qui sont communs à plusieurs taxons animaux apparaissent plus précocement que les caractères distinctifs de ces taxons3.\n" +
						"\n" +
						"Son livre, Über Entwicklungsgeschichte der Tiere, publié en 1828, marque la fondation de l’embryologie comparée.\n" +
						"\n" +
						"Il est également l'auteur en géologie de la loi de Baer.\n" +
						"\n" +
						"Il est lauréat de la Médaille Copley en 1867. Karl Ernst von Baer est devenu membre étranger de la Royal Society le 15 juin 1854.\n" +
						"\n" +
						"C'est son portrait qui est représenté sur les billets de 2 couronnes estoniennes du début des années 1990. Parmi ses étudiants, l'on peut distinguer Adolph Grube.");
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
