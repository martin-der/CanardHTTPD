package net.tetrakoopa.canardhttpd.domain.common;

import android.net.Uri;

import java.io.File;


public abstract class SharedInode extends CommonSharedThing {

	private final File file;

	public SharedInode(Uri uri, String name) {
		super(uri, name);
		this.file = new File(uri.getPath());
	}

	public File getFile() {
		return file;
	}

	public String getUriAsString() {
		return Uri.fromFile(file).toString();
	}

}
