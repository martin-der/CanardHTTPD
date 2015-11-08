package net.tetrakoopa.canardhttpd.util;

import java.io.File;
import java.util.Locale;

/** I swear, temporary... */
public class TemporaryMimeTypeUtil {

	public static String getMimeType(String filename) {
		final int p = filename.indexOf('.');
		return p < 0 ? null : basicMimeTypeFromExtension(filename.substring(p+1,filename.length()));
	}
	public static String getMimeType(File file) {
		return getMimeType(file.getAbsolutePath());
	}
	public static String basicMimeTypeFromExtension(String extension) {
		extension = extension.toLowerCase(Locale.US);
		
		if (extension.equals("js"))
			return "text/javascript";
		if (extension.equals("css"))
			return "text/css";
		
		if (extension.equals("jpeg") || extension.equals("jpg"))
			return "image/jpeg";
		if (extension.equals("png"))
			return "image/png";
		if (extension.equals("gif"))
			return "image/gif";
		if (extension.equals("bmp"))
			return "image/bitmap";

		if (extension.equals("ttf"))
			return "font/ttf";

		return null;
	}


}
