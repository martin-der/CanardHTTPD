package net.tetrakoopa.mdu.android.view.util;

import net.tetrakoopa.mdu.android.util.ResourcesUtil;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

public class SystemUIUtil {

	private SystemUIUtil() {

	}

	public static void showOKDialog(Context context, String titre, String message) {
		showOkDialog(context, titre, message, null, 0);
	}
	public static void showOKDialog(Context context, String titre, String message, int iconId) {
		showOkDialog(context, titre, message, null, iconId);
	}
	public static void showOKDialog(Context context, String titre, String message, final DialogInterface.OnClickListener onClickListener) {
		showOkDialog(context, titre, message, onClickListener, 0);
	}

	public static void showOkDialog(Context context, String titre, String message, final DialogInterface.OnClickListener onClickListener, int iconId) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		builder.setMessage(message).setCancelable(false)
				.setPositiveButton(ResourcesUtil.getString(context, android.R.string.ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (onClickListener != null)
							onClickListener.onClick(dialog, id);
						dialog.cancel();
					}
				});

		AlertDialog alert = builder.create();

		alert.setTitle(titre);
		if (iconId != 0)
			alert.setIcon(iconId);
		alert.show();

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
