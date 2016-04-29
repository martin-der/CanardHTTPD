package net.tetrakoopa.canardhttpd.util;

import android.content.Intent;
import android.net.Uri;

public class ToMduaUtil {

	public static void mimicIntent(Intent source, Intent destination) {
		final String action = source.getAction();
		final String type = source.getType();
		final String stringExtra = source.getStringExtra(Intent.EXTRA_TEXT);
		final Uri uri = source.getParcelableExtra(Intent.EXTRA_STREAM);
		if (action!=null)
			destination.setAction(action);
		if (type!=null)
			destination.setType(type);
		if (stringExtra!=null)
			destination.putExtra(Intent.EXTRA_TEXT, stringExtra);
		if (uri!=null)
			destination.putExtra(Intent.EXTRA_STREAM, uri);

	}
}
