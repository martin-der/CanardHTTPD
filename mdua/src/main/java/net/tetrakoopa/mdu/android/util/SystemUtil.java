package net.tetrakoopa.mdu.android.util;

import java.io.File;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

	public static String getMimeTypeFromExtension(String extension) {
		MimeTypeMap mime = MimeTypeMap.getSingleton();
		return mime.getMimeTypeFromExtension(extension.toLowerCase(Locale.US));
	}

	public static void trySleeping(long time) {
		try { Thread.sleep(time); } catch (InterruptedException e) {}
	}

}
