package net.tetrakoopa.canardhttpd.domain.sharing;

import net.tetrakoopa.canardhttpd.domain.common.SharedInode;

import java.io.File;


public class SharedFile extends SharedInode {

	private final String mimeType;

	private final String icon;

	public SharedFile(String fileName, File file, String mimeType, String icon) {
		super(fileName, file);
		this.mimeType = mimeType;
		this.icon = icon;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getIcon() {
		return icon;
	}
}
