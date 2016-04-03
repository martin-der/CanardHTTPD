package net.tetrakoopa.canardhttpd.domain.sharing;

import android.net.Uri;

import net.tetrakoopa.canardhttpd.domain.common.SharedInode;

import java.io.File;


public class SharedFile extends SharedInode {

	private final String mimeType;

	private final String icon;

	public SharedFile(Uri uri, String fileName, String mimeType, String icon) {
		super(uri, fileName);
		this.mimeType = mimeType;
		this.icon = icon;
	}

	@Override
	public String getType() {
		final String mimetype = getMimeType();
		return mimetype == null ? "File" : "File("+mimetype+")";
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getIcon() {
		return icon;
	}
}
