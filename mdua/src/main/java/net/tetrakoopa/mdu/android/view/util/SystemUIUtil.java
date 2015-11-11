package net.tetrakoopa.mdu.android.view.util;

import net.tetrakoopa.mdu.R;
import net.tetrakoopa.mdu.android.util.ResourcesUtil;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

public class SystemUIUtil {

	public static class Values {
		public static class Strings {
			public int dont_show_again = R.string.dont_show_again;
			public int dont_ask_again = R.string.dont_ask_again;
		}

		public final Strings strings = new Strings();
	}
	public static final Values values_R = new Values();

	public static class DontShowAgainLinkedToPreference {
		public boolean defaultValue;
		public String name;
		public String key;
		public int mode;
        public boolean result;
		public DontShowAgainLinkedToPreference(boolean defaultValue, String name, String key) {
			this(defaultValue, name, key, Context.MODE_PRIVATE);
		}
		public DontShowAgainLinkedToPreference(boolean defaultValue, String name, String key, int mode) {
			this.defaultValue = defaultValue; this.name = name; this.key = key;
			this.mode = mode;
		}
	}


	private SystemUIUtil() {
	}

	public static void showOKDialog(Context context, String titre, String message) {
		showOkDialog(context, titre, message, null, null, 0);
	}
	public static void showOKDialog(Context context, String titre, String message, DontShowAgainLinkedToPreference dontShowAgain) {
		showOkDialog(context, titre, message, dontShowAgain, null, 0);
	}
	public static void showOKDialog(Context context, String titre, String message, int iconId) {
		showOkDialog(context, titre, message, null, null, iconId);
	}
	public static void showOKDialog(Context context, String titre, String message, DontShowAgainLinkedToPreference dontShowAgain, int iconId) {
		showOkDialog(context, titre, message, dontShowAgain, null, iconId);
	}
	public static void showOKHtmlDialog(Context context, String titre, String message, DontShowAgainLinkedToPreference dontShowAgain, int iconId) {
		showOkHtmlDialog(context, titre, message, dontShowAgain, null, iconId);
	}
	public static void showOKDialog(Context context, String titre, String message, DialogInterface.OnClickListener onClickListener) {
		showOkDialog(context, titre, message, null, onClickListener, 0);
	}
	public static void showOKHtmlDialog(Context context, String titre, String message, DialogInterface.OnClickListener onClickListener) {
		showOkHtmlDialog(context, titre, message, null, onClickListener, 0);
	}
	public static void showOKDialog(Context context, String titre, String message, DontShowAgainLinkedToPreference dontShowAgain, final DialogInterface.OnClickListener onClickListener) {
		showOkDialog(context, titre, message, dontShowAgain, onClickListener, 0);
	}
	public static void showOKHtmlDialog(Context context, String titre, String message, DontShowAgainLinkedToPreference dontShowAgain, final DialogInterface.OnClickListener onClickListener) {
		showOkHtmlDialog(context, titre, message, dontShowAgain, onClickListener, 0);
	}

	public static void showOkDialog(final Context context, String titre, String message, DontShowAgainLinkedToPreference dontShowAgain, final DialogInterface.OnClickListener onClickListener, int iconId) {
		final AlertDialog.Builder builder = createOkDialogBuilderWithoutMessage(context, titre, dontShowAgain, onClickListener, iconId);
		builder.setMessage(message).show();
	}
	public static void showOkHtmlDialog(final Context context, String titre, String message, DontShowAgainLinkedToPreference dontShowAgain, final DialogInterface.OnClickListener onClickListener, int iconId) {
		final AlertDialog.Builder builder = createOkDialogBuilderWithoutMessage(context, titre, dontShowAgain, onClickListener, iconId);
		builder.setMessage(Html.fromHtml(message)).show();
	}

	private static AlertDialog.Builder createOkDialogBuilderWithoutMessage(final Context context, String titre, final DontShowAgainLinkedToPreference dontShowAgain, final DialogInterface.OnClickListener onClickListener, int iconId) {

		final AlertDialog.Builder builder = new AlertDialog.Builder(context);

		final CheckBox dontShowAgainCheckBox;

		if (dontShowAgain != null) {
			final LayoutInflater inflater = LayoutInflater.from(context);
			final View view = inflater.inflate(R.layout.dialog_ok_dontshow_checkbox, null);
			dontShowAgainCheckBox = (CheckBox) view.findViewById(R.id.dont_show);
			dontShowAgainCheckBox.setChecked(dontShowAgain.defaultValue);
			dontShowAgainCheckBox.setText(values_R.strings.dont_show_again);
			builder.setView(view);
		} else {
			dontShowAgainCheckBox = null;
		}

		builder.setCancelable(false)
				.setPositiveButton(ResourcesUtil.getString(context, android.R.string.ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (dontShowAgain != null) {
                            dontShowAgain.result = dontShowAgainCheckBox.isChecked();
							if (dontShowAgain.name != null && dontShowAgain.key != null) {
								final SharedPreferences settings = context.getSharedPreferences(dontShowAgain.name, dontShowAgain.mode);
                                final SharedPreferences.Editor editor = settings.edit();
								editor.putBoolean(dontShowAgain.key, dontShowAgain.result);
								editor.commit();
                            }
						}
						if (onClickListener != null) {
							onClickListener.onClick(dialog, id);
						}
						dialog.cancel();
					}
				});


		builder.setTitle(titre);
		if (iconId != 0)
			builder.setIcon(iconId);

		return builder;
	}
	public static ProgressDialog showProgress(Context context, String titre, String message, final DialogInterface.OnCancelListener listener) {

		ProgressDialog dialog = ProgressDialog.show(context, titre, message, true, true, new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				if (listener != null)
					listener.onCancel(dialog);
			}

		});
		return dialog;
	}

}
