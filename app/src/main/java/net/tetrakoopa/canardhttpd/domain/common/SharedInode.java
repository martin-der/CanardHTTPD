package net.tetrakoopa.canardhttpd.domain.common;

import android.net.Uri;

import java.io.File;


public abstract class SharedInode extends CommonSharedThing {

	private final File file;

	public SharedInode(String name, File file) {
		super(name);
		this.file = file;
	}

	public File getFile() {
		return file;
	}

	public String getUriAsString() {
		return Uri.fromFile(file).toString();
	}

}
