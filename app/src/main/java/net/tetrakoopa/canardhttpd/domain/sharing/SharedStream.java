package net.tetrakoopa.canardhttpd.domain.sharing;

import android.net.Uri;

import net.tetrakoopa.canardhttpd.domain.common.CommonSharedThing;


public class SharedStream extends CommonSharedThing {

	private final String mimeType;

	public SharedStream(Uri uri, String mimeType, String name) {
		super(uri, name);
		this.mimeType = mimeType;
	}

	@Override
	public String getType() {
		final String mimeType = getMimeType();
		return mimeType == null ? "Stream" : "Stream("+mimeType+")";
	}

	public String getMimeType() {
		return mimeType;
	}
}
