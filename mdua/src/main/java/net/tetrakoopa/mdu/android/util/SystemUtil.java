package net.tetrakoopa.mdu.android.util;

import java.io.File;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.webkit.MimeTypeMap;

public class SystemUtil {
	
	public static void installPackage(Context context, File apk) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}
	
	public static void shareText(Context context, String title, String text) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, text);
		context.startActivity(Intent.createChooser(intent, title));
	}
	public static void shareTextViaEMail(Context context, String title, String text) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("message/rfc822");
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		context.startActivity(intent);
	}

	public static String getMimeType(File file) {
		return getMimeType(Uri.fromFile(file));
	}

	public static String getMimeType(Uri uri) {
		MimeTypeMap mime = MimeTypeMap.getSingleton();
		String extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
		return mime.getMimeTypeFromExtension(extension);
	}

	public static String getMimeTypeFromFilename(String filename) {
		final int p = filename.indexOf('.');
		return p < 0 ? null : getMimeTypeFromExtension(filename.substring(p+1,filename.length()));
	}
	public static String getMimeTypeFromExtension(String extension) {
		MimeTypeMap mime = MimeTypeMap.getSingleton();
		return mime.getMimeTypeFromExtension(extension.toLowerCase(Locale.US));
	}

	public static void trySleeping(long time) {
		try { Thread.sleep(time); } catch (InterruptedException e) {}
	}


	public static String getFileName(Activity activity, Uri uri) {
		String result = null;
		if (uri.getScheme().equals("content")) {
			Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
			try {
				if (cursor != null && cursor.moveToFirst()) {
					result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
				}
			} finally {
				cursor.close();
			}
		}
		if (result == null) {
			result = uri.getPath();
			int cut = result.lastIndexOf('/');
			if (cut != -1) {
				result = result.substring(cut + 1);
			}
		}
		return result;
	}

}
