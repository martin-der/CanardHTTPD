package net.tetrakoopa.canardhttpd.util;

import java.util.Locale;

/** I swear, temporary... */
public class TemporaryMimeTypeUtil {

	public static String basicMimeTypeFromExtension(String asset) {
		asset = asset.toLowerCase(Locale.US);
		
		if (asset.endsWith(".js"))
			return "text/javascript";
		if (asset.endsWith(".css"))
			return "text/css";
		
		if (asset.endsWith(".jpeg") || asset.endsWith(".jpg"))
			return "image/jpeg";
		if (asset.endsWith(".png"))
			return "image/png";
		if (asset.endsWith(".gif"))
			return "image/gif";
		if (asset.endsWith(".bmp"))
			return "image/bitmap";

		if (asset.endsWith(".ttf"))
			return "font/ttf";

		return null;
	}


}
